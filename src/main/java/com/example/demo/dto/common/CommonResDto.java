package com.example.demo.dto.common;

import lombok.Getter;

@Getter
public class CommonResDto<T extends BaseResDtoType> {

    private final String message;
    private final T data;

    public CommonResDto(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
