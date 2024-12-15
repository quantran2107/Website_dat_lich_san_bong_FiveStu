package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.config.VNPay.EmailService;
import com.example.DATN_WebFiveTus.config.security.CookieUtils;
import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import com.example.DATN_WebFiveTus.dto.*;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.*;
import com.example.DATN_WebFiveTus.service.HoaDonService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HoaDonServiceImp implements HoaDonService {

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private NhanVienReposity nhanVienReposity;

    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    private KhachHangRepository khachHangRepository;

    private ModelMapper modelMapper;

    private final EmailService emailService;

    private final HoaDonRepository hoaDonRepository;

    @Autowired
    public HoaDonServiceImp(NhanVienReposity nhanVienReposity,
                            PhieuGiamGiaRepository phieuGiamGiaRepository, KhachHangRepository khachHangRepository,
                            HoaDonRepository hoaDonRepository, EmailService emailService,ModelMapper modelMapper) {
        this.nhanVienReposity = nhanVienReposity;
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.khachHangRepository = khachHangRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.emailService = emailService;
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

        // Khởi tạo đối tượng NhanVien
        NhanVien nhanVien = null;
        HoaDon hoaDon = modelMapper.map(hoaDonDTO, HoaDon.class);

        // Kiểm tra điều kiện trước khi tìm nhân viên
        if (hoaDonDTO.getIdNhanVien() != null) {
            nhanVien = nhanVienReposity.findById(hoaDonDTO.getIdNhanVien())
                    .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại với ID: " + hoaDonDTO.getIdNhanVien()));
            hoaDon.setTrangThai("Chờ thanh toán");
        }else{
            hoaDon.setTrangThai("Chờ đặt cọc");
        }

        hoaDon.setMaHoaDon(generateMaHoaDon());
        hoaDon.setId(hoaDonDTO.getId());
        // Thiết lập trạng thái dựa trên idNhanVien
        Date now = Date.from(Instant.now());
        hoaDon.setKhachHang(khachHang);
        hoaDon.setTongTienSan(hoaDonDTO.getTongTienSan());
        hoaDon.setTongTien(hoaDonDTO.getTongTien());
        hoaDon.setTienCoc(hoaDonDTO.getTienCoc());
        hoaDon.setNhanVien(nhanVien); // Nếu nhân viên null thì set null
        hoaDon.setNgayTao(now);
        hoaDon.setLoai(true);
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


    @Override
    public Page<HoaDonDTO> searchAndFilter(@Param("loai") Boolean loai,
                                           @Param("trangThai") String trangThai,
                                           @Param("keyword") String keyword,
                                           @Param("tongTienMin") Float tongTienMin,
                                           @Param("tongTienMax") Float tongTienMax,
                                           @Param("ngayTaoMin") LocalDateTime ngayTaoMin,
                                           @Param("ngayTaoMax") LocalDateTime ngayTaoMax,
                                           Pageable pageable) {
        List<HoaDon> hoaDonList = hoaDonRepository.searchAndFilter(loai, trangThai, keyword, tongTienMin, tongTienMax, ngayTaoMin, ngayTaoMax);

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
    @Override
    public void updateTrangThaiHoaDon(Integer idHoaDon, String trangThai) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với id " + idHoaDon));
        hoaDon.setTrangThai(trangThai);
        hoaDonRepository.save(hoaDon);
    }

    @Override
    public HoaDonDTO huyLichDat(Integer id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với id " + id));
        hoaDon.setTrangThai("Đã hủy");
        hoaDonRepository.save(hoaDon);
        return modelMapper.map(hoaDon,HoaDonDTO.class);
    }

    @Async
    @Override
    public void sendInvoiceEmail(HoaDonDTO hoaDonDTO, List<HoaDonChiTietDTO> hoaDonChiTietList) {
        emailService.sendInvoiceEmail(hoaDonDTO, hoaDonChiTietList);
    }

    @Override
    public NhanVienDTO getNhanVienTrongCa(HttpServletRequest request) {
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            NhanVien nv = nhanVienReposity.findByUsername(username);
            return modelMapper.map(nv,NhanVienDTO.class);
        }
        return null;
    }

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
