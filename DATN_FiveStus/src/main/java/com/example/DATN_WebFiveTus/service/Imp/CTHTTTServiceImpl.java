package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.config.security.CookieUtils;
import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import com.example.DATN_WebFiveTus.dto.ChiTietHinhThucThanhToanDTO;
import com.example.DATN_WebFiveTus.dto.HTTTDto;
import com.example.DATN_WebFiveTus.entity.ChiTietHinhThucThanhToan;
import com.example.DATN_WebFiveTus.entity.HinhThucThanhToan;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.repository.ChiTietHinhThucThanhToanRepository;
import com.example.DATN_WebFiveTus.repository.HinhThucThanhToanRepository;
import com.example.DATN_WebFiveTus.repository.HoaDonChiTietRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.service.CTHTTTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CTHTTTServiceImpl implements CTHTTTService {
    @Autowired
    private ChiTietHinhThucThanhToanRepository chiTietHinhThucThanhToanRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private NhanVienReposity nhanVienReposity;

    @Autowired
    private HinhThucThanhToanRepository hinhThucThanhToanRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<HTTTDto> getHtttById(int id) {
        List<HTTTDto> list = chiTietHinhThucThanhToanRepository.findByIdHdct(id).stream().map(HTTTDto::new).collect(Collectors.toList());
        return list;
    }

    @Override
    public Boolean addNew(HTTTDto htttDto, HttpServletRequest request) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(htttDto.getIdHD()).orElse(null);
        assert hoaDonChiTiet != null;
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            NhanVien nv = nhanVienReposity.findByUsername(username);
            if (nv != null) {
                hoaDonChiTiet.setNhanVien(nv);
                hoaDonChiTietRepository.save(hoaDonChiTiet);
            }
        }
        System.out.println(hoaDonChiTiet.getId());
        HinhThucThanhToan hinhThucThanhToan = hinhThucThanhToanRepository.findById(htttDto.getIdHttt()).get();

        ChiTietHinhThucThanhToan ctHttt = new ChiTietHinhThucThanhToan();
        ctHttt.setTrangThai("active");
        ctHttt.setHinhThucThanhToan(hinhThucThanhToan);
        ctHttt.setHoaDonChiTiet(hoaDonChiTiet);
        ctHttt.setSoTien((htttDto.getSoTien()));
        chiTietHinhThucThanhToanRepository.save(ctHttt);
        return true;
    }

    @Override
    public List<ChiTietHinhThucThanhToanDTO> findByHoaDonChiTietId(int hoaDonChiTietId) {
        List<ChiTietHinhThucThanhToan> chiTietHinhThucThanhToanList = chiTietHinhThucThanhToanRepository.findByHoaDonChiTiet_Id(hoaDonChiTietId);
        return chiTietHinhThucThanhToanList.stream()
                .map(chiTietHinhThucThanhToan -> modelMapper.map(chiTietHinhThucThanhToan, ChiTietHinhThucThanhToanDTO.class))
                .collect(Collectors.toList());
    }

}
