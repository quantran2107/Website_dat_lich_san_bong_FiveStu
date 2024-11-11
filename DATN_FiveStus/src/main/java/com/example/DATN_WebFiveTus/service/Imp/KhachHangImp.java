package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.DATN_WebFiveTus.dto.DiaChiKhachHangDTO;
import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.entity.DiaChiKhachHang;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.service.KhachHangService;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class KhachHangImp implements KhachHangService {

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private DiaChiKhachHangRepository diaChiKhachHangRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<KhachHangDTO> getAll() {
        List<KhachHang> khachHangs = khachHangRepository.findAll();
        return khachHangs.stream()
                .map(khachHang -> modelMapper.map(khachHang, KhachHangDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public KhachHangDTO getOne(Integer id) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfound("Không tồn tại id: " + id));
        if (!khachHang.isDeletedAt()) {
            throw new ResourceNotfound("Bản ghi đã bị xoá: " + id);
        }
        return modelMapper.map(khachHang, KhachHangDTO.class);
    }

    @Override
    public KhachHangDTO save(KhachHangDTO khachHangDTO) {
        KhachHang khachHang = modelMapper.map(khachHangDTO, KhachHang.class);
        khachHang = khachHangRepository.save(khachHang);

        List<DiaChiKhachHangDTO> diaChiList = khachHangDTO.getDiaChi();
        if (diaChiList != null && !diaChiList.isEmpty()) {
            for (DiaChiKhachHangDTO diaChiKhachHangDTO : diaChiList) {
                DiaChiKhachHang diaChiKhachHang = modelMapper.map(diaChiKhachHangDTO, DiaChiKhachHang.class);
                diaChiKhachHang.setIdKhachHang(khachHang);
                diaChiKhachHangRepository.save(diaChiKhachHang);
            }
        }

        return modelMapper.map(khachHang, KhachHangDTO.class);
    }



    @Override
    public void update(Integer id, KhachHangDTO khachHangDTO) {
        // Lấy khách hàng từ repository
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng có id: " + id));

        // Cập nhật thông tin từ khachHangDTO vào khachHang
        if (khachHangDTO.getMaKhachHang() != null) {
            khachHang.setMaKhachHang(khachHangDTO.getMaKhachHang());
        }
        if (khachHangDTO.getMatKhau() != null) {
            khachHang.setMatKhau(khachHangDTO.getMatKhau());
        }
        if (khachHangDTO.getHoVaTen() != null) {
            khachHang.setHoVaTen(khachHangDTO.getHoVaTen());
        }
        if (khachHangDTO.getEmail() != null) {
            khachHang.setEmail(khachHangDTO.getEmail());
        }
        khachHang.setGioiTinh(khachHangDTO.isGioiTinh());
        if (khachHangDTO.getSoDienThoai() != null) {
            khachHang.setSoDienThoai(khachHangDTO.getSoDienThoai());
        }
        if (khachHangDTO.getTrangThai() != null) {
            khachHang.setTrangThai(khachHangDTO.getTrangThai());
        }

        // Lưu thông tin khách hàng đã cập nhật vào cơ sở dữ liệu
        khachHangRepository.save(khachHang);

    }

    @Override
    public void delete(Integer id) {
    }

    @Override
    public KhachHangDTO findById(Integer id) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfound("Không tồn tại id: " + id));
        return modelMapper.map(khachHang, KhachHangDTO.class);
    }

    @Override
    public KhachHangDTO findBySoDienThoai(String soDienThoai) {
        KhachHang khachHang = khachHangRepository.findKhachHangBySoDienThoai(soDienThoai);
        return modelMapper.map(khachHang, KhachHangDTO.class);
    }

    @Override
    public Page<KhachHangDTO> getAll(Pageable pageable) {
        Page<KhachHang> khachHangs = khachHangRepository.findAll(pageable);
        return khachHangs.map(khachHang -> modelMapper.map(khachHang, KhachHangDTO.class));
    }

    @Override
    public Page<KhachHangDTO> searchAndFilter(String query, String status, String gender, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize); // Không trừ 1 vì page đã chỉnh ở controller

        Page<KhachHang> results;
        boolean genderBoolean = true;

        if ("false".equals(gender)) {
            genderBoolean = false;
        }

        if ("all".equals(status) && "all".equals(gender)) {
            results = khachHangRepository.searchByNamePhoneOrEmail(query, pageable);
        } else if ("all".equals(status)) {
            results = khachHangRepository.searchByNamePhoneOrEmailAndGender(query, genderBoolean, pageable);
        } else if ("all".equals(gender)) {
            results = khachHangRepository.searchByNamePhoneOrEmailAndStatus(query, status, pageable);
        } else {
            results = khachHangRepository.searchByNamePhoneOrEmailAndStatusAndGender(query, status, genderBoolean, pageable);
        }

        return results.map(khachHang -> modelMapper.map(khachHang, KhachHangDTO.class));
    }


    @Override
    public Page<KhachHangDTO> filter(String status, String gender, int page, int pageSize) {
        // Đảm bảo giá trị page không nhỏ hơn 0
        if (page < 0) {
            page = 0;
        }

        boolean genderBoolean = true; // Mặc định giới tính là true (Nam)

        if ("true".equals(gender)) {
            genderBoolean = true; // Nam
        } else if ("false".equals(gender)) {
            genderBoolean = false; // Nữ
        }

        Pageable pageable = PageRequest.of(page, pageSize);  // page đã được điều chỉnh

        Page<KhachHang> khachHangPage;

        if ("all".equals(status) && ("all".equals(gender) || gender == null)) {
            khachHangPage = khachHangRepository.findAll(pageable);
        } else if ("all".equals(status)) {
            khachHangPage = khachHangRepository.filterByGender(genderBoolean, pageable);
        } else if ("all".equals(gender)) {
            khachHangPage = khachHangRepository.filterByStatus(status, pageable);
        } else {
            khachHangPage = khachHangRepository.findByStatusAndGender(status, genderBoolean, pageable);
        }

        // Chuyển đổi từ Page<KhachHang> sang Page<KhachHangDTO>
        return khachHangPage.map(khachHang -> modelMapper.map(khachHang, KhachHangDTO.class));
    }


    @Override
    public Page<KhachHangDTO> searchActive(String query, String trangThai, Pageable pageable) {
        List<KhachHang> khachHangList = khachHangRepository.searchByNamePhoneOrEmailActive(query,trangThai,pageable);

        // Phân trang thủ công
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<KhachHang> list;

        if (khachHangList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, khachHangList.size());
            list = khachHangList.subList(startItem, toIndex);
        }

        List<KhachHangDTO> khachHangDTOList = list.stream()
                .map(khachHang -> modelMapper.map(khachHang, KhachHangDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(khachHangDTOList, pageable, khachHangList.size());
    }

    @Override
    public KhachHangDTO save2(KhachHangDTO khachHangDTO) {
        KhachHang khachHang = modelMapper.map(khachHangDTO,KhachHang.class);

        khachHang.setMaKhachHang(generateMaKhachHang());
        khachHang.setHoVaTen(khachHangDTO.getHoVaTen());
        khachHang.setSoDienThoai(khachHangDTO.getSoDienThoai());
        khachHang.setTrangThai("active");
        khachHang.setGioiTinh(true);

        KhachHang khachHangSave = khachHangRepository.save(khachHang);

        return modelMapper.map(khachHangSave, KhachHangDTO.class);
    }

    @Override
    public KhachHangDTO updateKhachHangByEmail(String email, KhachHangDTO khachHangDTO) {
        Optional<KhachHang> existingKhachHangOpt = khachHangRepository.findByEmail(email);
        if (!existingKhachHangOpt.isPresent()) {
            throw new EntityNotFoundException("Khách hàng không tìm thấy với email: " + email);
        }

        KhachHang existingKhachHang = existingKhachHangOpt.get();

        // Cập nhật các trường thông tin
        existingKhachHang.setHoVaTen(khachHangDTO.getHoVaTen());
        existingKhachHang.setGioiTinh(khachHangDTO.isGioiTinh());
        existingKhachHang.setSoDienThoai(khachHangDTO.getSoDienThoai());

        // Lưu KhachHang đã cập nhật
        KhachHang updatedKhachHang = khachHangRepository.save(existingKhachHang);

        // Chuyển đổi sang DTO và trả về
        return convertToDTO(updatedKhachHang);
    }

    private String generateMaKhachHang() {
        String PREFIX = "KH";
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int RANDOM_PART_LENGTH = 8; // Độ dài của phần ngẫu nhiên, để tổng độ dài là 10
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder(PREFIX);
        for (int i = 0; i < RANDOM_PART_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    private KhachHangDTO convertToDTO(KhachHang khachHang) {
        KhachHangDTO dto = new KhachHangDTO();
        dto.setId(khachHang.getId());
        dto.setMaKhachHang(khachHang.getMaKhachHang());
        dto.setMatKhau(khachHang.getMatKhau());
        dto.setHoVaTen(khachHang.getHoVaTen());
        dto.setEmail(khachHang.getEmail());
        dto.setGioiTinh(khachHang.isGioiTinh());
        dto.setSoDienThoai(khachHang.getSoDienThoai());
        dto.setTrangThai(khachHang.getTrangThai());
        dto.setCreatedAt(khachHang.getCreatedAt());
        // Giả sử bạn có phương thức để chuyển đổi danh sách địa chỉ (diaChi)
        return dto;
    }


    @Override
    public boolean isEmailExists(String email) {
        return khachHangRepository.existsByEmail(email);
    }

    @Override
    public boolean isSoDienThoaiExists(String soDienThoai) {
        return khachHangRepository.existsBySoDienThoai(soDienThoai);
    }

    @Override
    public boolean isMaKhachHangExists(String maKhachHang) {
        return khachHangRepository.existsByMaKhachHang(maKhachHang);
    }

}