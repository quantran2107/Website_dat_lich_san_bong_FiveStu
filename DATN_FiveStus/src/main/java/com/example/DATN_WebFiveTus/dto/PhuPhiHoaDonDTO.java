package com.example.DATN_WebFiveTus.dto;

import lombok.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PhuPhiHoaDonDTO {
    private int id;
    private int hoaDonChiTietId; // ID của HoaDonChiTiet
    private String ma;
    private String ten;
    private Double tienPhuPhi;
    private String ghiChu;
    private String trangThai;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean deletedAt;
}
