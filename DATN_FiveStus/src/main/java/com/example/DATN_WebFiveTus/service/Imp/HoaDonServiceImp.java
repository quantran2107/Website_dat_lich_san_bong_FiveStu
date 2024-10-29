package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.*;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.HoaDonChiTietRepository;
import com.example.DATN_WebFiveTus.repository.HoaDonRepository;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.repository.PhieuGiamGiaRepository;
import com.example.DATN_WebFiveTus.service.HoaDonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HoaDonServiceImp implements HoaDonService {

    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    private NhanVienReposity nhanVienReposity;

    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    private KhachHangRepository khachHangRepository;

    private ModelMapper modelMapper;
    @Autowired
    private HoaDonService hoaDonService;

    @Autowired
    public HoaDonServiceImp(HoaDonRepository hoaDonRepository, NhanVienReposity nhanVienReposity, PhieuGiamGiaRepository phieuGiamGiaRepository, KhachHangRepository khachHangRepository, ModelMapper modelMapper) {
        this.hoaDonRepository = hoaDonRepository;
        this.nhanVienReposity = nhanVienReposity;
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.khachHangRepository = khachHangRepository;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<HoaDonDTO> getAll() {
        return hoaDonRepository.findAll().stream()
                .map((hoaDon) -> modelMapper.map(hoaDon, HoaDonDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<HoaDonDTO> getAllJoinFetch() {
        return hoaDonRepository.getAllJoinFetch().stream()
                .map((hoaDon) -> modelMapper.map(hoaDon, HoaDonDTO.class)).collect(Collectors.toList());
    }

    @Override
    public HoaDonDTO getOne(Integer id) {
        return modelMapper.map(hoaDonRepository.findById(id).orElseThrow(() ->
                new ResourceNotfound("Không tồn tại id: " + id)), HoaDonDTO.class);
    }

    @Override
    public HoaDonDTO save(HoaDonDTO hoaDonDTO) {

        // Tìm khách hàng theo ID
        KhachHang khachHang = khachHangRepository.findById(hoaDonDTO.getIdKhachHang())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại với ID: " + hoaDonDTO.getIdKhachHang()));
        HoaDon hoaDon = modelMapper.map(hoaDonDTO, HoaDon.class);
        hoaDon.setMaHoaDon(generateMaHoaDon());
        hoaDon.setTrangThai("Chờ thanh toán");
        Date now = Date.from(Instant.now());
        hoaDon.setKhachHang(khachHang);
        hoaDon.setTongTienSan(hoaDonDTO.getTongTienSan());
        hoaDon.setNgayTao(now);
        hoaDon.setDeletedAt(false);
        HoaDon hoaDonSave = hoaDonRepository.save(hoaDon);
        return modelMapper.map(hoaDonSave, HoaDonDTO.class);
    }

    @Override
    public void updateThanhToan(Integer idHoaDonChiTiet) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(idHoaDonChiTiet).orElseThrow();
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonChiTiet.getHoaDon().getId())
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại với ID: " + hoaDonChiTiet.getHoaDon().getId()));
        // Kiểm tra xem HoaDon có tồn tại hay không
        if (hoaDon != null) {
//            // Thực hiện các thao tác như tính toán tiền sân, tiền cọc...
//            float tienSan = Float.parseFloat(hoaDonChiTiet.getTienSan());
//            float tienCoc = tienSan * 0.5f;
//            float tienConLai = hoaDon.getTongTien() - tienCoc;
//
//            if (tienConLai > 0) {
//                hoaDon.setTrangThai("Đã hoàn thành 1 phần");
//            } else {
//                hoaDon.setTrangThai("Đã hoàn thành");
//                tienConLai = 0;
//            }
//
//            hoaDon.setTienCoc(tienCoc);
//            hoaDon.setTienConLai(tienConLai);
//
//            // Lưu hóa đơn đã cập nhật vào cơ sở dữ liệu
//            hoaDonRepository.save(hoaDon);

        }
    }


    private String generateMaHoaDon() {
        String PREFIX = "HD";
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int RANDOM_PART_LENGTH = 8; // Độ dài của phần ngẫu nhiên, để tổng độ dài là 10
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder(PREFIX);
        for (int i = 0; i < RANDOM_PART_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public HoaDonDTO update(Integer id, HoaDonDTO hoaDonDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void deletedAt(Integer id) {

    }

    @Override
    public List<HoaDonDTO> getHDforNV(int id) {
        LocalDate today = LocalDate.now();
        List<HoaDon> list = hoaDonRepository.findByIdNV(id,today);
        if (!list.isEmpty()){
            return hoaDonRepository.findByIdNV(id,today).stream().map(hoaDon -> modelMapper.map(hoaDon,HoaDonDTO.class)).toList();
        }
        return null;
    }


//    @Override
//    public Page<HoaDonDTO> searchAndFilter(@Param("loai") Boolean loai,
//                                           @Param("trangThai") String trangThai,
//                                           @Param("keyword") String keyword,
//                                           @Param("tongTienMin") Float tongTienMin,
//                                           @Param("tongTienMax") Float tongTienMax,
//                                           Pageable pageable) {
//        List<HoaDon> hoaDonList = hoaDonRepository.searchAndFilter(loai, trangThai, keyword, tongTienMin, tongTienMax);
//
//        // Phân trang thủ công
//        int pageSize = pageable.getPageSize();
//        int currentPage = pageable.getPageNumber();
//        int startItem = currentPage * pageSize;
//        List<HoaDon> list;
//
//        if (hoaDonList.size() < startItem) {
//            list = Collections.emptyList();
//        } else {
//            int toIndex = Math.min(startItem + pageSize, hoaDonList.size());
//            list = hoaDonList.subList(startItem, toIndex);
//        }
//
//        List<HoaDonDTO> hoaDonDTOList = list.stream()
//                .map(hoaDon -> modelMapper.map(hoaDon, HoaDonDTO.class))
//                .collect(Collectors.toList());
//
//        return new PageImpl<>(hoaDonDTOList, pageable, hoaDonList.size());
//    }

    @Override
    public Page<HoaDonDTO> phanTrang(Pageable pageable) {
        List<HoaDon> hoaDonList = hoaDonRepository.getAllJoinFetch();

        // Phân trang thủ công
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<HoaDon> list;

        if (hoaDonList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, hoaDonList.size());
            list = hoaDonList.subList(startItem, toIndex);
        }

        List<HoaDonDTO> hoaDonDTOList = list.stream()
                .map(hoaDon -> modelMapper.map(hoaDon, HoaDonDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(hoaDonDTOList, pageable, hoaDonList.size());
    }


}
