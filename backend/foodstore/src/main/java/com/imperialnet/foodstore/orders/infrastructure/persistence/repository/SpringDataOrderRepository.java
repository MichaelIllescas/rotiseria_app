package com.imperialnet.foodstore.orders.infrastructure.persistence.repository;

import com.imperialnet.foodstore.orders.infrastructure.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataOrderRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByStatus(String status);
}
