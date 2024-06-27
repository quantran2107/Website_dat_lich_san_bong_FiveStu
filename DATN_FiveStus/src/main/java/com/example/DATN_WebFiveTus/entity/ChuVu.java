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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name="chuc_vu")
public class ChuVu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ten_chuc_vu", nullable = false, length = 100)
    private String tenChucVu;

    @Column(name = "ma_nhan_vien", nullable = false, length = 100)
    private String maNhanVien;

    @Column(name = "trang_thai", nullable = false, length = 100)
    private String trangThai;
}
