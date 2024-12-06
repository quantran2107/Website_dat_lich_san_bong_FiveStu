package com.example.DATN_WebFiveTus.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LichLamViecSearchRequest {
    private LocalDate date;
    private String status;
}
