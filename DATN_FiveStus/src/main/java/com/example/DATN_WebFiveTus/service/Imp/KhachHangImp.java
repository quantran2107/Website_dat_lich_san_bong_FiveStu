package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.DiaChiDTO;

import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
import com.example.DATN_WebFiveTus.service.KhachHangService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class KhachHangImp implements KhachHangService {

    private KhachHangRepository diaChiRepository;

    private ModelMapper modelMapper;

    @Autowired
    public KhachHangImp(KhachHangRepository khachHangRepository, ModelMapper modelMapper) {

        this.diaChiRepository = diaChiRepository;

        this.modelMapper = modelMapper;
    }

    @Override
    public List<KhachHangDTO> getAll() {
        return null;
    }

    @Override
    public KhachHangDTO getOne(Integer id) {
        return null;
    }

    @Override
    public KhachHangDTO save(KhachHangDTO khachHangDTO) {
        return null;
    }

    @Override
    public KhachHangDTO update(Integer id, KhachHangDTO khachHangDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
