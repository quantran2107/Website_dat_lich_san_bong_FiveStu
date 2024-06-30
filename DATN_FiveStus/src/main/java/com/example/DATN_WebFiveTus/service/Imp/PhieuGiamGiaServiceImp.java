package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.PhieuGiamGiaRepository;
import com.example.DATN_WebFiveTus.service.PhieuGiamGiaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        // Convert DTO to entity
        PhieuGiamGia phieuGiamGia = modelMapper.map(phieuGiamGiaDTO, PhieuGiamGia.class);
        // Save entity to repository
        PhieuGiamGia savedEntity = phieuGiamGiaRepository.save(phieuGiamGia);
        // Convert saved entity back to DTO
        return modelMapper.map(savedEntity, PhieuGiamGiaDTO.class);
    }

    @Override
    public PhieuGiamGiaDTO update(Integer id, PhieuGiamGiaDTO phieuGiamGiaDTO) {
        PhieuGiamGia existingEntity = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá với id " + id));

        // Update fields from DTO to existing entity
        existingEntity.setMaPhieuGiamGia(phieuGiamGiaDTO.getMaPhieuGiamGia());
        existingEntity.setTenPhieuGiamGia(phieuGiamGiaDTO.getTenPhieuGiamGia());
        existingEntity.setMucGiam(phieuGiamGiaDTO.getMucGiam());
        // Update other fields accordingly

        // Save updated entity
        PhieuGiamGia updatedEntity = phieuGiamGiaRepository.save(existingEntity);

        // Convert updated entity back to DTO
        return modelMapper.map(updatedEntity, PhieuGiamGiaDTO.class);
    }

    @Override
    public Page<PhieuGiamGiaDTO> phanTrang(Pageable pageable) {
        Page<PhieuGiamGia> phieuGiamGiaPage = phieuGiamGiaRepository.getAllJoinFetch(pageable);
        return new PageImpl<>(
                phieuGiamGiaPage.getContent().stream()
                        .map(phieuGiamGia -> modelMapper.map(phieuGiamGia, PhieuGiamGiaDTO.class))
                        .collect(Collectors.toList())
                , pageable
                , phieuGiamGiaPage.getTotalElements());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        PhieuGiamGia entity = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá với id " + id));
        entity.setDeletedAt(true); // Cập nhật trạng thái xóa mềm
        phieuGiamGiaRepository.save(entity);
    }

}
