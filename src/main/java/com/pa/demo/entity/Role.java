package com.pa.demo.entity;

import jakarta.persistence.*;

import java.util.UUID;
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "role_name")
    private String roleName;
}
