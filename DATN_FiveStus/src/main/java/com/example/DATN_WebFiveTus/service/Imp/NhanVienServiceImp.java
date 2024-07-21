package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.entity.LichLamViec;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.service.NhanVienService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class NhanVienServiceImp implements NhanVienService {

    private NhanVienReposity nhanVienReposity;
    private ModelMapper modelMapper;
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;


    public NhanVienServiceImp(NhanVienReposity nhanVienReposity, ModelMapper modelMapper) {
        this.nhanVienReposity = nhanVienReposity;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<NhanVienDTO> getAll() {
        return nhanVienReposity.findAll().stream().map((nhanVien) -> modelMapper
                .map(nhanVien, NhanVienDTO.class)).collect(Collectors.toList());
    }


    @Override
    public Boolean updateNew(NhanVienDTO nv) {
        NhanVien nhanVien = nhanVienReposity.getReferenceById(nv.getId());
        if (nhanVien != null) {
            nhanVien = modelMapper.map(nv, NhanVien.class);
            nhanVienReposity.save(nhanVien);
            return true;
        }
        return false;
    }

    @Override
    public Boolean addNew(NhanVienDTO nv) {
        NhanVien nhanVien = modelMapper.map(nv, NhanVien.class);
        nhanVien.setMaNhanVien(generateMaNV());
        nhanVien.setTenNhanVien(generateTKNV(nv.getHoTen()));
        nhanVien.setMatKhau(generateMK(16));
        nhanVien.setTrangThai("active");
        boolean checkMail = mailFunction(nhanVien);
        if (checkMail) {
            nhanVienReposity.save(nhanVien);
            return true;
        }
        return false;
    }

    @Override
    public List<NhanVienDTO> getActiveNV() {
        return nhanVienReposity.findAllActive().stream().map((nhanVien) -> modelMapper
                .map(nhanVien, NhanVienDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<NhanVienDTO> getInactiveNV() {
        return nhanVienReposity.findAllInActive().stream().map((nhanVien) -> modelMapper
                .map(nhanVien, NhanVienDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Boolean addMore(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook= new XSSFWorkbook(inputStream);

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();


            int skipRows = 2;
            for (int i = 0; i < skipRows; i++) {
                if (iterator.hasNext()) {
                    iterator.next();
                }
            }

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                Cell firstCell = currentRow.getCell(1);
                if (firstCell == null || firstCell.getCellType() == CellType.BLANK) {
                    break; // Dừng vòng lặp nếu dòng không có dữ liệu
                }
                NhanVien nhanVien = createNhanVienFormRow(currentRow,dateFormatter);

                if (nhanVien != null) {
                    boolean checkMail = mailFunction(nhanVien);
                    if (checkMail) {
                        nhanVienReposity.save(nhanVien);
                    }
                }
            }

            workbook.close();


            return true;
        } catch (IOException e ){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private NhanVien createNhanVienFormRow(Row row, DateTimeFormatter dateTimeFormatter) {
        NhanVien nhanVien = new NhanVien();
        try {
            Cell cellTenNV = row.getCell(1);
            Cell cellNgaySinh = row.getCell(2);
            Cell cellGioiTinh = row.getCell(3);
            Cell cellEmail = row.getCell(4);
            Cell cellSDT = row.getCell(5);
            Cell cellDc = row.getCell(6);
            Cell cellTinh = row.getCell(7);
            Cell cellHuyen = row.getCell(8);
            Cell cellXa = row.getCell(9);

            nhanVien.setHoTen(cellTenNV.getStringCellValue());
            nhanVien.setNgaySinh(LocalDate.parse(cellNgaySinh.getStringCellValue(),dateTimeFormatter));
            nhanVien.setGioiTinh(cellGioiTinh.getStringCellValue().equalsIgnoreCase("Nam")?true:false);
            nhanVien.setEmail(cellEmail.getStringCellValue());
            nhanVien.setSoDienThoai(cellSDT.getStringCellValue());

            String diaChi = cellDc.getStringCellValue()+", "+cellTinh.getStringCellValue() +", "+cellHuyen.getStringCellValue()+", "+cellXa.getStringCellValue();

            nhanVien.setDiaChi(diaChi);

            nhanVien.setMaNhanVien(generateMaNV());
            nhanVien.setTenNhanVien(generateTKNV(nhanVien.getHoTen()));
            nhanVien.setMatKhau(generateMK(16));
            nhanVien.setTrangThai("active");
            return nhanVien;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public Boolean mailFunction(NhanVien nhanVien) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            Context context = new Context();
            context.setVariable("username", nhanVien.getTenNhanVien());
            context.setVariable("password", nhanVien.getMatKhau());

            String html = springTemplateEngine.process("userNV", context);

            helper.setTo(nhanVien.getEmail());
            helper.setSubject("Thông báo tài khoản và mật khẩu");
            helper.setText(html, true);

            javaMailSender.send(mimeMessage);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace(); // Xử lý exception theo nhu cầu của bạn
            return false;
        }
    }


    public static String generateMK(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }

    public String generateMaNV() {
        List<NhanVien> list = nhanVienReposity.findAll();
        if (list.isEmpty()) {
            return "NV001";
        }
        Integer manv =list.get(list.size() - 1).getId()+1;
        return "NV" + manv;
    }

    public String generateTKNV(String fullName) {
        // Tách tên thành các từ
        String[] parts = fullName.split("\\s+");

        // Lấy từ cuối cùng làm họ chính (viết thường)
        String lastName = parts[parts.length - 1].toLowerCase();
        lastName = StringUtils.stripAccents(lastName);

        // Lấy chữ cái đầu của các từ còn lại (không bao gồm từ cuối cùng) và viết thường
        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            initials.append(Character.toLowerCase(parts[i].charAt(0)));
        }

        Random random = new Random();
        int randomNumber = random.nextInt(1000); // Sinh số từ 100 đến 999

        // Kết hợp lại với số ngẫu nhiên
        String username = lastName + initials.toString() + randomNumber;

        return username;
    }

}
