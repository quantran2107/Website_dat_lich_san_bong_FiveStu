package com.example.DATN_WebFiveTus.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Date;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Entity
@Table(name="ca")
public class Ca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ten_ca", nullable = false, length = 100)
    private String tenCa;

    @Column(name = "gia_ca")
    private Float giaCa;

    @Column(name = "thoi_gian_bat_dau")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime thoiGianBatDau;

    @Column(name = "thoi_gian_ket_thuc")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime  thoiGianKetThuc;

    @Column(name = "trang_thai", length = 50)
    private String trangThai;
}
