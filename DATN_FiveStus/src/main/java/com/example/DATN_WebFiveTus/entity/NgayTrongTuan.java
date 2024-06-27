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
@Table(name="ngay_trong_tuan")
public class NgayTrongTuan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "thu_trong_tuan", nullable = false, length = 100)
    private String thuTrongTuan;

    @Column(name = "gia_ca")
    private Float giaCa;

    @Column(name = "he_so")
    private Float heSo;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;
}
