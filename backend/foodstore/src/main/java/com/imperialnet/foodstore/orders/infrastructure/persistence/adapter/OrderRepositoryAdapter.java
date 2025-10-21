package com.imperialnet.foodstore.orders.infrastructure.persistence.adapter;

import com.imperialnet.foodstore.orders.application.ports.out.OrderRepositoryPort;
import com.imperialnet.foodstore.orders.domain.model.Order;
import com.imperialnet.foodstore.orders.infrastructure.mapper.OrderEntityMapper;
import com.imperialnet.foodstore.orders.infrastructure.persistence.entity.OrderEntity;
import com.imperialnet.foodstore.orders.infrastructure.persistence.repository.SpringDataOrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de infraestructura que implementa el puerto de salida OrderRepositoryPort.
 * Usa JPA para persistir las órdenes.
 */
@Component
public class OrderRepositoryAdapter implements OrderRepositoryPort {

    private final SpringDataOrderRepository jpaRepository;
    private final OrderEntityMapper mapper;

    public OrderRepositoryAdapter(SpringDataOrderRepository jpaRepository, OrderEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    // =========================================================
    // 🔹 Guardar (Domain → Entity)
    // =========================================================
    @Override
    @Transactional
    public Order save(Order order) {
        if (order == null) {
            throw new IllegalArgumentException("No se puede guardar una orden nula.");
        }

        // Mapear dominio → entidad
        OrderEntity entity = mapper.toEntity(order);

        // ✅ Asegurar relación bidireccional (por si algún ítem quedó sin setOrder)
        if (entity.getItems() != null) {
            entity.getItems().forEach(item -> item.setOrder(entity));
        }

        // Guardar en BD
        OrderEntity savedEntity = jpaRepository.save(entity);

        // Devolver dominio mapeado
        return mapper.toDomain(savedEntity);
    }

    // =========================================================
    // 🔹 Buscar por ID (Entity → Domain)
    // =========================================================
    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    // =========================================================
    // 🔹 Eliminar por ID
    // =========================================================
    @Override
    @Transactional
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    // =========================================================
    // 🔹 Buscas Todos
    // =========================================================

    @Override
    public List<Order> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    // =========================================================
    // 🔹 Buscar por Estado
    // =========================================================

    @Override
    public List<Order> findByStatus(String status) {
        return jpaRepository.findByStatus(status)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    // =========================================================
    // 🔹
    // =========================================================

}
