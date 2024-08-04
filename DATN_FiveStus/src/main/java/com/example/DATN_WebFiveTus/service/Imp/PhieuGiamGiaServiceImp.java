package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.PhieuGiamGiaRepository;
import com.example.DATN_WebFiveTus.service.PhieuGiamGiaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaServiceImp implements PhieuGiamGiaService {
    private PhieuGiamGiaRepository phieuGiamGiaRepository;
    private ModelMapper modelMapper;

    @Autowired
    public PhieuGiamGiaServiceImp(PhieuGiamGiaRepository phieuGiamGiaRepository, ModelMapper modelMapper) {
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PhieuGiamGiaDTO> phanTrang(Pageable pageable) {
        Page<PhieuGiamGia> trangPhieuGiamGia = phieuGiamGiaRepository.getAllJoinFetch(pageable);
        List<PhieuGiamGiaDTO> danhSachDTO = trangPhieuGiamGia.getContent().stream()
                .map(phieuGiamGia -> modelMapper.map(phieuGiamGia, PhieuGiamGiaDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(danhSachDTO, pageable, trangPhieuGiamGia.getTotalElements());
    }


    @Override
    public List<PhieuGiamGiaDTO> getAll() {
        List<PhieuGiamGia> phieuGiamGiaList = phieuGiamGiaRepository.findAll();
        return phieuGiamGiaList.stream()
                .map(phieuGiamGia -> modelMapper.map(phieuGiamGia, PhieuGiamGiaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PhieuGiamGiaDTO getOne(Integer id) {
        return modelMapper.map(phieuGiamGiaRepository.findById(id).orElseThrow(() ->
                new ResourceNotfound("Không tồn tại phieu giam gia ID: " + id)), PhieuGiamGiaDTO.class);
    }

    @Override
    public PhieuGiamGiaDTO save(PhieuGiamGiaDTO phieuGiamGiaDTO) {
        PhieuGiamGia phieuGiamGia = modelMapper.map(phieuGiamGiaDTO, PhieuGiamGia.class);

        // Kiểm tra nếu mã phiếu giảm giá không được điền
        if (phieuGiamGia.getMaPhieuGiamGia().trim() == null || phieuGiamGia.getMaPhieuGiamGia().trim().isEmpty()) {
            // Tự động tạo mã phiếu giảm giá mới
            phieuGiamGia.setMaPhieuGiamGia(generateMaPhieuGiamGia());
        }
        if (phieuGiamGia.getSoLuong() == null ) {
            phieuGiamGia.setSoLuong(Integer.MAX_VALUE); // Set số lượng là dương vô cùng
        }

        phieuGiamGia.setDeletedAt(false); // Đặt giá trị deletedAt trước khi lưu
        PhieuGiamGia savedEntity = phieuGiamGiaRepository.save(phieuGiamGia);
        return modelMapper.map(savedEntity, PhieuGiamGiaDTO.class);
    }


    private String generateMaPhieuGiamGia() {
        String PREFIX = "PGG";
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int RANDOM_PART_LENGTH = 7; // Độ dài của phần ngẫu nhiên, để tổng độ dài là 10
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder(PREFIX);
        for (int i = 0; i < RANDOM_PART_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }



    @Override
    public PhieuGiamGiaDTO update(Integer id, PhieuGiamGiaDTO phieuGiamGiaDTO) {
        PhieuGiamGia existingEntity = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá với id " + id));
        // Save updated entity
        PhieuGiamGia updatedEntity = phieuGiamGiaRepository.save(existingEntity);

        // Convert updated entity back to DTO
        return modelMapper.map(updatedEntity, PhieuGiamGiaDTO.class);
    }

    @Override
    @Transactional
    public void updateStatus(Integer id, String newStatus) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu giảm giá với id " + id));
        phieuGiamGia.setTrangThai(newStatus); // Cập nhật trạng thái mới
        phieuGiamGiaRepository.save(phieuGiamGia); // Lưu lại vào cơ sở dữ liệu
    }

    @Override
    public Page<PhieuGiamGiaDTO> searchPhieuGiamGia(String keyword, Boolean doiTuongApDung, Boolean hinhThucGiamGia, String trangThai, Date ngayBatDau, Date ngayKetThuc, Pageable pageable) {
        List<PhieuGiamGia> phieuGiamGiaList = phieuGiamGiaRepository.searchPhieuGiamGia(keyword, doiTuongApDung, hinhThucGiamGia, trangThai, ngayBatDau, ngayKetThuc, pageable);

        List<PhieuGiamGiaDTO> phieuGiamGiaDTOList = phieuGiamGiaList.stream()
                .map(phieuGiamGia -> modelMapper.map(phieuGiamGia, PhieuGiamGiaDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(phieuGiamGiaDTOList, pageable, phieuGiamGiaRepository.count());
    }

}

