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
@Table(name = "tham_so")
public class ThamSo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "id_loai_tham_so")
    private LoaiThamSo loaiThamSo;

    @Column(name = "ten", length = 100)
    private String ten;

    @Column(name = "gia_tri", length = 100)
    private String giaTri;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;
}
