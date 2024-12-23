package com.example.demo.controller;

import com.example.demo.dto.ItemRequestDto;
import com.example.demo.dto.ItemResDto;
import com.example.demo.dto.common.CommonResDto;
import com.example.demo.entity.Item;
import com.example.demo.service.ItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }
    
    @PostMapping
    public ResponseEntity<CommonResDto<ItemResDto>> createItem(@RequestBody ItemRequestDto itemRequestDto) {
        Item item = itemService.createItem(itemRequestDto.getName(),
                itemRequestDto.getDescription(),
                itemRequestDto.getOwnerId(),
                itemRequestDto.getManagerId());

        return ResponseEntity.status(201).body(new CommonResDto<>("create item", new ItemResDto(item)));
    }
}
