package com.example.DATN_WebFiveTus.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "giao_ca")


public class GiaoCa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_nv_nhan_ca")
    private NhanVien nhanVienNhan;

    @ManyToOne
    @JoinColumn(name = "id_nv_ban_giao")
    private NhanVien nhanVienGiao;

    @Column(name = "tien_phat_sinh")
    private Float tienPhatSinh;

    @Column(name = "thoi_gian_ket_ca", length = 100)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime thoiGianKetCa;

    @Column(name = "tien_ban_dau", length = 50)
    private Float tienBanDau;

    @Column(name = "tong_tien_khac", length = 50)
    private Float tongTienKhac;

    @Column(name = "tong_tien_mat", length = 50)
    private Float tongTienMat;

    @Column(name = "tong_tien_mat_ca_truoc", length = 50)
    private Float tongTienMatCaTruoc;

    @Column(name = "tong_tien_mat_rut", length = 50)
    private Float tongTienMatRut;

    @Column(name = "tong_tien_trong_ca", length = 50)
    private Float tongTienTrongCa;

    @Column(name = "ghi_chu_phat_sinh", length = 50)
    private String ghiChuPhatSinh;

    @Column(name = "trang_thai", length = 50)
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
