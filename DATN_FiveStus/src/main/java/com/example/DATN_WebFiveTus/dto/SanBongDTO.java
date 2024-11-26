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
@ToString
@Builder
public class SanBongDTO {

    private Integer id;

    private String tenSanBong;

    private Integer idLoaiSan;

    private String tenLoaiSan;

    private String trangThai;


}
