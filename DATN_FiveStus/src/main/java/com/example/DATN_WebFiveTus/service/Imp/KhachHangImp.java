package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.DiaChiDTO;

import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.entity.DiaChi;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.repository.DiaChiRepository;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
import com.example.DATN_WebFiveTus.service.KhachHangService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class KhachHangImp implements KhachHangService {
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private DiaChiRepository diaChiRepository;
    private ModelMapper modelMapper;


    public KhachHangImp(KhachHangRepository khachHangRepository, ModelMapper modelMapper) {

        this.khachHangRepository = khachHangRepository;

        this.modelMapper = modelMapper;
    }

    @Override
    public List<KhachHangDTO> getAll() {
        List<KhachHang>khachHangs=khachHangRepository.findAll();
        return khachHangs.stream().map(khachHang -> modelMapper.map(khachHang,KhachHangDTO.class)).collect(Collectors.toList());
    }

    @Override
    public KhachHangDTO getOne(Integer id) {
        return null;
    }

    @Override
    public KhachHangDTO save(KhachHangDTO khachHangDTO) {
        KhachHang khachHang = modelMapper.map(khachHangDTO, KhachHang.class);
        khachHang = khachHangRepository.save(khachHang);

        for (DiaChiDTO diaChiDTO : khachHangDTO.getDiaChi()) {
            DiaChi diaChi = modelMapper.map(diaChiDTO, DiaChi.class);
            diaChi.setKhachHang(khachHang);
            diaChiRepository.save(diaChi);
        }

        return modelMapper.map(khachHang, KhachHangDTO.class);
    }

    @Override
    public KhachHangDTO update(Integer id, KhachHangDTO khachHangDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
