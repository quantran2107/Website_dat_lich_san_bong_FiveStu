package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.entity.SanCa;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.*;
import com.example.DATN_WebFiveTus.service.HoaDonChiTietService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HoaDonChiTietServiceImp implements HoaDonChiTietService {

    private HoaDonChiTietRepository hoaDonChiTietRepository;

    private HoaDonRepository hoaDonRepository;

    private KhachHangRepository khachHangRepository;
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Autowired
    private NhanVienReposity nhanVienReposity;

    private SanCaRepository sanCaRepository;

    private ModelMapper modelMapper;

    @Autowired
    private JavaMailSender javaMailSender; // Để gửi email

    @Autowired
    private SpringTemplateEngine springTemplateEngine;


    @Autowired
    public HoaDonChiTietServiceImp(HoaDonChiTietRepository hoaDonChiTietRepository, HoaDonRepository hoaDonRepository, SanCaRepository sanCaRepository, ModelMapper modelMapper, PhieuGiamGiaRepository phieuGiamGiaRepository) {
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.sanCaRepository = sanCaRepository;
        this.khachHangRepository = khachHangRepository;
        this.nhanVienReposity = nhanVienReposity;
        this.modelMapper = modelMapper;
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
    }

    @Override
    public List<HoaDonChiTietDTO> getAll() {
        return hoaDonChiTietRepository.findAll().stream()
                .map((hoaDonChiTiet) -> modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<HoaDonChiTietDTO> getAllJoinFetch() {
        return hoaDonChiTietRepository.getAllJoinFetch().stream()
                .map((hoaDonChiTiet) -> modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class)).collect(Collectors.toList());
    }

    @Override
    public HoaDonChiTietDTO getOne(Integer id) {
        return modelMapper.map(hoaDonChiTietRepository.findById(id).orElseThrow(() ->
                new ResourceNotfound("Không tồn tại id: " + id)), HoaDonChiTietDTO.class);
    }

    @Override
    public HoaDonChiTietDTO save(HoaDonChiTietDTO hoaDonChiTietDTO) {
        HoaDonChiTiet hoaDonChiTiet = modelMapper.map(hoaDonChiTietDTO, HoaDonChiTiet.class);

        SanCa sanCa = sanCaRepository.findById(hoaDonChiTietDTO.getIdSanCa()).orElseThrow();

        HoaDon hoaDon = hoaDonRepository.findById(hoaDonChiTietDTO.getIdHoaDon()).orElseThrow();

        hoaDonChiTiet.setMaHoaDonChiTiet(generateMaHoaDonChiTiet());
        hoaDonChiTiet.setSanCa(sanCa);
        hoaDonChiTiet.setHoaDon(hoaDon);
        hoaDonChiTiet.setNgayDenSan((Date) hoaDonChiTietDTO.getNgayDenSan());
        hoaDonChiTiet.setTrangThai("Chờ nhận sân");

        hoaDonChiTiet.setKieuNgayDat(hoaDonChiTietDTO.getKieuNgayDat());
        hoaDonChiTiet.setDeletedAt(false);

        HoaDonChiTiet hoaDonChiTietSave = hoaDonChiTietRepository.save(hoaDonChiTiet);

        return modelMapper.map(hoaDonChiTietSave, HoaDonChiTietDTO.class);
    }

    private String generateMaHoaDonChiTiet() {
        String PREFIX = "HDCT";
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int RANDOM_PART_LENGTH = 6; // Độ dài của phần ngẫu nhiên, để tổng độ dài là 10
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder(PREFIX);
        for (int i = 0; i < RANDOM_PART_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public HoaDonChiTietDTO update(Integer id, HoaDonChiTietDTO hoaDonChiTietDTO) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hóa đơn chi tiết không tồn tại với ID: " + id));

        NhanVien nhanVien = nhanVienReposity.findById(hoaDonChiTietDTO.getIdNhanVien())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại với ID: " + hoaDonChiTietDTO.getIdNhanVien()));

        hoaDonChiTiet.setNhanVien(nhanVien);
        hoaDonChiTiet.setTongTien(hoaDonChiTietDTO.getTongTien());
        hoaDonChiTiet.setTienGiamGia(hoaDonChiTietDTO.getTienGiamGia());
        hoaDonChiTiet.setTongTienThucTe(hoaDonChiTietDTO.getTongTienThucTe());
        hoaDonChiTiet.setTrangThai("Đã thanh toán");

        HoaDonChiTiet hoaDonChiTietSave = hoaDonChiTietRepository.save(hoaDonChiTiet);

        return modelMapper.map(hoaDonChiTietSave, HoaDonChiTietDTO.class);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void deletedAt(Integer id) {

    }

    @Override
    public List<HoaDonChiTietDTO> searchFromHoaDon(Integer idHoaDon) {
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.searchFromHoaDon(idHoaDon);
        List<HoaDonChiTietDTO> dtoList = new ArrayList<>();

        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTietList) {
            HoaDonChiTietDTO dto = modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class);

            // Thêm các trường tùy chỉnh vào DTO
            dto.setMaHoaDon(hoaDonChiTiet.getHoaDon().getMaHoaDon()); // Lấy maHoaDon từ đối tượng hoaDon
            dto.setMaHoaDonChiTiet(hoaDonChiTiet.getMaHoaDonChiTiet()); // Lấy maHoaDonChiTiet

            dtoList.add(dto);
        }

        return dtoList;
    }


    @Override
    public Page<HoaDonChiTietDTO> getHoaDonChiTietByTrangThai(String trangThai, String keyWord, Pageable pageable) {
        Page<HoaDonChiTiet> page = hoaDonChiTietRepository.findByTrangThai(trangThai, keyWord, pageable);
        List<HoaDonChiTietDTO> list = page.getContent().stream()
                .map(hoaDonChiTiet -> {
                    HoaDonChiTietDTO dto = modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class);

                    // Lấy thông tin khách hàng từ hóa đơn
                    HoaDon hoaDon = hoaDonRepository.findByIdWithKhachHang(hoaDonChiTiet.getHoaDon().getId());
                    if (hoaDon != null && hoaDon.getKhachHang() != null) {
                        dto.setIdKhachHang(hoaDon.getKhachHang().getId());
                        dto.setHoVaTenKhachHang(hoaDon.getKhachHang().getHoVaTen());
                        dto.setSoDienThoaiKhachHang(hoaDon.getKhachHang().getSoDienThoai());
                        dto.setEmailKhachHang(hoaDon.getKhachHang().getEmail());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }


    @Override
    public HoaDonChiTietDTO getOneHDCT(Integer id) {
        // Tìm HoaDonChiTiet từ repository
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findHoaDonChiTietById(id);


        // Map dữ liệu từ entity sang DTO
        HoaDonChiTietDTO dto = modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class);

        // Set thêm các thông tin cần thiết vào DTO
        dto.setIdHoaDon(hoaDonChiTiet.getHoaDon().getId());
        dto.setMaHoaDon(hoaDonChiTiet.getHoaDon().getMaHoaDon());
        dto.setIdSanCa(hoaDonChiTiet.getSanCa().getId());
        dto.setNgayTaoHoaDon(hoaDonChiTiet.getHoaDon().getNgayTao());
        dto.setNgayDenSan(hoaDonChiTiet.getNgayDenSan());
        dto.setTongTien(hoaDonChiTiet.getTongTien());
        dto.setTienCocHdct(hoaDonChiTiet.getTienCocHdct());
        dto.setTienGiamGia(hoaDonChiTiet.getTienGiamGia());
        dto.setTongTienThucTe(hoaDonChiTiet.getTongTienThucTe());
        dto.setGhiChu(hoaDonChiTiet.getGhiChu());
        dto.setTrangThai(hoaDonChiTiet.getTrangThai());
        dto.setTenSanBong(hoaDonChiTiet.getSanCa().getSanBong().getTenSanBong());
        dto.setTenCa(hoaDonChiTiet.getSanCa().getCa().getTenCa());
        dto.setThoiGianBatDauCa(hoaDonChiTiet.getSanCa().getCa().getThoiGianBatDau());
        dto.setThoiGianKetThucCa(hoaDonChiTiet.getSanCa().getCa().getThoiGianKetThuc());
        dto.setIdKhachHang(hoaDonChiTiet.getHoaDon().getKhachHang().getId());
        dto.setHoVaTenKhachHang(hoaDonChiTiet.getHoaDon().getKhachHang().getHoVaTen());
        dto.setSoDienThoaiKhachHang(hoaDonChiTiet.getHoaDon().getKhachHang().getSoDienThoai());
        dto.setEmailKhachHang(hoaDonChiTiet.getHoaDon().getKhachHang().getEmail());

        return dto;
    }

    @Override
    public void updateTrangThai(Integer id) {
        hoaDonChiTietRepository.updateTrangThai(id);
    }

    @Override
    public void updateTrangThaiThanhToan(Integer id) {
        hoaDonChiTietRepository.updateTrangThaiThanhToan(id);
    }

    @Override
    public void updateTrangThaiHuy(Integer id) {
        hoaDonChiTietRepository.updateTrangThaiHuy(id);
    }

    public List<HoaDonChiTietDTO> findByNgayDenSan(java.util.Date ngayDenSan) {
        // Lấy danh sách HoaDonChiTiet từ repository
        List<HoaDonChiTiet> list = hoaDonChiTietRepository.findByNgayDenSan((java.sql.Date) ngayDenSan);

        // Ánh xạ và bổ sung thông tin cho DTO
        List<HoaDonChiTietDTO> dtoList = list.stream()
                .map(hoaDonChiTiet -> {
                    // Ánh xạ từ HoaDonChiTiet sang HoaDonChiTietDTO
                    HoaDonChiTietDTO dto = modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class);

                    // Lấy thông tin hóa đơn từ đối tượng HoaDon
                    HoaDon hoaDon = hoaDonChiTiet.getHoaDon();
                    if (hoaDon != null) {
                        dto.setMaHoaDon(hoaDon.getMaHoaDon());
                        if (hoaDon.getKhachHang() != null) {
                            dto.setIdKhachHang(hoaDon.getKhachHang().getId());
                            dto.setHoVaTenKhachHang(hoaDon.getKhachHang().getHoVaTen());
                            dto.setSoDienThoaiKhachHang(hoaDon.getKhachHang().getSoDienThoai());
                            dto.setEmailKhachHang(hoaDon.getKhachHang().getEmail());
                        }
                    }

                    return dto;
                })
                .collect(Collectors.toMap(
                        HoaDonChiTietDTO::getMaHoaDon, // key
                        dto -> dto, // value
                        (existing, replacement) -> existing)) // Resolve conflicts: keep existing
                .values()
                .stream()
                .collect(Collectors.toList());

        return dtoList;
    }

    @Override
    public HoaDonChiTietDTO save2(HoaDonChiTietDTO hoaDonChiTietDTO) {
        HoaDonChiTiet hoaDonChiTiet = modelMapper.map(hoaDonChiTietDTO, HoaDonChiTiet.class);
        SanCa sanCa = sanCaRepository.findById(hoaDonChiTietDTO.getIdSanCa()).orElseThrow();
        // Khởi tạo đối tượng NhanVien
        NhanVien nhanVien = null;

        // Kiểm tra điều kiện trước khi tìm nhân viên

        HoaDon hoaDon = hoaDonRepository.findById(hoaDonChiTietDTO.getIdHoaDon()).orElseThrow();
        hoaDonChiTiet.setMaHoaDonChiTiet(generateMaHoaDonChiTiet());
        // Thiết lập trạng thái dựa trên idNhanVien

        hoaDonChiTiet.setSanCa(sanCa);
        hoaDonChiTiet.setHoaDon(hoaDon);
        hoaDonChiTiet.setNgayDenSan(new java.util.Date(hoaDonChiTietDTO.getNgayDenSan().getTime()));
        hoaDonChiTiet.setKieuNgayDat(hoaDonChiTietDTO.getKieuNgayDat());
        hoaDonChiTiet.setTongTien(hoaDonChiTietDTO.getTongTien());
        hoaDonChiTiet.setTienCocHdct(hoaDonChiTietDTO.getTienCocHdct());
        hoaDonChiTiet.setDeletedAt(false);
        hoaDonChiTiet.setTienGiamGia((double) 0);
        if (hoaDon.getNhanVien() != null) {
            nhanVien = nhanVienReposity.findById(hoaDon.getNhanVien().getId())
                    .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại với ID: " + hoaDonChiTietDTO.getIdNhanVien()));
            hoaDonChiTiet.setTrangThai("Chờ nhận sân");
            hoaDonChiTiet.setTongTienThucTe(hoaDonChiTietDTO.getTongTien());
        } else {
            hoaDonChiTiet.setTrangThai("Chờ đặt cọc");
        }

        HoaDonChiTiet hoaDonChiTietSave = hoaDonChiTietRepository.save(hoaDonChiTiet);
        return modelMapper.map(hoaDonChiTietSave, HoaDonChiTietDTO.class);
    }

    @Override
    public boolean isSanCaBooked(Long idSanCa, LocalDate ngayDenSan) {
        Long count = hoaDonChiTietRepository.countByIdSanCaAndNgayDenSan(idSanCa, ngayDenSan);
        return count > 0;  // Nếu count > 0 tức là sân ca đã được đặt
    }

    @Override
    public void updateTrangThaiHoaDonChiTiet(Integer idHoaDonChiTiet, String trangThai) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(idHoaDonChiTiet)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn chi tiết với id " + idHoaDonChiTiet));
        hoaDonChiTiet.setTrangThai(trangThai);
        hoaDonChiTietRepository.save(hoaDonChiTiet);
    }

    @Override
    public List<HoaDonChiTietDTO> findByIdKhachHang(Integer id) {
        List<HoaDonChiTiet> list = hoaDonChiTietRepository.findHoaDonChiTietByIdKhachHang(id);
        return list.stream()
                .map((hoaDonChiTiet) -> modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Boolean huyDatSan(Integer id) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id).orElseThrow(null);
        if (hoaDonChiTiet != null) {
            hoaDonChiTiet.setDeletedAt(true);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
            return true;
        }
        return false;
    }

    public List<HoaDonChiTietDTO> findByNgayDenSanBetween(LocalDate startDate, LocalDate endDate) {
        // Lấy danh sách HoaDonChiTiet từ repository
        List<HoaDonChiTiet> list = hoaDonChiTietRepository.findByNgayDenSanBetween(startDate, endDate);

        // Ánh xạ và bổ sung thông tin cho DTO
        List<HoaDonChiTietDTO> dtoList = list.stream()
                .map(hoaDonChiTiet -> {
                    // Ánh xạ từ HoaDonChiTiet sang HoaDonChiTietDTO
                    HoaDonChiTietDTO dto = modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class);

                    // Lấy thông tin hóa đơn từ đối tượng HoaDon
                    HoaDon hoaDon = hoaDonChiTiet.getHoaDon();
                    if (hoaDon != null) {
                        dto.setMaHoaDon(hoaDon.getMaHoaDon());
                        if (hoaDon.getKhachHang() != null) {
                            dto.setIdKhachHang(hoaDon.getKhachHang().getId());
                            dto.setHoVaTenKhachHang(hoaDon.getKhachHang().getHoVaTen());
                            dto.setSoDienThoaiKhachHang(hoaDon.getKhachHang().getSoDienThoai());
                            dto.setEmailKhachHang(hoaDon.getKhachHang().getEmail());
                        }
                    }

                    return dto;
                })
                .collect(Collectors.toMap(
                        HoaDonChiTietDTO::getId, // key
                        dto -> dto, // value
                        (existing, replacement) -> existing)) // Resolve conflicts: keep existing
                .values()
                .stream()
                .collect(Collectors.toList());

        return dtoList;

    }

    @Override
    public HoaDonChiTietDTO huyLichDat(Integer id) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với id " + id));
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonChiTiet.getHoaDon().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với id " + hoaDonChiTiet.getHoaDon().getId()));

        hoaDonChiTiet.setTrangThai("Đã hủy");
        hoaDonChiTiet.setHoaDon(hoaDon);

        // Lấy danh sách tất cả hóa đơn chi tiết của hóa đơn
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.searchFromHoaDon(hoaDon.getId());

        // Kiểm tra trạng thái của tất cả hóa đơn chi tiết
        boolean allCancelled = true; // Kiểm tra tất cả là "Đã hủy"
        boolean allCompletedOrCancelled = true; // Kiểm tra tất cả là "Đã thanh toán", "Đã hủy", hoặc "Đã hoàn tiền cọc"

        for (HoaDonChiTiet chiTiet : hoaDonChiTietList) {
            String trangThai = chiTiet.getTrangThai();
            if (!"Đã thanh toán".equals(trangThai) && !"Đã hủy".equals(trangThai) && !"Đã hoàn tiền cọc".equals(trangThai)) {
                allCompletedOrCancelled = false;
            }
            if (!"Đã hủy".equals(trangThai)) {
                allCancelled = false;
            }
        }

        // Cập nhật trạng thái hóa đơn
        if (allCancelled) {
            hoaDon.setTrangThai("Đã hủy");
        } else if (allCompletedOrCancelled) {
            hoaDon.setTrangThai("Đã hoàn thành");
        } else {
            hoaDon.setTrangThai("Đã hoàn thành một phần");
        }

        // Lưu lại hóa đơn và hóa đơn chi tiết
        hoaDonRepository.save(hoaDon);
        hoaDonChiTietRepository.save(hoaDonChiTiet);

        return modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class);
    }


    @Override
    public HoaDonChiTietDTO hoanTienCoc(Integer id) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với id " + id));
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonChiTiet.getHoaDon().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với id " + hoaDonChiTiet.getHoaDon().getId()));

        hoaDonChiTiet.setTrangThai("Đã hoàn tiền cọc");
        hoaDonChiTiet.setHoaDon(hoaDon);

        // Lấy danh sách tất cả hóa đơn chi tiết của hóa đơn
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.searchFromHoaDon(hoaDon.getId());

        // Kiểm tra trạng thái của tất cả hóa đơn chi tiết
        boolean allCancelled = true; // Kiểm tra tất cả là "Đã hủy"
        boolean allCompletedOrCancelled = true; // Kiểm tra tất cả là "Đã thanh toán", "Đã hủy", hoặc "Đã hoàn tiền cọc"

        for (HoaDonChiTiet chiTiet : hoaDonChiTietList) {
            String trangThai = chiTiet.getTrangThai();
            if (!"Đã thanh toán".equals(trangThai) && !"Đã hủy".equals(trangThai) && !"Đã hoàn tiền cọc".equals(trangThai)) {
                allCompletedOrCancelled = false;
            }
            if (!"Đã hủy".equals(trangThai)) {
                allCancelled = false;
            }
        }

        // Cập nhật trạng thái hóa đơn
        if (allCancelled) {
            hoaDon.setTrangThai("Đã hủy");
        } else if (allCompletedOrCancelled) {
            hoaDon.setTrangThai("Đã hoàn thành");
        } else {
            hoaDon.setTrangThai("Đã hoàn thành một phần");
        }

        // Lưu lại hóa đơn và hóa đơn chi tiết
        hoaDonRepository.save(hoaDon);
        hoaDonChiTietRepository.save(hoaDonChiTiet);

        return modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class);
    }


    @Override
    public HoaDonChiTietDTO thanhToan(Integer id, HoaDonChiTietDTO hoaDonChiTietDTO) {
        // Lấy thông tin hóa đơn chi tiết hiện tại
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hóa đơn chi tiết không tồn tại với ID: " + id));

        SanCa sanCa = sanCaRepository.findById(hoaDonChiTietDTO.getIdSanCa()).orElseThrow();
        NhanVien nhanVien = nhanVienReposity.findById(hoaDonChiTietDTO.getIdNhanVien()).orElseThrow();
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonChiTiet.getHoaDon().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hóa đơn với id " + hoaDonChiTiet.getHoaDon().getId()));


        // Cập nhật trạng thái của hóa đơn chi tiết hiện tại
        hoaDonChiTiet.setSanCa(sanCa);
        hoaDonChiTiet.setHoaDon(hoaDon);
        hoaDonChiTiet.setNhanVien(nhanVien);
        hoaDonChiTiet.setTongTien(hoaDonChiTietDTO.getTongTien());
        hoaDonChiTiet.setTienCocHdct(hoaDonChiTietDTO.getTienCocHdct());
        if (hoaDonChiTietDTO.getIdphieuGiamGia() != null) {
            hoaDonChiTiet.setPhieuGiamGia(
                    phieuGiamGiaRepository.findById(hoaDonChiTietDTO.getIdphieuGiamGia())
                            .orElseThrow(() -> new RuntimeException("Phiếu giảm giá không tồn tại với ID: " + hoaDonChiTietDTO.getIdphieuGiamGia()))
            );
            hoaDonChiTiet.setTienGiamGia(hoaDonChiTietDTO.getTienGiamGia());
        } else {
            hoaDonChiTiet.setPhieuGiamGia(null);
            hoaDonChiTiet.setTienGiamGia((double) 0);
        }

        hoaDonChiTiet.setTongTienThucTe(hoaDonChiTietDTO.getTongTienThucTe());
        hoaDonChiTiet.setTrangThai("Đã thanh toán");

        // Lưu hóa đơn chi tiết đã cập nhật
        HoaDonChiTiet hoaDonChiTietSave = hoaDonChiTietRepository.save(hoaDonChiTiet);

        // Kiểm tra tất cả các hóa đơn chi tiết của hóa đơn này
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.searchFromHoaDon(hoaDon.getId());
        boolean allCompletedOrCancelled = true;

        // Kiểm tra trạng thái của các hóa đơn chi tiết
        for (HoaDonChiTiet chiTiet : hoaDonChiTietList) {
            if (!"Đã thanh toán".equals(chiTiet.getTrangThai()) && !"Đã hủy".equals(chiTiet.getTrangThai())&& !"Đã hoàn tiền cọc".equals(chiTiet.getTrangThai())) {
                allCompletedOrCancelled = false;
                break;
            }
        }

        // Cập nhật trạng thái của hóa đơn
        if (allCompletedOrCancelled) {
            hoaDon.setTrangThai("Đã hoàn thành");
        } else {
            hoaDon.setTrangThai("Đã hoàn thành một phần");
        }

        // Lưu lại hóa đơn với trạng thái cập nhật
        hoaDonRepository.save(hoaDon);

        // Trả về đối tượng DTO sau khi đã lưu
        return modelMapper.map(hoaDonChiTietSave, HoaDonChiTietDTO.class);
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 39 2 * * ?", zone = "Asia/Ho_Chi_Minh")// Chạy mỗi phút
    public void sendDailyReminders() {
        ZonedDateTime nowInHCM = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
        LocalDate tomorrow = nowInHCM.plusDays(1).toLocalDate();
//        log.info("Ngày cần nhắc nhở (GMT+7): " + tomorrow);

//        System.out.println("Ngày cần check " + tomorrow);
        // Lấy danh sách hóa đơn chi tiết cần nhắc nhở
        List<HoaDonChiTiet> reminders = hoaDonChiTietRepository.findRemindersForTomorrow(tomorrow);

//        log.info("Số lượng hóa đơn cần nhắc nhở: " + reminders.size());

        for (HoaDonChiTiet hoaDonChiTiet : reminders) {
            try {
//                log.info("Đang gửi email cho khách hàng: " + hoaDonChiTiet.getHoaDon().getKhachHang().getEmail());
                sendReminderEmail(hoaDonChiTiet);
            } catch (MessagingException e) {
                log.error("Lỗi khi gửi email: ", e);
            }
        }
    }

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private void sendReminderEmail(HoaDonChiTiet hoaDonChiTiet) throws MessagingException {
        // Tạo nội dung email từ Thymeleaf
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormatter.format(hoaDonChiTiet.getNgayDenSan());
        String thoiGianBatDau = hoaDonChiTiet.getSanCa().getCa().getThoiGianBatDau().format(TIME_FORMATTER);
        String thoiGianKetThuc = hoaDonChiTiet.getSanCa().getCa().getThoiGianKetThuc().format(TIME_FORMATTER);
        Context context = new Context();
        context.setVariable("tenKhachHang", hoaDonChiTiet.getHoaDon().getKhachHang().getHoVaTen());
        context.setVariable("tenSanBong", hoaDonChiTiet.getSanCa().getSanBong().getTenSanBong());
        context.setVariable("ngayDenSan", formattedDate);
        context.setVariable("thoiGianBatDau",thoiGianBatDau);
        context.setVariable("thoiGianKetThuc", thoiGianKetThuc);

        String htmlContent = springTemplateEngine.process("nhac-lich", context);

        // Gửi email
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(hoaDonChiTiet.getHoaDon().getKhachHang().getEmail());
        helper.setSubject("Nhắc Nhở: Bạn Sắp Có Lịch Đá Bóng");
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }
}
