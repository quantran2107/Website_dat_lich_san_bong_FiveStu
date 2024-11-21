package com.example.DATN_WebFiveTus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private String emailKhachHang;

    private Boolean loai;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date ngayTao;

    private String ghiChu;

    private String trangThai;

    private Double tongTienSan;

    private Double tienCoc;

    private Double tongTien;

}
