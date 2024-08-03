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

        // Save entity to repository
        PhieuGiamGiaChiTiet savedEntity = phieuGiamGiaChiTietRepository.save(phieuGiamGiaChiTiet);

        // Gửi email thông báo
        sendEmail(savedEntity);

        return modelMapper.map(savedEntity, PhieuGiamGiaChiTietDTO.class);

    }
    private void sendEmail(PhieuGiamGiaChiTiet savedEntity) {
        KhachHang khachHang = khachHangRepository.getReferenceById(savedEntity.getKhachHang().getId());
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.getReferenceById(savedEntity.getPhieuGiamGia().getId());

        // Gửi email thông báo
        String emailKhachHang = khachHang.getEmail();

        if (emailKhachHang == null || emailKhachHang.isEmpty()) {
            throw new IllegalArgumentException("Email khách hàng không được trống");
        }

        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            Context context = new Context();
            context.setVariable("ten", phieuGiamGia.getTenPhieuGiamGia());
            String hinhThuc = phieuGiamGia.getHinhThucGiamGia() == true ? "%" : "VND";
            context.setVariable("mucGiam", phieuGiamGia.getMucGiam() + " " + hinhThuc);
            context.setVariable("ketThuc", phieuGiamGia.getNgayKetThuc());
            context.setVariable("batDau", phieuGiamGia.getNgayBatDau());
            context.setVariable("dieuKienToiThieu", phieuGiamGia.getDieuKienSuDung());

            String html = springTemplateEngine.process("pgg", context);

            mimeMessage.setContent(html, "text/html; charset=utf-8");

            helper.setTo(emailKhachHang);
            helper.setSubject("Thông báo bạn có một phiếu giảm giá mới!");
            helper.setText(html, true);

            javaMailSender.send(mimeMessage); // Gửi email

        } catch (MessagingException e) {
            e.printStackTrace(); // Xử lý ngoại lệ MessagingException
        } catch (Exception e) {
            e.printStackTrace(); // Xử lý ngoại lệ khác
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

        // Gửi email nếu trạng thái đã thay đổi từ false thành true
        if (!currentDeletedAt && deletedAt) {
            sendEmail1(emailKhachHang, "Thông báo bạn có một phiếu giảm giá hết hạn!", "pgg-het-han", context);
        } else {
            sendEmail1(emailKhachHang, "Thông báo bạn có một phiếu giảm giá!", "pgg", context);
        }

    }

    private void sendEmail1(String to, String subject, String templateName, Context context) {
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
