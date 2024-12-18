package com.example.DATN_WebFiveTus.config.PDF;

import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PdfGeneratorRunner implements CommandLineRunner {

    @Autowired
    private PDFService pdfService;

    @Override
    public void run(String... args) throws Exception {
    }
}
