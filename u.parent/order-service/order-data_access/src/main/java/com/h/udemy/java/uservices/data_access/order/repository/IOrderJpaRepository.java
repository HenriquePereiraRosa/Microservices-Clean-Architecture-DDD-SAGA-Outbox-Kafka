package com.h.udemy.java.uservices.data_access.order.repository;

import com.h.udemy.java.uservices.data_access.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IOrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<OrderEntity> findByTrackingId(UUID trackingId);
}
