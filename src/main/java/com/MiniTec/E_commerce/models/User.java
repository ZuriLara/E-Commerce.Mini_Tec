package com.MiniTec.E_commerce.models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 225, nullable = false)
    private String name;

    @Column(length = 225, nullable = false)
    private String lastname;

    @Column(length = 225, nullable = false, unique = true)
    private String email;

    @Column(length = 4, nullable = true, unique = true)
    private String matriculate;

    @Column(length = 225, nullable = true)
    private String image;

    @Column(length = 225, nullable = false)
    private String password;

    @Column(name = "notification_token", length = 225, nullable = false)
    private String notificationToken;

    @Column(name="created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name="updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    public User() {}
    @PreUpdate
    public void onUpdate(){
        this.updatedAt = LocalDateTime.now();
    }



}
