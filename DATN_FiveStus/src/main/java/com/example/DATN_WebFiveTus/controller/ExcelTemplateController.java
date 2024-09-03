package com.example.DATN_WebFiveTus.controller;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/template")
public class ExcelTemplateController {

    @GetMapping("/nuoc-uong/download")
    public ResponseEntity<InputStreamResource> downloadNuocUongTemplate() throws IOException {
        InputStream templateStream = createNuocUongTemplate();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=nuoc-uong-template.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(templateStream));
    }

    @GetMapping("/do-thue/download")
    public ResponseEntity<InputStreamResource> downloadDoThueTemplate() throws IOException {
        InputStream templateStream = createDoThueTemplate();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=do-thue-template.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(templateStream));
    }

    private InputStream createNuocUongTemplate() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Nước Uống");

        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Tên nước uống");
        header.createCell(1).setCellValue("Giá");
        header.createCell(2).setCellValue("Số lượng");

        // Write the output to a ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return new ByteArrayInputStream(baos.toByteArray());
    }

    private InputStream createDoThueTemplate() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Đồ Thuê");

        // Create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Tên đồ thuê");
        header.createCell(1).setCellValue("Giá");
        header.createCell(2).setCellValue("Số lượng");

        // Write the output to a ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();

        return new ByteArrayInputStream(baos.toByteArray());
    }
}
