package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.stream.Collectors;

import com.example.DATN_WebFiveTus.dto.DiaChiKhachHangDTO;
import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.entity.DiaChiKhachHang;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.DiaChiKhachHangRepository;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
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
    public Page<KhachHangDTO> getAll(Pageable pageable) {
        Page<KhachHang> khachHangs = khachHangRepository.findAll(pageable);
        return khachHangs.map(khachHang -> modelMapper.map(khachHang, KhachHangDTO.class));
    }

    @Override
    public List<KhachHangDTO> search(String query, int page, int pageSize) {
        Pageable pageable = PageRequest.of(page - 1, pageSize);
        Page<KhachHang> results = khachHangRepository.searchByNamePhoneOrEmail(query, pageable);

        return results.stream()
                .map(khachHang -> modelMapper.map(khachHang, KhachHangDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public List<KhachHangDTO> filter(String status, String gender, int page, int pageSize) {
        List<KhachHang> khachHangs;
        boolean genderBoolean;

        if ("true".equals(gender)) {
            genderBoolean = true; // Nam
        } else if ("false".equals(gender)) {
            genderBoolean = false; // Nữ
        } else {
            // Trường hợp "all", không cần lọc theo giới tính
            genderBoolean = true; // Mặc định, không lọc theo giới tính
        }

        Pageable pageable = PageRequest.of(page - 1, pageSize);

        if ("all".equals(status) && ("all".equals(gender) || gender == null)) {
            khachHangs = khachHangRepository.findAll(pageable).getContent();
        } else if ("all".equals(status)) {
            khachHangs = khachHangRepository.filterByGender(genderBoolean, pageable).getContent();
        } else if ("all".equals(gender)) {
            khachHangs = khachHangRepository.filterByStatus(status, pageable).getContent();
        } else {
            khachHangs = khachHangRepository.findByStatusAndGender(status, genderBoolean, pageable).getContent();
        }

        return khachHangs.stream()
                .map(khachHang -> modelMapper.map(khachHang, KhachHangDTO.class))
                .collect(Collectors.toList());
    }

}