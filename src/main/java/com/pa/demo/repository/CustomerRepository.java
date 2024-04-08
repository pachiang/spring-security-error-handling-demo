package com.pa.demo.repository;

import com.pa.demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Customer findOneByEmail(String name);

    Page<Customer> findAll(Pageable pageable);
}
