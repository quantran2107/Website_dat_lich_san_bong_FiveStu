package com.example.DATN_WebFiveTus.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountDTO {

    private String ma;

    private String username;

    private String email;

    private String passwords;

    private boolean enableds;
}
