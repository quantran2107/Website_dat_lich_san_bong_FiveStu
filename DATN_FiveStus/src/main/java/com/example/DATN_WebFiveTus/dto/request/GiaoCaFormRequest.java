package com.example.DATN_WebFiveTus.dto.request;

import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class GiaoCaFormRequest {
    private String codeNhanVien ;

    private BigDecimal tienMat;
}
