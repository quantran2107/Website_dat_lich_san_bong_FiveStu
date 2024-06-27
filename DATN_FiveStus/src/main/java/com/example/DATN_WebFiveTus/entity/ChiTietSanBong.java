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
@Table(name="chi_tiet_san_bong")
public class ChiTietSanBong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "id_san_bong", nullable = false)
    private SanBong sanBong;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "id_ngay_trong_tuan", nullable = false)
    private NgayTrongTuan ngayTrongTuan;

    @Column(name = "trang_thai", nullable = false, length = 100)
    private String trangThai;
}
