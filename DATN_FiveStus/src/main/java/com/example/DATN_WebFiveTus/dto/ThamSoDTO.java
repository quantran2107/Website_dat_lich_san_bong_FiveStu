package com.example.DATN_WebFiveTus.dto;

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
@Builder
@ToString
public class ThamSoDTO {

    private Integer id;

    private String ma;

    private String ten;

    private String giaTri;

    private String typeGiaTri;

    private String moTa;

    private boolean isActive;

    private boolean trangThai;

}
