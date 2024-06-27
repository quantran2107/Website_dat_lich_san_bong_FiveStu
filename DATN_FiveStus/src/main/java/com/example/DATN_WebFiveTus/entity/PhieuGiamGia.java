package com.example.DATN_WebFiveTus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "phieu_giam_gia")
public class PhieuGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ma_phieu_giam_gia", length = 100)
    private String maPhieuGiamGia;
    @Column(name = "ten_phieu_giam_gia", length = 100)
    private String tenPhieuGiamGia;

    @Column(name = "so_luong")
    private int soLuong;

    @Column(name = "muc_giam", length = 100)
    private String mucGiam;

    @Column(name = "hinh_thuc_giam_gia")
    private Float hinhThucGiamGia;

    @Column(name = "dieu_kien_su_dung", length = 100)
    private String dieuKienSuDung;

    @Column(name = "ngay_bat_dau")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayBatDau;

    @Column(name = "ngay_ket_thuc")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ngayKetThuc;

    @Column(name = "trang_thai", length = 100)
    private String trangThai;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private boolean deletedAt;
}


