package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.config.security.CookieUtils;
import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import com.example.DATN_WebFiveTus.dto.ApiResponseDto;
import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.dto.request.NhanCaRequest;
import com.example.DATN_WebFiveTus.dto.response.BanGiaoCaResponse;
import com.example.DATN_WebFiveTus.entity.ChiTietHinhThucThanhToan;
import com.example.DATN_WebFiveTus.entity.GiaoCa;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.entity.auth.ResponseStatus;
import com.example.DATN_WebFiveTus.repository.ChiTietHinhThucThanhToanRepository;
import com.example.DATN_WebFiveTus.repository.GiaoCaRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.service.GiaoCaService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ApiResponseDto<?> banGiao(HttpServletRequest request) {
        GiaoCa giaoCa = getGiaoCa(request);
        if (giaoCa != null) {
            List<BanGiaoCaResponse> list = banGiaoCaResponse(giaoCa.getNhanVien().getId());
            if (list.isEmpty()) {
                giaoCa.setTienChuyenKhoanTrongCa(BigDecimal.valueOf(0));
                giaoCa.setTienMatTrongCa(BigDecimal.valueOf(0));
                giaoCa.setTongTienTrongCa(BigDecimal.valueOf(0));
                giaoCa.setTrangThai(false);
                giaoCaRepository.save(giaoCa);
                return ApiResponseDto.builder().status(String.valueOf(HttpStatus.CREATED)).message("Done!").response(true).build();
            }
            BigDecimal tongTien = BigDecimal.ZERO;
            for (BanGiaoCaResponse bg:list){
                if (bg.getHinhThucThanhToan().equalsIgnoreCase("chuyển khoản")){
                    giaoCa.setTienChuyenKhoanTrongCa(bg.getTongTien());
                } else {
                    giaoCa.setTienMatTrongCa(bg.getTongTien());
                }
                tongTien = tongTien.add(bg.getTongTien());
            }
            giaoCa.setTongTienTrongCa(tongTien);
            giaoCa.setTrangThai(false);
            giaoCaRepository.save(giaoCa);
            return ApiResponseDto.builder().status(String.valueOf(HttpStatus.CREATED)).message("Done!").response(true).build();
        }
        return ApiResponseDto.builder().status(String.valueOf(HttpStatus.NOT_FOUND)).message("Not found!").response(false).build();
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
            giaoCa.setTienMatTrongCa(BigDecimal.valueOf(0));
            giaoCa.setTienChuyenKhoanTrongCa(BigDecimal.valueOf(0));
            giaoCa.setTienMatCaTruoc(BigDecimal.valueOf(requestBody.getTienMatDauCa() + requestBody.getTienChuyenKhoanDauCa()+requestBody.getTienMatCaTruoc()));
            giaoCaRepository.save(giaoCa);
            return true;
        }
        return false;
    }

    @Override
    public ApiResponseDto<?> checkGC(HttpServletRequest request) {
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            NhanVien nv = nhanVienReposity.findByUsername(username);
            GiaoCa giaoCa = giaoCaRepository.findDataLast();
            if (giaoCa != null) {
                if (giaoCa.getNhanVien().equals(nv) && giaoCa.getTrangThai()) {
                    return ApiResponseDto.builder().status(String.valueOf(ResponseStatus.CONTINUE_WORKING)).message("Staff on shift").response(modelMapper.map(giaoCa, GiaoCaDTO.class)).build();
                }
                if (!giaoCa.getNhanVien().equals(nv) && giaoCa.getTrangThai()) {
                    return ApiResponseDto.builder().status(String.valueOf(ResponseStatus.OTHER_STAFF_ON_SHIFT)).message("Other staff on shift").response(modelMapper.map(giaoCa, GiaoCaDTO.class)).build();
                }
                return ApiResponseDto.builder().status(String.valueOf(ResponseStatus.START_WORKING)).message("Start working").response(modelMapper.map(giaoCa, GiaoCaDTO.class)).build();
            }
            return ApiResponseDto.builder().status(String.valueOf(ResponseStatus.START_WORKING)).message("Start working").response(null).build();
        }
        return ApiResponseDto.builder().status(String.valueOf(ResponseStatus.FAIL)).message("NOT FOUND").response(false).build();
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

    private List<BanGiaoCaResponse> banGiaoCaResponse(Integer id) {
        return chiTietHinhThucThanhToanRepository.getHinhThucThanhToanTongTien(id);
    }
}
