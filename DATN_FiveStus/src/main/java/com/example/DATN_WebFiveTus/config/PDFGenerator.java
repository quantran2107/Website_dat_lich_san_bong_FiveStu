package com.example.DATN_WebFiveTus.config;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PDFGenerator {

    public ByteArrayOutputStream generateInvoicePDF(HoaDonChiTietDTO hoaDonChiTietDTO) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();
            document.add(new Paragraph("Hóa Đơn Chi Tiết"));
            document.add(new Paragraph("Mã Hóa Đơn Chi Tiết: " + hoaDonChiTietDTO.getMaHoaDonChiTiet()));
            document.add(new Paragraph("ID Hóa Đơn: " + hoaDonChiTietDTO.getIdHoaDon()));
            document.add(new Paragraph("ID Sân Ca: " + hoaDonChiTietDTO.getIdSanCa()));
            document.add(new Paragraph("Ngày Đến Sân: " + hoaDonChiTietDTO.getNgayDenSan()));
            document.add(new Paragraph("Trạng Thái: " + hoaDonChiTietDTO.getTrangThai()));
            document.add(new Paragraph("Tổng Tiền: " + hoaDonChiTietDTO.getTongTien()));

            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return outputStream;
    }
}
