package com.example.DATN_WebFiveTus.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OtpRequest {
    private String otp;
    private String email;
}
