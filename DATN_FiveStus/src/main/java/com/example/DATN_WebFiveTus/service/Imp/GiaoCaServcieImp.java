package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.config.security.CookieUtils;
import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import com.example.DATN_WebFiveTus.dto.ApiResponseDto;
import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.dto.request.GiaoCaFormRequest;
import com.example.DATN_WebFiveTus.dto.request.NhanCaRequest;
import com.example.DATN_WebFiveTus.dto.response.BanGiaoCaResponse;
import com.example.DATN_WebFiveTus.entity.ChiTietHinhThucThanhToan;
import com.example.DATN_WebFiveTus.entity.GiaoCa;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.repository.ChiTietHinhThucThanhToanRepository;
import com.example.DATN_WebFiveTus.repository.GiaoCaRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.service.GiaoCaService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Autowired
    private ChiTietHinhThucThanhToanRepository chiTietHinhThucThanhToanRepository;


    @Override
    public GiaoCaDTO lastData() {
        GiaoCa giaoCa = giaoCaRepository.findDataLast();
        if (giaoCa != null) {
            return modelMapper.map(giaoCa, GiaoCaDTO.class);
        }
        return null;
    }

    @Override
    public BanGiaoCaResponse banGiao(HttpServletRequest request) {
        GiaoCa giaoCa = getGiaoCa(request);
        if (giaoCa != null) {
            BanGiaoCaResponse banGiaoCaResponse = banGiaoCaResponse(giaoCa.getCreatedAt());
            banGiaoCaResponse.setTienMatDauCa(giaoCa.getTienMatCaTruoc());
            banGiaoCaResponse.setId(giaoCa.getId());
            return banGiaoCaResponse;
        }
        return null;
    }

    @Override
    public ResponseEntity<?> changeGC(HttpServletRequest request, GiaoCaFormRequest gcrq) {
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            NhanVien nv = nhanVienReposity.findByUsername(username);
            if (nv != null) {
                GiaoCa giaoCa = giaoCaRepository.findById(gcrq.getId()).orElse(null);
                if (giaoCa != null) {
                    giaoCa.setNhanVien(nv);
                    giaoCa.setGhiChu(gcrq.getGhiChu());
                    giaoCa.setTienChuyenKhoanTrongCa(gcrq.getTienChuyenKhoan());
                    giaoCa.setTienMatTrongCa(gcrq.getTienMatTrongCa());
                    giaoCa.setTongTienMatThucTe(gcrq.getTongTienMatThucTe());
                    giaoCa.setTongTienTrongCa(gcrq.getTongTienTrongCa());
                    giaoCa.setTrangThai(false);
                    giaoCa.setTongTienPhatSinh(gcrq.getTienPhatSinh());
                    giaoCaRepository.save(giaoCa);
                    return ResponseEntity.ok().body(true);
                }
            }
        }
        return ResponseEntity.ok().body(false);
    }

    @Override
    public Boolean addRow(HttpServletRequest request, NhanCaRequest requestBody) {
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            NhanVien nv = nhanVienReposity.findByUsername(username);
            GiaoCa giaoCa = new GiaoCa();
            giaoCa.setNhanVien(nv);
            giaoCa.setTrangThai(true);
            giaoCa.setTienMatCaTruoc(BigDecimal.valueOf(requestBody.getTienDauCa()));
            giaoCa.setTienMatTrongCa(BigDecimal.valueOf(0));
            giaoCa.setTienChuyenKhoanTrongCa(BigDecimal.valueOf(0));
            giaoCa.setTongTienTrongCa(BigDecimal.valueOf(0));
            giaoCa.setTongTienMatThucTe(BigDecimal.valueOf(0));
            giaoCa.setTongTienPhatSinh(BigDecimal.valueOf(0));
            giaoCaRepository.save(giaoCa);
            return true;
        }
        return false;
    }

    private GiaoCa getGiaoCa(HttpServletRequest request) {
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            NhanVien nv = nhanVienReposity.findByUsername(username);
            if (nv != null) {

                return giaoCaRepository.getRowLast(nv.getId());
            }
            return null;
        }
        return null;
    }

    private BanGiaoCaResponse banGiaoCaResponse(LocalDateTime createdAt) {

        BanGiaoCaResponse result = new BanGiaoCaResponse();
        result.setChuyenKhoanDatCoc(BigDecimal.valueOf(0));
        result.setTienMatDatCoc(BigDecimal.valueOf(0));

        List<ChiTietHinhThucThanhToan> listCK = chiTietHinhThucThanhToanRepository.findByCreatedAndChuyenKhoan(createdAt);
        List<ChiTietHinhThucThanhToan> listTM = chiTietHinhThucThanhToanRepository.findByCreatedAndTienMat(createdAt);
        if (listTM.isEmpty() && listCK.isEmpty()) {
            result.setTienMatSB(BigDecimal.valueOf(0));
            result.setTienMatSB(BigDecimal.valueOf(0));
            return result;
        }
        if (!listTM.isEmpty() && !listCK.isEmpty()) {
            BigDecimal totalAmountCK = listCK.stream()
                    .map(ChiTietHinhThucThanhToan::getSoTien)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalAmountTM = listCK.stream()
                    .map(ChiTietHinhThucThanhToan::getSoTien)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            result.setChuyenKhoanSB(totalAmountCK);
            result.setTienMatSB(totalAmountTM);
            return result;
        }
        if (listTM.isEmpty()) {
            BigDecimal totalAmount = listCK.stream()
                    .map(ChiTietHinhThucThanhToan::getSoTien)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            result.setChuyenKhoanSB(totalAmount);
            result.setTienMatSB(BigDecimal.valueOf(0));
            return result;
        }
        BigDecimal totalAmount = listTM.stream()
                .map(ChiTietHinhThucThanhToan::getSoTien)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        result.setTienMatSB(totalAmount);
        result.setTienMatSB(BigDecimal.valueOf(0));
        return result;


    }
}
