package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.LichLamViecDTO;
import com.example.DATN_WebFiveTus.entity.LichLamViec;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.repository.LichLamViecRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.service.LichLamViecService;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LichLamViecServiceImpl implements LichLamViecService {

    private NhanVienReposity nhanVienReposity;
    private LichLamViecRepository lichLamViecRepository;
    private ModelMapper modelMapper;

    public LichLamViecServiceImpl(NhanVienReposity nhanVienReposity, LichLamViecRepository lichLamViecRepository, ModelMapper modelMapper) {
        this.nhanVienReposity = nhanVienReposity;
        this.lichLamViecRepository = lichLamViecRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<LichLamViecDTO> getAll() {
        return lichLamViecRepository.findAll().stream().map((lichLamViec) -> modelMapper.map(lichLamViec, LichLamViecDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Boolean add(LichLamViecDTO lichLamViecDto) {
        lichLamViecRepository.save(modelMapper.map(lichLamViecDto, LichLamViec.class));
        return true;
    }

    @Override
    public Boolean addMore(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream); // Đọc file Excel dưới định dạng xlsx

            Sheet sheet = workbook.getSheetAt(0); // Lấy sheet đầu tiên
            Iterator<Row> iterator = sheet.iterator();

            List<LichLamViec> lichLamViecList = new ArrayList<>();

            // Bỏ qua dòng tiêu đề (nếu có)
            int skipRows = 3; // Số dòng cần bỏ qua từ đầu (ví dụ: bỏ qua 2 dòng đầu tiên)

// Bỏ qua số dòng cần thiết từ đầu
            for (int i = 0; i < skipRows; i++) {
                if (iterator.hasNext()) {
                    iterator.next();
                }
            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Cell firstCell = currentRow.getCell(0);
                if (firstCell == null || firstCell.getCellType() == CellType.BLANK) {
                    break; // Dừng vòng lặp nếu dòng không có dữ liệu
                }
                LichLamViec lichLamViec = createLichLamViecFromRow(currentRow,dateFormatter,timeFormatter);
               
                if (lichLamViec != null) {
                    lichLamViecList.add(lichLamViec);
                }
            }

            workbook.close();
            // Lưu vào cơ sở dữ liệu
            lichLamViecRepository.saveAll(lichLamViecList); // Cần có repository để lưu dữ liệu vào database

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private LichLamViec createLichLamViecFromRow(Row row, DateTimeFormatter dateFormatter, DateTimeFormatter timeFormatter) {
        LichLamViec lichLamViec = new LichLamViec();

        // Đọc dữ liệu từ các cell của row
        try {

            Cell cellNhanVien = row.getCell(0);
            Cell cellViTri = row.getCell(2);
            Cell cellGioBatDau = row.getCell(4);
            Cell cellGioKetThuc = row.getCell(5);
            Cell cellNgay = row.getCell(6);

            NhanVien nhanVien = nhanVienReposity.findByMaNhanVien(cellNhanVien.getStringCellValue());
            lichLamViec.setNhanVien(nhanVien);
            lichLamViec.setViTri(cellViTri.getStringCellValue());
            lichLamViec.setGioBatDau(LocalTime.parse(cellGioBatDau.getStringCellValue(),timeFormatter));
            lichLamViec.setGioKetThuc(LocalTime.parse(cellGioKetThuc.getStringCellValue(),timeFormatter));
            lichLamViec.setNgay(LocalDate.parse(cellNgay.getStringCellValue(),dateFormatter));

            return lichLamViec;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi trong quá trình đọc dữ liệu từ Excel
        }
    }
}