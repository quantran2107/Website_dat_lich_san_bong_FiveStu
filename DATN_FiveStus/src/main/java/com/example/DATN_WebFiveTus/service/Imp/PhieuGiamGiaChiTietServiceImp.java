package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.config.VNPay.EmailService;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaChiTietDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGiaChiTiet;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
import com.example.DATN_WebFiveTus.repository.PhieuGiamGiaChiTietRepository;
import com.example.DATN_WebFiveTus.repository.PhieuGiamGiaRepository;
import com.example.DATN_WebFiveTus.service.PhieuGiamGiaChiTietService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaChiTietServiceImp implements PhieuGiamGiaChiTietService {

    private PhieuGiamGiaChiTietRepository phieuGiamGiaChiTietRepository;
    private ModelMapper modelMapper;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Autowired
    public PhieuGiamGiaChiTietServiceImp(PhieuGiamGiaChiTietRepository phieuGiamGiaChiTietRepository, ModelMapper modelMapper) {
        this.phieuGiamGiaChiTietRepository = phieuGiamGiaChiTietRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PhieuGiamGiaChiTietDTO> getAll() {
        List<PhieuGiamGiaChiTiet> phieuGiamGiaChiTietList = phieuGiamGiaChiTietRepository.findAll();
        return phieuGiamGiaChiTietList.stream()
                .map(phieuGiamGiaChiTiet -> modelMapper.map(phieuGiamGiaChiTiet, PhieuGiamGiaChiTietDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updatePhieuGiamGiaToCaNhan(Integer idPhieuGiamGia, List<Integer> selectedKhachHangIds) {
        // Lấy danh sách phiếu giảm giá chi tiết liên quan
        List<PhieuGiamGiaChiTiet> chiTietList = phieuGiamGiaChiTietRepository.findAllByIdPhieuGiamGia(idPhieuGiamGia);

        // Lấy thông tin phiếu giảm giá gốc
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(idPhieuGiamGia)
                .orElseThrow(() -> new ResourceNotfound("Phiếu giảm giá không tồn tại: " + idPhieuGiamGia));
        phieuGiamGia.setDoiTuongApDung(true); // Chuyển sang cá nhân
        phieuGiamGiaRepository.save(phieuGiamGia);

        // Tạo context email
        Context context = new Context();
        context.setVariable("ten", phieuGiamGia.getTenPhieuGiamGia());
        String hinhThuc = phieuGiamGia.getHinhThucGiamGia() ? "%" : "VND";
        context.setVariable("mucGiam", phieuGiamGia.getMucGiam() + " " + hinhThuc);
        context.setVariable("ketThuc", phieuGiamGia.getNgayKetThuc());
        context.setVariable("batDau", phieuGiamGia.getNgayBatDau());
        context.setVariable("dieuKienToiThieu", phieuGiamGia.getDieuKienSuDung());

        int index = 0;

        // Gán lần lượt idKhachHang cho các phiếu trống
        for (; index < chiTietList.size() && index < selectedKhachHangIds.size(); index++) {
            PhieuGiamGiaChiTiet chiTiet = chiTietList.get(index);
            Integer khachHangId = selectedKhachHangIds.get(index);
            KhachHang khachHang = khachHangRepository.findById(khachHangId)
                    .orElseThrow(() -> new ResourceNotfound("Khách hàng không tồn tại: " + khachHangId));
            chiTiet.setKhachHang(khachHang);
            phieuGiamGiaChiTietRepository.save(chiTiet);

            // Gửi email bất đồng bộ
            emailService.sendPGGEmail(khachHang.getEmail(), "Thông báo bạn có một phiếu giảm giá!", "pgg", context);
        }

        // Nếu còn khách hàng chưa được gán, tạo thêm phiếu giảm giá chi tiết
        for (; index < selectedKhachHangIds.size(); index++) {
            Integer khachHangId = selectedKhachHangIds.get(index);
            KhachHang khachHang = khachHangRepository.findById(khachHangId)
                    .orElseThrow(() -> new ResourceNotfound("Khách hàng không tồn tại: " + khachHangId));

            PhieuGiamGiaChiTiet newChiTiet = new PhieuGiamGiaChiTiet();
            newChiTiet.setPhieuGiamGia(phieuGiamGia);
            newChiTiet.setKhachHang(khachHang);
            newChiTiet.setDeletedAt(false);
            phieuGiamGiaChiTietRepository.save(newChiTiet);

            // Gửi email bất đồng bộ
            emailService.sendPGGEmail(khachHang.getEmail(), "Thông báo bạn có một phiếu giảm giá!", "pgg", context);
        }
    }

    @Override
    public PhieuGiamGiaChiTietDTO save(PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO) {
        // Convert DTO to entity
        PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = modelMapper.map(phieuGiamGiaChiTietDTO, PhieuGiamGiaChiTiet.class);
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.getReferenceById(phieuGiamGiaChiTietDTO.getIdPhieuGiamGia());
        System.out.println(phieuGiamGia);
        // Kiểm tra kiểu công khai
        if (phieuGiamGia.getDoiTuongApDung() == false) {
            phieuGiamGiaChiTiet.setKhachHang(null);
        } else {
            KhachHang khachHang = khachHangRepository.getReferenceById(phieuGiamGiaChiTietDTO.getIdKhachHang());
            phieuGiamGiaChiTiet.setKhachHang(khachHang);
        }
        phieuGiamGiaChiTiet.setDeletedAt(false);

        // Lưu entity vào repository
        PhieuGiamGiaChiTiet savedEntity = phieuGiamGiaChiTietRepository.save(phieuGiamGiaChiTiet);

        // Gửi email bất đồng bộ nếu có khách hàng
        if (savedEntity.getKhachHang() != null) {
            KhachHang khachHang = savedEntity.getKhachHang();
//            PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.getReferenceById(savedEntity.getPhieuGiamGia().getId());

            Context context = new Context();
            context.setVariable("ten", phieuGiamGia.getTenPhieuGiamGia());
            String hinhThuc = phieuGiamGia.getHinhThucGiamGia() ? "%" : "VND";
            context.setVariable("mucGiam", phieuGiamGia.getMucGiam() + " " + hinhThuc);
            context.setVariable("ketThuc", phieuGiamGia.getNgayKetThuc());
            context.setVariable("batDau", phieuGiamGia.getNgayBatDau());
            context.setVariable("dieuKienToiThieu", phieuGiamGia.getDieuKienSuDung());

            emailService.sendPGGEmail(khachHang.getEmail(), "Thông báo bạn có một phiếu giảm giá mới!", "pgg", context);
        }

        // Trả về DTO sau khi lưu
        return modelMapper.map(savedEntity, PhieuGiamGiaChiTietDTO.class);
    }


    @Override
    public PhieuGiamGiaChiTietDTO update(Integer id, PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO) {
        PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = phieuGiamGiaChiTietRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá chi tiết với id " + id));
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(phieuGiamGiaChiTietDTO.getIdPhieuGiamGia()).orElseThrow();
        KhachHang khachHang = khachHangRepository.findById(phieuGiamGiaChiTietDTO.getIdKhachHang()).orElseThrow();
        phieuGiamGiaChiTiet.setPhieuGiamGia(phieuGiamGia);
        phieuGiamGiaChiTiet.setKhachHang(khachHang);
        phieuGiamGiaChiTiet.setDeletedAt(phieuGiamGiaChiTietDTO.getDeletedAt());
        PhieuGiamGiaChiTiet updatedEntity = phieuGiamGiaChiTietRepository.save(phieuGiamGiaChiTiet);
        return modelMapper.map(updatedEntity, PhieuGiamGiaChiTietDTO.class);
    }


    @Override
    public PhieuGiamGiaChiTietDTO getOne(Integer id) {
        return modelMapper.map(phieuGiamGiaChiTietRepository.findById(id).orElseThrow(() ->
                new ResourceNotfound("Không tồn tại phieu giam gia ID: " + id)), PhieuGiamGiaChiTietDTO.class);
    }

    @Override
    public List<PhieuGiamGiaChiTietDTO> findByIdPGG(Integer idPhieuGiamGia) {
        List<PhieuGiamGiaChiTiet> phieuGiamGiaCTList = phieuGiamGiaChiTietRepository.findAllByIdPhieuGiamGia(idPhieuGiamGia);
        return phieuGiamGiaCTList.stream()
                .map(phieuGiamGiaChiTiet -> modelMapper.map(phieuGiamGiaChiTiet, PhieuGiamGiaChiTietDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PhieuGiamGiaChiTietDTO> findByIdKhachHang(Integer idKhachHang) {
        List<PhieuGiamGiaChiTiet> phieuGiamGiaCTList = phieuGiamGiaChiTietRepository.findAllByIdKhachHang(idKhachHang);
        return phieuGiamGiaCTList.stream()
                .map(phieuGiamGiaChiTiet -> modelMapper.map(phieuGiamGiaChiTiet, PhieuGiamGiaChiTietDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PhieuGiamGiaChiTietDTO> getAllPGGCTCongKhai(Double tongTien, String keyWord) {
        List<PhieuGiamGiaChiTiet> phieuGiamGiaChiTietList = phieuGiamGiaChiTietRepository.getAllPGGCongKhai(tongTien, keyWord);
        return phieuGiamGiaChiTietList.stream()
                .map(phieuGiamGiaChiTiet -> modelMapper.map(phieuGiamGiaChiTiet, PhieuGiamGiaChiTietDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PhieuGiamGiaChiTietDTO> getAllPGGCTCaNhan(Integer idKhachHang, Double tongTien, String keyWord) {
        List<PhieuGiamGiaChiTiet> phieuGiamGiaChiTietList = phieuGiamGiaChiTietRepository.getAllPGGCaNhan(idKhachHang, tongTien, keyWord);
        return phieuGiamGiaChiTietList.stream()
                .map(phieuGiamGiaChiTiet -> modelMapper.map(phieuGiamGiaChiTiet, PhieuGiamGiaChiTietDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateDeletedAt(Integer id, Integer idKhachHang, Boolean deletedAt) {
        // Lấy phiếu giảm giá chi tiết
        PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = phieuGiamGiaChiTietRepository.findByIdAndKhachHangId(id, idKhachHang)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá chi tiết với id " + id + " và khách hàng id " + idKhachHang));

        Boolean currentDeletedAt = phieuGiamGiaChiTiet.getDeletedAt();
        phieuGiamGiaChiTiet.setDeletedAt(deletedAt);

        // Lưu phiếu giảm giá chi tiết
        PhieuGiamGiaChiTiet savedEntity = phieuGiamGiaChiTietRepository.save(phieuGiamGiaChiTiet);

        // Lấy thông tin khách hàng và phiếu giảm giá
        KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với id " + idKhachHang));
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.getReferenceById(savedEntity.getPhieuGiamGia().getId());

        // Tạo context email
        Context context = new Context();
        context.setVariable("ten", phieuGiamGia.getTenPhieuGiamGia());
        String hinhThuc = phieuGiamGia.getHinhThucGiamGia() ? "%" : "VND";
        context.setVariable("mucGiam", phieuGiamGia.getMucGiam() + " " + hinhThuc);
        context.setVariable("ketThuc", phieuGiamGia.getNgayKetThuc());
        context.setVariable("batDau", phieuGiamGia.getNgayBatDau());
        context.setVariable("dieuKienToiThieu", phieuGiamGia.getDieuKienSuDung());

        String emailKhachHang = khachHang.getEmail();
        if (emailKhachHang == null || emailKhachHang.isEmpty()) {
            throw new IllegalArgumentException("Email khách hàng không được trống");
        }

        // Gửi email bất đồng bộ với template phù hợp
        if (!currentDeletedAt && deletedAt) {
            emailService.sendPGGEmail(emailKhachHang, "Thông báo bạn có một phiếu giảm giá hết hạn!", "pgg-het-han", context);
        } else {
            emailService.sendPGGEmail(emailKhachHang, "Thông báo bạn có một phiếu giảm giá mới!", "pgg", context);
        }
    }


    @Override
    public PhieuGiamGiaChiTietDTO findByIdPGGAndKhachHang(Integer id, Integer idKhachHang) {
        PhieuGiamGiaChiTiet phieuGiamGiaCTList = phieuGiamGiaChiTietRepository.findByIdAndKhachHangId(id, idKhachHang).orElseThrow();
        return modelMapper.map(phieuGiamGiaCTList, PhieuGiamGiaChiTietDTO.class);
    }

    @Transactional
    public void updatePhieuGiamGiaStatusToEnded(Integer idPhieuGiamGia) {
        // Cập nhật trạng thái phiếu giảm giá chính
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(idPhieuGiamGia)
                .orElseThrow(() -> new ResourceNotfound("Phiếu giảm giá không tồn tại: " + idPhieuGiamGia));
        phieuGiamGia.setTrangThai("Đã kết thúc");
        phieuGiamGiaRepository.save(phieuGiamGia);

        // Lấy danh sách chi tiết phiếu giảm giá liên quan
        List<PhieuGiamGiaChiTiet> chiTietList = phieuGiamGiaChiTietRepository.findAllByIdPhieuGiamGia(idPhieuGiamGia);

        // Tạo context email
        Context context = new Context();
        context.setVariable("ten", phieuGiamGia.getTenPhieuGiamGia());
        String hinhThuc = phieuGiamGia.getHinhThucGiamGia() ? "%" : "VND";
        context.setVariable("mucGiam", phieuGiamGia.getMucGiam() + " " + hinhThuc);
        context.setVariable("ketThuc", phieuGiamGia.getNgayKetThuc());
        context.setVariable("batDau", phieuGiamGia.getNgayBatDau());
        context.setVariable("dieuKienToiThieu", phieuGiamGia.getDieuKienSuDung());

        // Cập nhật trạng thái chi tiết và gửi email
        for (PhieuGiamGiaChiTiet chiTiet : chiTietList) {
            if (chiTiet.getKhachHang() != null) {
                // Cập nhật trạng thái chi tiết
                chiTiet.setDeletedAt(true);
                phieuGiamGiaChiTietRepository.save(chiTiet);

                // Gửi email
                KhachHang khachHang = chiTiet.getKhachHang();
                emailService.sendPGGEmail(
                        khachHang.getEmail(),
                        "Thông báo: Phiếu giảm giá của bạn đã hết hạn!",
                        "pgg-het-han",
                        context
                );
            }
        }
    }

}
