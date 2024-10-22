package com.example.DATN_WebFiveTus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtAuthResponse {

    private String accessToken;

    private String tokenType = "Bearer";

    private String roles;
}
