package com.example.demo.dto.common;

import lombok.Getter;

import java.util.List;

@Getter
public class CommonListResDto<T extends BaseResDtoType> {

    private final String message;
    private final List<T> data;

    public CommonListResDto(String message, List<T> data) {
        this.message = message;
        this.data = data;
    }
}
