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
@Table(name="nhan_vien")
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ten_nhan_vien", nullable = false, length = 100)
    private String tenNhanVien;

    @Column(name = "ma_nhan_vien", nullable = false, length = 100)
    private String maNhanVien;

    @Column(name = "mat_khau", nullable = false, length = 100)
    private String matKhau;

    @Column(name = "ho_ten", nullable = false, length = 100)
    private String hoTen;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "gioi_tinh", nullable = false, length = 100)
    private String gioiTinh;

    @Column(name = "so_dien_thoai", nullable = false, length = 100)
    private String soDienThoai;

    @Column(name = "dia_chi", nullable = false, length = 100)
    private String diaChi;

    @Column(name = "trang_thai", nullable = false, length = 100)
    private String trangThai;
}
