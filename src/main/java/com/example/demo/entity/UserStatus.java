package com.example.demo.entity;

import lombok.Getter;

@Getter
public enum UserStatus {
    NORMAL("normal"),
    BLOCKED("blocked");

    private final String name;

    UserStatus(String name) {
        this.name = name;
    }

    public static UserStatus of(String statusName) {
        for (UserStatus status : values()) {
            if (status.getName().equals(statusName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("해당되는 이름의 상태를 찾을 수 없습니다. " + statusName);
    }
}
