package com.h.udemy.java.uservices.restaurant.dataaccess.repository;

import com.h.udemy.java.uservices.restaurant.dataaccess.entity.OrderApprovalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IOrderApprovalJpaRepository extends JpaRepository<OrderApprovalEntity, UUID> {


}
