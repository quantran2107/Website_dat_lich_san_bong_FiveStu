package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.ChiTietHinhThucThanhToanDTO;
import com.example.DATN_WebFiveTus.dto.HTTTDto;
import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.entity.ChiTietHinhThucThanhToan;
import com.example.DATN_WebFiveTus.entity.HinhThucThanhToan;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.entity.SanCa;
import com.example.DATN_WebFiveTus.repository.ChiTietHinhThucThanhToanRepository;
import com.example.DATN_WebFiveTus.repository.HinhThucThanhToanRepository;
import com.example.DATN_WebFiveTus.repository.HoaDonChiTietRepository;
import com.example.DATN_WebFiveTus.service.CTHTTTService;
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
    ;

    @Autowired
    private HinhThucThanhToanRepository hinhThucThanhToanRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ChiTietHinhThucThanhToanDTO> findByHoaDonChiTietId(int hoaDonChiTietId) {
        List<ChiTietHinhThucThanhToan> chiTietHinhThucThanhToanList = chiTietHinhThucThanhToanRepository.findByIdHdct(hoaDonChiTietId);
        return chiTietHinhThucThanhToanList.stream()
                .map(chiTietHinhThucThanhToan -> modelMapper.map(chiTietHinhThucThanhToan, ChiTietHinhThucThanhToanDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deletedSoft(int idHinhThucThanhToan) {
        ChiTietHinhThucThanhToan chiTietHinhThucThanhToan = chiTietHinhThucThanhToanRepository.findById(idHinhThucThanhToan).orElseThrow();
        chiTietHinhThucThanhToan.setDeletedAt(true);
        ChiTietHinhThucThanhToan chiTietHinhThucThanhToanSave = chiTietHinhThucThanhToanRepository.save(chiTietHinhThucThanhToan);
    }

    @Override
    public ChiTietHinhThucThanhToanDTO save(ChiTietHinhThucThanhToanDTO chiTietHinhThucThanhToanDTO) {
        ChiTietHinhThucThanhToan chiTietHinhThucThanhToan = modelMapper.map(chiTietHinhThucThanhToanDTO, ChiTietHinhThucThanhToan.class);
        HinhThucThanhToan hinhThucThanhToan = hinhThucThanhToanRepository.findById(chiTietHinhThucThanhToanDTO.getIdHinhThucThanhToan()).orElseThrow();
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(chiTietHinhThucThanhToanDTO.getIdHoaDonChiTiet()).orElseThrow();
        chiTietHinhThucThanhToan.setHinhThucThanhToan(hinhThucThanhToan);
        chiTietHinhThucThanhToan.setHoaDonChiTiet(hoaDonChiTiet);
        chiTietHinhThucThanhToan.setSoTien(chiTietHinhThucThanhToanDTO.getSoTien());
        if(hinhThucThanhToan.getId() == 1){
            chiTietHinhThucThanhToan.setMaGiaoDich(chiTietHinhThucThanhToanDTO.getMaGiaoDich());
        }else{
            chiTietHinhThucThanhToan.setMaGiaoDich(null);
        }
        chiTietHinhThucThanhToan.setDeletedAt(false);
        ChiTietHinhThucThanhToan chiTietHTTTSave = chiTietHinhThucThanhToanRepository.save(chiTietHinhThucThanhToan);
        return modelMapper.map(chiTietHTTTSave, ChiTietHinhThucThanhToanDTO.class);
    }

}
