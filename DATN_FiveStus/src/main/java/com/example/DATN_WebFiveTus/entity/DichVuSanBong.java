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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
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
@Table(name = "dich_vu_san_bong")
public class DichVuSanBong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "id_do_thue")
    private DoThue doThue;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_nuoc_uong")
    private NuocUong nuocUong;

    @Column(name = "don_gia", precision = 10, scale = 2)
    private BigDecimal donGia;

    @Column(name = "so_luong_do_thue")
    private int soLuongDoThue;

    @Column(name = "so_luong_nuoc_uong")
    private int soLuongNuocUong;

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
