package com.example.demo.dto;

import com.example.demo.dto.common.BaseResDtoType;
import com.example.demo.entity.Item;
import lombok.Getter;

@Getter
public class ItemResDto implements BaseResDtoType {

    private final Long id;
    private final String name;
    private final String description;
    private final Long ownerId;
    private final Long managerId;
    private final String status;

    public ItemResDto(Long id, String name, String description, Long ownerId, Long managerId, String status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.managerId = managerId;
        this.status = status;
    }

    public ItemResDto(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.description = item.getDescription();
        this.ownerId = item.getOwner().getId();
        this.managerId = item.getManager().getId();
        this.status = item.getStatus() == null ? "PENDING" : item.getStatus();
    }
}
