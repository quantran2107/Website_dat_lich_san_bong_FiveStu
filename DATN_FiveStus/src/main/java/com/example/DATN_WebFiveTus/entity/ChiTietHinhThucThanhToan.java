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
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name = "chi_tiet_hinh_thuc_thanh_toan")
public class ChiTietHinhThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "id_hoa_don")
    private HoaDon hoaDon;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_hinh_thuc_thanh_toan")
    private HinhThucThanhToan hinhThucThanhToan;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;
}
