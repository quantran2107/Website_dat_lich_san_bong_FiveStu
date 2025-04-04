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
@Table(name="hoa_don_chi_tiet")
public class HoaDonChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_phieu_giam_gia", nullable = false)
    private PhieuGiamGia phieuGiamGia;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_nhan_vien", nullable = false)
    private NhanVien nhanVien;

    @Column(name = "ma_hoa_don_chi_tiet")
    private String maHoaDonChiTiet;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_san_ca")
    private SanCa sanCa;

    @Column(name = "tong_tien")
    private Double tongTien;

    @Column(name = "tien_coc_hdct")
    private Double tienCocHdct;

    @Column(name = "tien_giam_gia")
    private Double tienGiamGia;

    @Column(name = "tong_tien_thuc_te")
    private Double tongTienThucTe;

    @Column(name = "kieu_ngay_dat")
    private String kieuNgayDat;

    @Column(name = "ghi_chu", length = 200)
    private String ghiChu;

    @Temporal(TemporalType.DATE)
    @Column(name = "ngay_den_san")
    private Date ngayDenSan;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private Boolean deletedAt;

    @Column(name = "trang_thai", length = 100)
    private String trangThai;

}
