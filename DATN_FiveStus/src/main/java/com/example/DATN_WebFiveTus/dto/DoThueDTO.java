package com.example.DATN_WebFiveTus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DoThueDTO {

    private int id;

    private float donGias;

    private int soLuongs;

    private String tenDoThue;

    private String trangThai;

    private Boolean deletedAt;

    private LocalDateTime createdAt;

    private String imageData;
}
