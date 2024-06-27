package com.example.DATN_WebFiveTus.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien", nullable = false)
    private NhanVien nhanVien;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_phieu_giam_gia", nullable = false)
    private PhieuGiamGia phieuGiamGia;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang", nullable = false)
    private KhachHang khachHang;

    @Column(name = "ma_hoa_don", nullable = false, length = 100)
    private String maHoaDon;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_tao")
    private Date ngayTao;

    @Column(name = "so_dien_thoai_nguoi_dat", nullable = false, length = 100)
    private String soDienThoaiNguoiDat;

    @Column(name = "ten_nguoi_dat", nullable = false, length = 100)
    private String tenNguoiDat;

    @Column(name = "tong_tien")
    private Float tongTien;

    @Column(name = "tien_coc")
    private Float tienCoc;

    @Column(name = "tien_con_lai")
    private Float tienConLai;

    @Column(name = "tien_thua")
    private Float tienThua;

    @Column(name = "ghi_chu", nullable = false, length = 100)
    private String ghiChu;

    @Column(name = "trang_thai", nullable = false, length = 100)
    private String trangThai;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_den_san")
    private Date ngayDenSan;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_thanh_toan")
    private Date ngayThanhToan;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private boolean deletedAt;
}
