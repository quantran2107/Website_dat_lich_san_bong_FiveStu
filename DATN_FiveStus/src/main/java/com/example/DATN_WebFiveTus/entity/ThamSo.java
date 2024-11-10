package com.example.DATN_WebFiveTus.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "tham_so")
public class ThamSo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma", nullable = false)
    private String ma;

    @Column(name = "ten", nullable = false)
    private String ten;

    @Column(name = "gia_tri", nullable = false)
    private String giaTri;

    @Column(name = "type_gia_tri", nullable = false)
    private String typeGiaTri;

    @Column(name = "mo_ta")
    private String moTa;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "trang_thai", nullable = false)
    private boolean trangThai;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at", columnDefinition = "BIT(0) DEFAULT 0")
    private Boolean deletedAt = true;

}
