package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.service.NhanVienService;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class NhanVienServiceImp implements NhanVienService {

    private NhanVienReposity nhanVienReposity;
    private ModelMapper modelMapper;

    public NhanVienServiceImp(NhanVienReposity nhanVienReposity, ModelMapper modelMapper) {
        this.nhanVienReposity = nhanVienReposity;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<NhanVienDTO> getAll() {
        return nhanVienReposity.findAll().stream().map((nhanVien) ->modelMapper
                .map(nhanVien, NhanVienDTO.class)).collect(Collectors.toList());
    }


    @Override
    public Boolean updateNew(NhanVienDTO nv) {
        NhanVien nhanVien = nhanVienReposity.getReferenceById(nv.getId());
        if (nhanVien !=null){
            nhanVien =modelMapper.map(nv, NhanVien.class);
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
        nhanVienReposity.save(nhanVien);
        return true;
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

    public String generateMaNV(){
        List<NhanVien> list = nhanVienReposity.findAll();
        String manv = String.valueOf(list.get(list.size()-1).getId()) ;
        return "NV"+manv;
    }

    public String generateTKNV(String fullName){
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
