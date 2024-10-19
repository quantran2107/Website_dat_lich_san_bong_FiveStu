package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.config.security.CookieUtils;
import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.dto.GiaoCaRequest;
import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.dto.request.GiaoCaFormRequest;
import com.example.DATN_WebFiveTus.entity.GiaoCa;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.repository.GiaoCaRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.service.GiaoCaService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Service
public class GiaoCaServcieImp implements GiaoCaService {

    @Autowired
    private GiaoCaRepository giaoCaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NhanVienReposity nhanVienReposity;

    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public GiaoCaDTO getRowforId(int id) {
        LocalDate today = LocalDate.now();
        GiaoCa giaoCa = giaoCaRepository.findByIdNhanVien(id, today);
        if (giaoCa != null) {
            return modelMapper.map(giaoCa, GiaoCaDTO.class);
        }
        return null;
    }
    @Override
    public Boolean addRow(GiaoCaFormRequest request) {
        NhanVien nhanVien = nhanVienReposity.findByMaNhanVien(request.getCodeNhanVien());
        if (nhanVien != null) {
            GiaoCa giaoCa = new GiaoCa();
            giaoCa.setNhanVienNhan(nhanVien);
            giaoCa.setTienMatCaTruoc(request.getTienMat());
            giaoCa.setTrangThai(true);
            giaoCaRepository.save(giaoCa);
            return true;
        }
        return false;
    }

    @Override
    public Integer getIdNVG(HttpServletRequest request) {
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            NhanVien nv = nhanVienReposity.findByUsername(username);
            if (nv != null) {
                return nv.getId();
            }
            return 0;
        }
        return 0;
    }

    @Override
    public Boolean changeGCN(GiaoCaRequest request) {
        GiaoCa giaoCa = giaoCaRepository.getRowLast();
        giaoCa.setTienMatTrongCa(request.getTienMatTrongCa());
        giaoCa.setTienChuyenKhoanTrongCa(request.getTienChuyenKhoanTrongCa());
        giaoCa.setTongTienTrongCa(request.getTongTienTrongCa());
        giaoCa.setTongTienMatThucTe(request.getTongTienMatThucTe());
        giaoCa.setTongTienPhatSinh(request.getTongTienPhatSinh());
        giaoCa.setGhiChu(request.getGhiChu());
        giaoCa.setTrangThai(request.getTrangThai());
        giaoCaRepository.save(giaoCa);
        return true;
    }

    @Override
    public Boolean getLastRow(HttpServletRequest request) {
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            NhanVien nv = nhanVienReposity.findByUsername(username);
            if (nv != null) {
                GiaoCa giaoCa = giaoCaRepository.getRowLast();
                LocalDate today = LocalDate.now();
                if (giaoCa.getCreatedAt().toLocalDate().isBefore(today)){
                    GiaoCa giaoCa1 = new GiaoCa();
                    giaoCa1.setTienMatCaTruoc(BigDecimal.valueOf(100000));
                    giaoCa1.setNhanVienNhan(nv);
                    giaoCa1.setTrangThai(true);
                    giaoCaRepository.save(giaoCa1);
                    return true;
                }
                return today.equals(giaoCa.getCreatedAt().toLocalDate()) && giaoCa.getTrangThai() && nv.getId() == giaoCa.getNhanVienNhan().getId();
            }
            return false;
        }
        return false;
    }

    @Override
    public GiaoCaDTO checkNhanCa() {
        List<GiaoCa> list = giaoCaRepository.findAll();
        if (list.isEmpty()) {
            return null;
        }
        return modelMapper.map(list.get(list.size() - 1), GiaoCaDTO.class);
    }

    @Override
    public NhanVienDTO getNV(HttpServletRequest request) {
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            NhanVien nv = nhanVienReposity.findByUsername(username);
            if (nv != null) {
                return modelMapper.map(nv, NhanVienDTO.class);
            }
            return null;
        }
        return null;
    }
}
