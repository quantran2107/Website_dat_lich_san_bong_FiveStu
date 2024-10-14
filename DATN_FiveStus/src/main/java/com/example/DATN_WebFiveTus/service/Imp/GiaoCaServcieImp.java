package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.dto.GiaoCaRequest;
import com.example.DATN_WebFiveTus.entity.GiaoCa;
import com.example.DATN_WebFiveTus.repository.GiaoCaRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.service.GiaoCaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GiaoCaServcieImp implements GiaoCaService {

    @Autowired
    private GiaoCaRepository giaoCaRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public GiaoCaDTO getRowforId(int id) {
        LocalDate today = LocalDate.now();
        String status="on";
        return modelMapper.map(giaoCaRepository.findByIdNhanVien(id,today,status),GiaoCaDTO.class);
    }

    @Override
    public Boolean changeGCN(int id, GiaoCaRequest request) {
        GiaoCa giaoCa = giaoCaRepository.findById(id).orElse(null);
        if (giaoCa!=null){
            giaoCa.setTienMatTrongCa(request.getTienMatTrongCa());
            giaoCa.setTienChuyenKhoanTrongCa(request.getTienChuyenKhoanTrongCa());
            giaoCa.setTongTienTrongCa(request.getTongTienTrongCa());
            giaoCa.setTongTienMatThucTe(request.getTongTienMatThucTe());
            giaoCa.setTongTienPhatSinh(request.getTongTienPhatSinh());
            giaoCa.setTrangThai(request.getTrangThai());
            giaoCa.setGhiChu(request.getGhiChu());
            giaoCaRepository.save(giaoCa);
            return true;
        }
        return false;
    }
}
