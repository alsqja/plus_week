package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.jetbrains.annotations.TestOnly;

@Entity
@Getter
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private UserStatus status; // NORMAL, BLOCKED

    @Enumerated(value = EnumType.STRING)
    private Role role = Role.USER;

    public User(String role, String email, String nickname, String password, UserStatus status) {
        this.role = Role.of(role);
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.status = status;
    }

    public User() {
    }

    @TestOnly
    public User(Long id, String role, String email, String nickname, String password, UserStatus status) {
        this.id = id;
        this.role = Role.of(role);
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.status = status;
    }
}
