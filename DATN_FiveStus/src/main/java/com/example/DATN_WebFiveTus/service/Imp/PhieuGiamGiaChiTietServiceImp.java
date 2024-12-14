package com.example.DATN_WebFiveTus.service.Imp;

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

    @Override
    public PhieuGiamGiaChiTietDTO save(PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO) {
        // Convert DTO to entity
        PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = modelMapper.map(phieuGiamGiaChiTietDTO, PhieuGiamGiaChiTiet.class);
        System.out.println(phieuGiamGiaChiTiet.getKhachHang());
        // Kiểm tra kiểu công khai
        if (phieuGiamGiaChiTiet.getKhachHang() == null) {
            phieuGiamGiaChiTiet.setKhachHang(null);
        }
        else{
            KhachHang khachHang = khachHangRepository.getReferenceById(phieuGiamGiaChiTietDTO.getIdKhachHang());
            phieuGiamGiaChiTiet.setKhachHang(khachHang);
        }
        // Lưu entity vào repository
        PhieuGiamGiaChiTiet savedEntity = phieuGiamGiaChiTietRepository.save(phieuGiamGiaChiTiet);

        // Gửi email bất đồng bộ nếu có khách hàng
        if (savedEntity.getKhachHang() != null) {
            sendEmail(savedEntity); // Không chờ đợi kết quả
        }

        // Trả về DTO sau khi lưu
        return modelMapper.map(savedEntity, PhieuGiamGiaChiTietDTO.class);
    }

    @Async
    public void sendEmail(PhieuGiamGiaChiTiet savedEntity) {
        try {
            // Kiểm tra khách hàng
            KhachHang khachHang = savedEntity.getKhachHang();
            if (khachHang == null ) {
                return; // Không gửi email nếu không có khách hàng
            }

            KhachHang khachHangEntity = khachHangRepository.getReferenceById(khachHang.getId());
            PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.getReferenceById(savedEntity.getPhieuGiamGia().getId());

            String emailKhachHang = khachHangEntity.getEmail();
            if (emailKhachHang == null || emailKhachHang.isEmpty()) {
                throw new IllegalArgumentException("Email khách hàng không được trống");
            }

            // Tạo email
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            Context context = new Context();
            context.setVariable("ten", phieuGiamGia.getTenPhieuGiamGia());
            String hinhThuc = phieuGiamGia.getHinhThucGiamGia() ? "%" : "VND";
            context.setVariable("mucGiam", phieuGiamGia.getMucGiam() + " " + hinhThuc);
            context.setVariable("ketThuc", phieuGiamGia.getNgayKetThuc());
            context.setVariable("batDau", phieuGiamGia.getNgayBatDau());
            context.setVariable("dieuKienToiThieu", phieuGiamGia.getDieuKienSuDung());

            String html = springTemplateEngine.process("pgg", context);

            helper.setTo(emailKhachHang);
            helper.setSubject("Thông báo bạn có một phiếu giảm giá mới!");
            helper.setText(html, true);

            // Gửi email
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            // Ghi log lỗi gửi email
            e.printStackTrace();
        } catch (Exception e) {
            // Ghi log các lỗi khác
            e.printStackTrace();
        }
    }

    @Override
    public PhieuGiamGiaChiTietDTO update(Integer id, PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO) {
        PhieuGiamGiaChiTiet existingEntity = phieuGiamGiaChiTietRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá chi tiết với id " + id));

        PhieuGiamGiaChiTiet updatedEntity = phieuGiamGiaChiTietRepository.save(existingEntity);
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
    public List<PhieuGiamGiaChiTietDTO> getAllPGGCTCongKhai(Double tongTien) {
        List<PhieuGiamGiaChiTiet> phieuGiamGiaChiTietList = phieuGiamGiaChiTietRepository.getAllPGGCongKhai(tongTien);
        return phieuGiamGiaChiTietList.stream()
                .map(phieuGiamGiaChiTiet -> modelMapper.map(phieuGiamGiaChiTiet, PhieuGiamGiaChiTietDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<PhieuGiamGiaChiTietDTO> getAllPGGCTCaNhan(Integer idKhachHang,Double tongTien) {
        List<PhieuGiamGiaChiTiet> phieuGiamGiaChiTietList = phieuGiamGiaChiTietRepository.getAllPGGCaNhan(idKhachHang,tongTien);
        return phieuGiamGiaChiTietList.stream()
                .map(phieuGiamGiaChiTiet -> modelMapper.map(phieuGiamGiaChiTiet, PhieuGiamGiaChiTietDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateDeletedAt(Integer id, Integer idKhachHang, Boolean deletedAt) {
        PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = phieuGiamGiaChiTietRepository.findByIdAndKhachHangId(id, idKhachHang)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá chi tiết với id " + id + " và khách hàng id " + idKhachHang));

        Boolean currentDeletedAt = phieuGiamGiaChiTiet.getDeletedAt();
        phieuGiamGiaChiTiet.setDeletedAt(deletedAt);

        // Save entity to repository
        PhieuGiamGiaChiTiet savedEntity = phieuGiamGiaChiTietRepository.save(phieuGiamGiaChiTiet);

        // Fetch related entities
        KhachHang khachHang = khachHangRepository.findById(idKhachHang)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khách hàng với id " + idKhachHang));
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.getReferenceById(savedEntity.getPhieuGiamGia().getId());

        // Prepare email context
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

        // Gửi email bất đồng bộ
        if (!currentDeletedAt && deletedAt) {
            sendEmail1(emailKhachHang, "Thông báo bạn có một phiếu giảm giá hết hạn!", "pgg-het-han", context);
        } else {
            sendEmail1(emailKhachHang, "Thông báo bạn có một phiếu giảm giá!", "pgg", context);
        }

    }

    @Async
    public void sendEmail1(String to, String subject, String templateName, Context context) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String html = springTemplateEngine.process(templateName, context);
            mimeMessage.setContent(html, "text/html; charset=utf-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(html, true);

            javaMailSender.send(mimeMessage); // Gửi email

        } catch (MessagingException e) {
            e.printStackTrace(); // Xử lý ngoại lệ MessagingException
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý ngoại lệ khác
        }
    }

}
