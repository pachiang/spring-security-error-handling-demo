package com.pa.demo.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "authorities")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String name;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;
}
