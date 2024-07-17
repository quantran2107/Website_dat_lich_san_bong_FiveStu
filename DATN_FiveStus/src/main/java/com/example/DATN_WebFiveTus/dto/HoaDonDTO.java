package com.example.DATN_WebFiveTus.dto;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class HoaDonDTO {

    private int id;

    private Integer idNhanVien;

    private String tenNhanVien;

    private String maNhanVien;

    private Integer idPhieuGiamGia;

    private Integer idKhachHang;

    private String maHoaDon;

    private String hoVaTenKhachHang;

    private String soDienThoaiKhachHang;

    private Boolean loai;

    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayTao;

    private Float tongTien;

    private Float tienCoc;

    private Float tienConLai;

    private Float tienThua;

    private String ghiChu;

    private String trangThai;

    @Temporal(TemporalType.DATE)
    private Date ngayDenSan;

    @Temporal(TemporalType.DATE)
    private Date ngayThanhToan;

}
