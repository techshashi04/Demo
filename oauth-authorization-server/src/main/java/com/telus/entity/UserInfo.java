package com.telus.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Entity
@Data
@RequiredArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 40)
    private String userName;
    @Column(length = 120)
    private String email;

    @Column(length = 60)
    private String password;
    @Column(length = 255)
    private List<String> roles;
    @Column
    private boolean enabled = true;
}
