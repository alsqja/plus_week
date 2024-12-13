package com.example.demo.dto;

import com.example.demo.dto.common.BaseResDtoType;
import com.example.demo.entity.Role;
import lombok.Getter;

@Getter
public class Authentication implements BaseResDtoType {

    private final Long id;
    private final Role role;

    public Authentication(Long id, Role role) {
        this.id = id;
        this.role = role;
    }
}
