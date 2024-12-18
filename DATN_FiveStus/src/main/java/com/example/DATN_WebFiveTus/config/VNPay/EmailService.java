package com.example.DATN_WebFiveTus.config.VNPay;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Async
    @Transactional
    public void sendInvoiceEmail(HoaDonDTO hoaDonDTO, List<HoaDonChiTietDTO> hoaDonChiTietList) {
        try {
            // Tạo Context cho Thymeleaf
            Context context = new Context();
            context.setVariable("hoaDon", hoaDonDTO);
            context.setVariable("chiTietHoaDon", hoaDonChiTietList);

            // Render nội dung email từ template
            String emailContent = springTemplateEngine.process("invoiceTemplate", context);

            // Tạo email
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(hoaDonDTO.getEmailKhachHang());
            helper.setSubject("Hóa đơn thanh toán thành công");
            helper.setText(emailContent, true);

            // Gửi email
            javaMailSender.send(message);
        } catch (MessagingException e) {
//            System.err.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }

    @Async
    @Transactional
    public void sendPGGEmail(String email, String subject, String templateName, Context context) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Render nội dung email từ template
            String emailContent = springTemplateEngine.process(templateName, context);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(emailContent, true);

            // Gửi email
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
//            System.err.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}
