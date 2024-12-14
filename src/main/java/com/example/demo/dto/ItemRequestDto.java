package com.example.demo.dto;

import lombok.Generated;
import lombok.Getter;
import org.jetbrains.annotations.TestOnly;

@Getter
public class ItemRequestDto {
    private String name;

    private String description;

    private Long managerId;

    private Long ownerId;

    @TestOnly
    @Generated
    public ItemRequestDto(String name, String description, Long managerId, Long ownerId) {
        this.name = name;
        this.description = description;
        this.managerId = managerId;
        this.ownerId = ownerId;
    }
}
