package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.DiaChiKhachHangDTO;
import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.entity.DiaChiKhachHang;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.repository.DiaChiKhachHangRepository;
import com.example.DATN_WebFiveTus.service.DiaChiKhachHangService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaChiKhachHangLmp implements DiaChiKhachHangService {
    @Autowired
    private DiaChiKhachHangRepository diaChiKhachHangRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<DiaChiKhachHangDTO> getAll() {
        List<DiaChiKhachHang> khachHangs = diaChiKhachHangRepository.findAll();
        return khachHangs.stream()
                .map(diaChiKhachHang -> modelMapper.map(diaChiKhachHang, DiaChiKhachHangDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public DiaChiKhachHangDTO getOne(Integer id) {
        return null;
    }

    @Override
    public DiaChiKhachHangDTO save(DiaChiKhachHangDTO diaChiKhachHangDTO) {
        return null;
    }

    @Override
    public DiaChiKhachHangDTO update(Integer id, DiaChiKhachHangDTO diaChiKhachHangDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<DiaChiKhachHangDTO> getAllJoinFetch() {
        return null;
    }
}
