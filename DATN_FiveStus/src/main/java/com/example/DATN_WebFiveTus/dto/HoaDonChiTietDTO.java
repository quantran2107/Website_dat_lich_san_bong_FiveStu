package com.example.DATN_WebFiveTus.dto;

import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class HoaDonChiTietDTO {

    private Integer id;

    private Integer idHoaDon;

    private String maHoaDon;

    private Integer idNhanVien;

    private String tenNhanVien;

    private String maNhanVien;

    private Integer idNhanVienHoaDon;

    private String tenNhanVienHoaDon;

    private String maNhanVienHoaDon;

    private String maHoaDonChiTiet;

    private Integer idSanCa;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date ngayTaoHoaDon;

    @Temporal(TemporalType.DATE)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date ngayDenSan;

    private String ghiChu;

    private String trangThai;

    private String kieuNgayDat;

    private String tenSanBong;

    private String tenCa;

    private LocalDateTime thoiGianBatDauCa;

    private LocalDateTime thoiGianKetThucCa;

    private Integer idKhachHang;

    private String hoVaTenKhachHang;

    private String soDienThoaiKhachHang;

    private String emailKhachHang;

    private Boolean deletedAt;

    private String tenLoaiSan;

    private Double tongTien;

    private Double tienCocHdct;

    private Double tienGiamGia;

    private Double tongTienThucTe;

    private Integer idphieuGiamGia;

    private String maPhieuGiamGia;

    private String tenPhieuGiamGia;

    private Integer soLuongPhieuGianGia;

    private Float mucGiamPhieuGianGia;

    private Boolean hinhThucGiamGiaPhieuGianGia;

    private Float dieuKienSuDungPhieuGianGia;

    private Float giaTriToiDaPhieuGianGia;

    private Boolean doiTuongApDungPhieuGianGia;

    private Date ngayBatDauPhieuGianGia;

    private Date ngayKetThucPhieuGianGia;

    private String trangThaiPhieuGianGia;

    private Double giaSanCa;

}
