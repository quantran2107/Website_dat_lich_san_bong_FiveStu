package com.example.DATN_WebFiveTus.controller;
import com.example.DATN_WebFiveTus.dto.DoThueDTO;
import com.example.DATN_WebFiveTus.dto.NuocUongDTO;
import com.example.DATN_WebFiveTus.service.DoThueService;
import com.example.DATN_WebFiveTus.service.NuocUongService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/import")
public class ExcelUploadController {

    @Autowired
    private NuocUongService nuocUongService;

    @Autowired
    private DoThueService doThueService;

    @PostMapping("/nuoc-uong")
    public ResponseEntity<String> importNuocUongExcel(@RequestParam("file") MultipartFile file) {
        try {
            // Đọc dữ liệu từ tệp Excel
            List<NuocUongDTO> nuocUongList = parseNuocUongExcelFile(file.getInputStream());
            for (NuocUongDTO nuocUongDTO : nuocUongList) {
                nuocUongService.save(nuocUongDTO);
            }
            return ResponseEntity.ok("Nước uống đã được nhập thành công.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nhập dữ liệu thất bại.");
        }
    }

    @PostMapping("/do-thue")
    public ResponseEntity<String> importDoThueExcel(@RequestParam("file") MultipartFile file) {
        try {
            // Đọc dữ liệu từ tệp Excel
            List<DoThueDTO> doThueList = parseDoThueExcelFile(file.getInputStream());
            for (DoThueDTO doThueDTO : doThueList) {
                doThueService.save(doThueDTO);
            }
            return ResponseEntity.ok("Đồ thuê đã được nhập thành công.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Nhập dữ liệu thất bại.");
        }
    }

    private List<NuocUongDTO> parseNuocUongExcelFile(InputStream inputStream) throws IOException {
        List<NuocUongDTO> nuocUongList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }

            NuocUongDTO nuocUongDTO = new NuocUongDTO();
            nuocUongDTO.setTenNuocUong(row.getCell(0).getStringCellValue());
            nuocUongDTO.setDonGias((float) row.getCell(1).getNumericCellValue());
            nuocUongDTO.setSoLuongs((int) row.getCell(1).getNumericCellValue());
            if(nuocUongDTO.getSoLuongs()==0){
                nuocUongDTO.setTrangThai("Hết");
            }else{
                nuocUongDTO.setTrangThai("Còn");
            }
            nuocUongList.add(nuocUongDTO);
        }

        workbook.close();
        return nuocUongList;
    }

    private List<DoThueDTO> parseDoThueExcelFile(InputStream inputStream) throws IOException {
        List<DoThueDTO> doThueList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            if (row.getRowNum() == 0) {
                continue; // Skip header row
            }

            DoThueDTO doThueDTO = new DoThueDTO();
            doThueDTO.setTenDoThue(row.getCell(0).getStringCellValue());
            doThueDTO.setDonGias((float) row.getCell(1).getNumericCellValue());
            doThueDTO.setSoLuongs((int) row.getCell(1).getNumericCellValue());
            if(doThueDTO.getSoLuongs()==0){
                doThueDTO.setTrangThai("Hết");
            }else{
                doThueDTO.setTrangThai("Còn");
            }
            doThueList.add(doThueDTO);
        }

        workbook.close();
        return doThueList;
    }
}
