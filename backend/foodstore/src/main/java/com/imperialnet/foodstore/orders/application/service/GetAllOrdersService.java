package com.imperialnet.foodstore.orders.application.service;

import com.imperialnet.foodstore.orders.application.ports.in.GetAllOrdersUseCase;
import com.imperialnet.foodstore.orders.application.ports.out.OrderRepositoryPort;
import com.imperialnet.foodstore.orders.domain.model.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
@Service
public class GetAllOrdersService implements GetAllOrdersUseCase {

    private final OrderRepositoryPort orderRepository;

    public GetAllOrdersService(OrderRepositoryPort orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        MDC.put("action", "GET_ALL_ORDERS");
        long start = System.nanoTime();
        try {
            log.debug("Inicio GetAllOrdersService.getAllOrders");
            List<Order> orders = orderRepository.findAll();
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            log.info("Pedidos obtenidos: {} en {} ms", orders.size(), durationMs);
            return orders;
        } catch (RuntimeException e) {
            long durationMs = (System.nanoTime() - start) / 1_000_000;
            log.error("Error obteniendo pedidos después de {} ms: {}",
                    durationMs, e.getMessage(), e);
            throw e;
        } finally {
            MDC.clear();
        }
    }
}
