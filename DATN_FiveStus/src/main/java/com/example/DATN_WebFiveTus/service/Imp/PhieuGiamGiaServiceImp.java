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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        PhieuGiamGia savedEntity = phieuGiamGiaRepository.save(phieuGiamGia);
        savedEntity.setDeletedAt(false);
        return modelMapper.map(savedEntity, PhieuGiamGiaDTO.class);
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


    //    @Override
//    @Transactional
//    public void delete(Integer id) {
//        PhieuGiamGia entity = phieuGiamGiaRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá với id " + id));
//        entity.setDeletedAt(true); // Cập nhật trạng thái xóa mềm
//        phieuGiamGiaRepository.save(entity);
//    }
//

//
//    @Override
//    public void deleteMultiple(List<Integer> ids) {
//        List<PhieuGiamGia> phieuGiamGiaList = phieuGiamGiaRepository.findAllById(ids);
//        phieuGiamGiaList.stream()
//                .map(phieuGiamGia -> modelMapper.map(phieuGiamGia, PhieuGiamGiaDTO.class))
//                .collect(Collectors.toList()).forEach(phieuGiamGiaDTO -> phieuGiamGiaDTO.setDeletedAt(true));
//        phieuGiamGiaRepository.saveAll(phieuGiamGiaList);
//    }

//    @Override
//    public List<PhieuGiamGiaDTO> search(String query) {
//        List<PhieuGiamGia> results = phieuGiamGiaRepository.searchByNameOrCode(query);
//        return results.stream()
//                .map(phieuGiamGia -> modelMapper.map(phieuGiamGia, PhieuGiamGiaDTO.class))
//                .collect(Collectors.toList());
//    }

//    @Override
//    public List<PhieuGiamGiaDTO> filter(String status) {
//        if ("all".equals(status)) {
//            return phieuGiamGiaRepository.findAll().stream()
//                    .map(phieuGiamGia -> modelMapper.map(phieuGiamGia, PhieuGiamGiaDTO.class))
//                    .collect(Collectors.toList());
//        } else {
//            boolean isActive = "true".equals(status);
//            return phieuGiamGiaRepository.filterByStatus(isActive).stream()
//                    .map(phieuGiamGia -> modelMapper.map(phieuGiamGia, PhieuGiamGiaDTO.class))
//                    .collect(Collectors.toList());
//        }
//    }

}

