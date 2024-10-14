package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.dto.GiaoCaRequest;
import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.dto.request.GiaoCaFormRequest;
import com.example.DATN_WebFiveTus.entity.GiaoCa;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.repository.GiaoCaRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.service.GiaoCaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.Repositories;
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
    @Autowired
    private Repositories repositories;
    @Autowired
    private NhanVienReposity nhanVienReposity;

    @Override
    public GiaoCaDTO getRowforId(int id) {
        LocalDate today = LocalDate.now();
        String status = "on";
        return modelMapper.map(giaoCaRepository.findByIdNhanVien(id, today, status), GiaoCaDTO.class);
    }

    @Override
    public Boolean changeGCN(int id, GiaoCaRequest request) {
        GiaoCa giaoCa = giaoCaRepository.findById(id).orElse(null);
        if (giaoCa != null) {
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

    @Override
    public GiaoCaDTO getLastRow() {
        List<GiaoCa> list = giaoCaRepository.findAll();
        if (list.isEmpty()) {
            return null;
        }
        return modelMapper.map(list.get(list.size() - 1), GiaoCaDTO.class);
    }

    @Override
    public Boolean addRow(GiaoCaFormRequest request) {
        NhanVien nhanVien = nhanVienReposity.findByMaNhanVien(request.getCodeNhanVien());
        if (nhanVien != null) {
            GiaoCa giaoCa = new GiaoCa();
            giaoCa.setNhanVienNhan(nhanVien);
            giaoCa.setTienMatCaTruoc(request.getTienMat());
            giaoCaRepository.save(giaoCa);

            return true;
        }
        return false;
    }

    @Override
    public Integer getIdNVG() {
        return 0;
    }
}
