package com.example.DATN_WebFiveTus.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
@ToString
@Builder
public class DichVuSanBongDTO {

    private int id;

    private Integer idDoThue;

    private Integer idNuocUong;

    private Float donGia;

    private int soLuongDoThue;

    private int soLuongNuocUong;

    private String trangThai;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private boolean deletedAt;
}

