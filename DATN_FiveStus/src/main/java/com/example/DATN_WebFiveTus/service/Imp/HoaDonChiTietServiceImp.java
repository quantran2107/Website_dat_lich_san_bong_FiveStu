package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.config.PDFGenerator;
import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.entity.SanCa;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.HoaDonChiTietRepository;
import com.example.DATN_WebFiveTus.repository.HoaDonRepository;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
import com.example.DATN_WebFiveTus.repository.SanCaRepository;
import com.example.DATN_WebFiveTus.service.HoaDonChiTietService;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import java.io.ByteArrayOutputStream;
@Service
public class HoaDonChiTietServiceImp implements HoaDonChiTietService {

    private HoaDonChiTietRepository hoaDonChiTietRepository;

    private HoaDonRepository hoaDonRepository;

    private KhachHangRepository khachHangRepository;

    private SanCaRepository sanCaRepository;

    private ModelMapper modelMapper;

    @Autowired
    private PDFGenerator pdfGenerator;

    @Autowired
    private JavaMailSender javaMailSender; // Để gửi email

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    public HoaDonChiTietServiceImp(HoaDonChiTietRepository hoaDonChiTietRepository, HoaDonRepository hoaDonRepository, SanCaRepository sanCaRepository, ModelMapper modelMapper) {
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.sanCaRepository = sanCaRepository;
        this.khachHangRepository = khachHangRepository;
        this.modelMapper = modelMapper;
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
        hoaDonChiTiet.setNgayDenSan(hoaDonChiTietDTO.getNgayDenSan());
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
        return null;
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
    public Page<HoaDonChiTietDTO> getHoaDonChiTietByTrangThai(String trangThai, String soDienThoaiKhachHang, Pageable pageable) {
        Page<HoaDonChiTiet> page = hoaDonChiTietRepository.findByTrangThai(trangThai, soDienThoaiKhachHang, pageable);
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

    public List<HoaDonChiTietDTO> findByNgayDenSan(Date ngayDenSan) {
        // Lấy danh sách HoaDonChiTiet từ repository
        List<HoaDonChiTiet> list = hoaDonChiTietRepository.findByNgayDenSan(ngayDenSan);

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

        HoaDon hoaDon = hoaDonRepository.findById(hoaDonChiTietDTO.getIdHoaDon()).orElseThrow();

        hoaDonChiTiet.setMaHoaDonChiTiet(generateMaHoaDonChiTiet());
        hoaDonChiTiet.setSanCa(sanCa);
        hoaDonChiTiet.setHoaDon(hoaDon);
        hoaDonChiTiet.setNgayDenSan(hoaDonChiTietDTO.getNgayDenSan());
        hoaDonChiTiet.setTrangThai("Chờ nhận sân");
        hoaDonChiTiet.setKieuNgayDat("Theo ngày");
        hoaDonChiTiet.setTongTien(hoaDonChiTietDTO.getTongTien());

        HoaDonChiTiet hoaDonChiTietSave = hoaDonChiTietRepository.save(hoaDonChiTiet);

        System.out.println("idSanCa: " + hoaDonChiTietDTO.getIdSanCa());
        System.out.println("idHoaDon: " + hoaDonChiTietDTO.getIdHoaDon());
        System.out.println("tongTien: " + hoaDonChiTietDTO.getTongTien());

        return modelMapper.map(hoaDonChiTietSave, HoaDonChiTietDTO.class);
    }

    @Override
    public boolean isSanCaBooked(Long idSanCa, LocalDate ngayDenSan) {
        Long count = hoaDonChiTietRepository.countByIdSanCaAndNgayDenSan(idSanCa, ngayDenSan);
        return count > 0;  // Nếu count > 0 tức là sân ca đã được đặt
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

}
