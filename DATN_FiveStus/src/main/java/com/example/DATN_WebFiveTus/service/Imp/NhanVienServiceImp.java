package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.config.RoleFactory;
import com.example.DATN_WebFiveTus.config.security.CookieUtils;
import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.entity.auth.Role;
import com.example.DATN_WebFiveTus.entity.auth.User;
import com.example.DATN_WebFiveTus.exception.RoleNotFoundException;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.repository.UserRepository;
import com.example.DATN_WebFiveTus.service.NhanVienService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleFactory roleFactory;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private JwtUtils jwtUtils;

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
    public Object addNew(NhanVienDTO nv) throws RoleNotFoundException {
        NhanVien nhanVien = modelMapper.map(nv, NhanVien.class);
        NhanVien nv2 = nhanVienReposity.findByUsername(nv.getEmail());
        if (nv2 != null) {
            return "Email đã tồn tại!";
        }
        nhanVien.setMaNhanVien(generateMaNV());
        nhanVien.setTenNhanVien(generateTKNV(nv.getHoTen()));
        String pass = generateMK(16);
        nhanVien.setMatKhau(passwordEncoder.encode(pass));
        nhanVien.setTrangThai("active");
        boolean checkMail = mailFunction(nhanVien, pass);
        if (checkMail) {
            createUser(nhanVien);
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
            Workbook workbook = new XSSFWorkbook(inputStream);

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
                    break;
                }
                NhanVien nhanVien = createNhanVienFormRow(currentRow, dateFormatter);
                if (nhanVien != null) {
                    String pass = generateMK(16);
                    nhanVien.setMatKhau(passwordEncoder.encode(pass));
                    createUser(nhanVien);
                    boolean checkMail = mailFunction(nhanVien, pass);
                    if (checkMail) {
                        nhanVienReposity.save(nhanVien);
                    }
                }
            }

            workbook.close();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        } catch (RoleNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<?> getOneNv(int id) {
        NhanVien nv = nhanVienReposity.findById(id).orElse(null);
        if (nv != null) {
            return ResponseEntity.ok(modelMapper.map(nv, NhanVienDTO.class));
        }
        return ResponseEntity
                .badRequest()
                .body("null");
    }

    @Override
    public ResponseEntity<?> getForCode(String maNV) {
        NhanVien nv = nhanVienReposity.findByMaNhanVien(maNV);
        if (nv != null) {
            return ResponseEntity.ok(modelMapper.map(nv, NhanVienDTO.class));
        }
        return ResponseEntity.ok(maNV);
    }

    @Override
    public NhanVienDTO getNV(HttpServletRequest request) {
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            NhanVien nv = nhanVienReposity.findByUsername(username);
            return modelMapper.map(nv, NhanVienDTO.class);
        }
        return null;
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
            nhanVien.setNgaySinh(LocalDate.parse(cellNgaySinh.getStringCellValue(), dateTimeFormatter));
            nhanVien.setGioiTinh(cellGioiTinh.getStringCellValue().equalsIgnoreCase("Nam") ? true : false);
            nhanVien.setEmail(cellEmail.getStringCellValue());
            nhanVien.setSoDienThoai(cellSDT.getStringCellValue());

            String diaChi = cellDc.getStringCellValue() + ", " + cellTinh.getStringCellValue() + ", " + cellHuyen.getStringCellValue() + ", " + cellXa.getStringCellValue();

            nhanVien.setDiaChi(diaChi);

            nhanVien.setMaNhanVien(generateMaNV());
            nhanVien.setTenNhanVien(generateTKNV(nhanVien.getHoTen()));
            nhanVien.setTrangThai("active");
            return nhanVien;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public Boolean mailFunction(NhanVien nhanVien, String pass) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            Context context = new Context();
            context.setVariable("username", nhanVien.getEmail());
            context.setVariable("password", pass);

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
        Integer manv = list.get(list.size() - 1).getId() + 1;
        return "NV" + manv;
    }

    public String generateTKNV(String fullName) {
        String[] parts = fullName.split("\\s+");

        String lastName = parts[parts.length - 1].toLowerCase();

        StringBuilder initials = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            initials.append(Character.toLowerCase(parts[i].charAt(0)));
        }

        Random random = new Random();
        int randomNumber = random.nextInt(1000);

        String username = lastName + initials.toString() + randomNumber;
        String normalized = Normalizer.normalize(username, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(normalized).replaceAll("");
        result = result.replace("đ", "d").replace("Đ", "D");

        return result;
    }

    private void createUser(NhanVien nhanVien) throws RoleNotFoundException {
        Set<Role> roles = new HashSet<>();
        roles.add(roleFactory.getInstance("user"));
        roles.add(roleFactory.getInstance("employee"));
        userRepository.save(User.builder()
                .email(nhanVien.getEmail())
                .username(nhanVien.getEmail().substring(0, nhanVien.getEmail().indexOf("@")))
                .password(nhanVien.getMatKhau())
                .enabled(true)
                .roles(roles)
                .build());
        KhachHang khachHang = new KhachHang();
        khachHang.setEmail(nhanVien.getEmail());
        khachHang.setMaKhachHang(nhanVien.getEmail().substring(0, nhanVien.getEmail().indexOf("@")));
        khachHang.setMatKhau(nhanVien.getMatKhau());
        khachHangRepository.save(khachHang);
    }

}