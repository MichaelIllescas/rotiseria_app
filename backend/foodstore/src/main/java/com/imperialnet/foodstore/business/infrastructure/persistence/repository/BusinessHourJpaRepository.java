package com.imperialnet.foodstore.business.infrastructure.persistence.repository;


import com.imperialnet.foodstore.business.infrastructure.persistence.entity.BusinessHourEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessHourJpaRepository extends JpaRepository<BusinessHourEntity, Long> { }