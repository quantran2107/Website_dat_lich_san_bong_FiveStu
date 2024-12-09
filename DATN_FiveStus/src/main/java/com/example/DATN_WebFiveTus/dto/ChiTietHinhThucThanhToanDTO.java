package com.example.DATN_WebFiveTus.dto;

import com.example.DATN_WebFiveTus.entity.HinhThucThanhToan;
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

public class ChiTietHinhThucThanhToanDTO {

    private Integer id;

    private Integer idHoaDonChiTiet;

    private Integer idHinhThucThanhToan;

    private Float soTien;

    private String maGiaoDich;

    private String trangThai;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean deletedAt;

}
