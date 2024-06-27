package com.example.DATN_WebFiveTus.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@ToString
@Builder
public class NgayTrongTuanDTO {

    private int id;

    private String thuTrongTuan;

    private Float giaCa;

    private Float heSo;

    private String trangThai;
}
