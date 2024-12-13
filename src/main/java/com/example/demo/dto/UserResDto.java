package com.example.demo.dto;

import com.example.demo.dto.common.BaseResDtoType;
import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.entity.UserStatus;
import lombok.Getter;

@Getter
public class UserResDto implements BaseResDtoType {

    private final Long id;
    private final String email;
    private final UserStatus status;
    private final Role role;

    public UserResDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.role = user.getRole();
    }
}
