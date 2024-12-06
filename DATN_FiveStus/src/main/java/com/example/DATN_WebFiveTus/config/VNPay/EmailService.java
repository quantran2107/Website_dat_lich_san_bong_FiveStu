package com.example.DATN_WebFiveTus.config.VNPay;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
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
    public void sendInvoiceEmail(Integer idHoaDon, List<HoaDonChiTietDTO> hoaDonChiTietList, HoaDon hoaDon) {
        try {
            // Tạo mẫu email
            Context context = new Context();
            context.setVariable("hoaDon", hoaDon);
            context.setVariable("chiTietHoaDon", hoaDonChiTietList);

            String emailContent = springTemplateEngine.process("invoiceTemplate", context);

            // Tạo email
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(hoaDon.getKhachHang().getEmail());
            helper.setSubject("Hóa đơn thanh toán thành công");
            helper.setText(emailContent, true);

            // Gửi email
            javaMailSender.send(message);
        } catch (MessagingException e) {
            System.err.println("Lỗi khi gửi email: " + e.getMessage());
        }
    }
}
