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

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name="dia_chi")
public class DiaChi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "thanh_pho", length = 100, columnDefinition = "VARCHAR(100) DEFAULT ''")
    private String thanhPho;

    @Column(name = "quan_huyen", length = 100, columnDefinition = "VARCHAR(100) DEFAULT ''")
    private String quanHuyen;

    @Column(name = "phuong_xa", length = 100, columnDefinition = "VARCHAR(100) DEFAULT ''")
    private String phuongXa;

    @Column(name = "ghi_chu", length = 100, columnDefinition = "VARCHAR(100) DEFAULT ''")
    private String ghiChu;

    @Column(name = "trang_thai", length = 100, columnDefinition = "VARCHAR(100) DEFAULT ''")
    private Boolean trangThai;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private boolean deletedAt;
}
