package com.example.DATN_WebFiveTus.config.PDF;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

@Service
public class PDFService {

    @Autowired
    private TemplateEngine templateEngine;

    public void generatePdf(String outputPath, Object hoaDon, Object chiTietHoaDon) {
        try {
            // Thiết lập dữ liệu cho Thymeleaf
            Context context = new Context();
            context.setVariable("hoaDon", hoaDon);
            context.setVariable("chiTietHoaDon", chiTietHoaDon);

            // Render HTML từ template
            String htmlContent = templateEngine.process("hoaDon", context);

            // Chuyển đổi HTML sang PDF
            try (OutputStream os = new FileOutputStream(outputPath)) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(htmlContent, "");
                builder.toStream(os);
                builder.run();
            }

            System.out.println("PDF đã được tạo tại: " + outputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
