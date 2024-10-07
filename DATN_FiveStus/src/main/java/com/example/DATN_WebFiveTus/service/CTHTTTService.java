package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.HTTTDto;

import java.util.List;

public interface CTHTTTService {
    List<HTTTDto> getHtttById(int id);

    Boolean addNew(HTTTDto htttDto);
}
