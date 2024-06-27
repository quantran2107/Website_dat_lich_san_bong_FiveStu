package com.example.DATN_WebFiveTus.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SanBongDTO {

    private int id;

    private String tenSanBong;

    private Float giaSan;

    private String trangThai;
}
