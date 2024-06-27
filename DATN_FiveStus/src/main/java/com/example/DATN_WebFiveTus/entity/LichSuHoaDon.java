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
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "lich_su_hoa_don")


public class LichSuHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    @Column(name = "hanh_dong", length = 255)
    private String hanhDong;

    @Column(name = "loai_hanh_dong", length = 255)
    private String loaiHanhDong;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_tao")
    private Date ngayTao;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ngay_cap_nhat")
    private Date ngayCapNhat;

    @Column(name = "ten_nguoi_tao", length = 255)
    private String tenNguoiTao;

    @Column(name = "ten_nguoi_cap_nhat", length = 255)
    private String tenNguoiCapNhat;

    @Column(name = "so_lan_thay_doi")
    private int soLanThayDoi;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;
}
