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

import java.math.BigDecimal;
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
    @JoinColumn(name = "id_nhan_vien")
    private NhanVien nhanVien;

    @Column(name = "tien_mat_ca_truoc", precision = 38, scale = 2)
    private BigDecimal tienMatCaTruoc;

    @Column(name = "tien_mat_trong_ca", precision = 38, scale = 2)
    private BigDecimal tienMatTrongCa;

    @Column(name = "tien_ck_trong_ca", precision = 38, scale = 2)
    private BigDecimal tienChuyenKhoanTrongCa;

    @Column(name = "tong_tien_trong_ca", precision = 38, scale = 2)
    private BigDecimal tongTienTrongCa;

    @Column(name = "tong_tien_mat_thuc_te", precision = 38, scale = 2)
    private BigDecimal tongTienMatThucTe;

    @Column(name = "tong_tien_phat_sinh", precision = 38, scale = 2)
    private BigDecimal tongTienPhatSinh;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private Boolean deletedAt;

}
