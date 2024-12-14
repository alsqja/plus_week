package com.example.demo.dto;

import lombok.Generated;
import lombok.Getter;
import org.jetbrains.annotations.TestOnly;

import java.util.List;

@Getter
public class ReportRequestDto {
    private List<Long> userIds;

    public ReportRequestDto() {
    }

    @TestOnly
    @Generated
    public ReportRequestDto(List<Long> userIds) {
        this.userIds = userIds;
    }
}
