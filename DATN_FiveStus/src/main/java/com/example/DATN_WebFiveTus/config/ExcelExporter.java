package com.example.DATN_WebFiveTus.config;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
public class ExcelExporter extends AbstractExporter {


        public void Export(List<SanCaDTO> sanCaDTOList, HttpServletResponse httpServletResponse) throws IOException {
            super.setResponseHeader(httpServletResponse, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ".xlsx");


            Workbook workbook = new XSSFWorkbook();

            Sheet sheet = workbook.createSheet("SanCa Data");


            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("Tên sân bóng");
            headerRow.createCell(2).setCellValue("TênCa");
            headerRow.createCell(3).setCellValue("Bắt đầu");
            headerRow.createCell(4).setCellValue("Kết thúc");
            headerRow.createCell(5).setCellValue("Giá");
            headerRow.createCell(6).setCellValue("Trạng thái");

            int rowNum = 1;

            for (SanCaDTO sanCaDTO : sanCaDTOList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(sanCaDTO.getId());
                row.createCell(1).setCellValue(sanCaDTO.getTenSanBong());
                row.createCell(2).setCellValue(sanCaDTO.getTenCa());
                row.createCell(3).setCellValue(sanCaDTO.getThoiGianBatDauCa());
                row.createCell(4).setCellValue(sanCaDTO.getThoiGianKetThucCa());
                row.createCell(5).setCellValue(sanCaDTO.getGia());
                row.createCell(6).setCellValue(sanCaDTO.getTrangThai());
            }

            // Ghi Workbook vào OutputStream để xuất file
            OutputStream outputStream = httpServletResponse.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        }

//
//    public void Export(List<SanCaDTO> sanCaDTOList, HttpServletResponse httpServletResponse) {
//        super.setResponseHeader(httpServletResponse, "haipham3010", ".xlsx");
//        // Các công việc xuất Excel của bạn sẽ được thực hiện ở đây
//        // Ví dụ: ghi dữ liệu từ sanCaDTOList vào file Excel
//    }
}
