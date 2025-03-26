package com.h.udemy.java.uservices.customer.service.dataaccess.repository;

import com.h.udemy.java.uservices.customer.service.dataaccess.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {

    Optional<CustomerEntity> findByUsername(String username);
}
