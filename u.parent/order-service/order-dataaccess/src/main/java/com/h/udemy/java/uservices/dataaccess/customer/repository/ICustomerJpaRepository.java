package com.h.udemy.java.uservices.dataaccess.customer.repository;

import com.h.udemy.java.uservices.dataaccess.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICustomerJpaRepository extends JpaRepository<CustomerEntity, UUID> {
}
