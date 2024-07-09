package com.example.DATN_WebFiveTus.dto;

import com.example.DATN_WebFiveTus.entity.KhachHang;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class DiaChiKhachHangDTO {

    private Integer id;

    private String diaChiCuThe;

    private String thanhPho;

    private String quanHuyen;

    private String phuongXa;

    private String ghiChu;

    private String trangThai;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deletedAt;

}
