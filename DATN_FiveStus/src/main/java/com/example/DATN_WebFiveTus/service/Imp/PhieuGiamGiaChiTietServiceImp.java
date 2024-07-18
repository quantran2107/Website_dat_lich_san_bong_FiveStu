package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaChiTietDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGiaChiTiet;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.PhieuGiamGiaChiTietRepository;
import com.example.DATN_WebFiveTus.repository.PhieuGiamGiaRepository;
import com.example.DATN_WebFiveTus.service.PhieuGiamGiaChiTietService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaChiTietServiceImp implements PhieuGiamGiaChiTietService {

    private PhieuGiamGiaChiTietRepository phieuGiamGiaChiTietRepository;
    private ModelMapper modelMapper;

    @Autowired
    public PhieuGiamGiaChiTietServiceImp(PhieuGiamGiaChiTietRepository phieuGiamGiaChiTietRepository, ModelMapper modelMapper) {
        this.phieuGiamGiaChiTietRepository = phieuGiamGiaChiTietRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<PhieuGiamGiaChiTietDTO> getAll() {
        List<PhieuGiamGiaChiTiet> phieuGiamGiaChiTietList = phieuGiamGiaChiTietRepository.findAll();
        return phieuGiamGiaChiTietList.stream()
                .map(phieuGiamGiaChiTiet -> modelMapper.map(phieuGiamGiaChiTiet, PhieuGiamGiaChiTietDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PhieuGiamGiaChiTietDTO save(PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO) {
        // Convert DTO to entity
        PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = modelMapper.map(phieuGiamGiaChiTietDTO, PhieuGiamGiaChiTiet.class);
        // Save entity to repository
        PhieuGiamGiaChiTiet savedEntity = phieuGiamGiaChiTietRepository.save(phieuGiamGiaChiTiet);
        // Convert saved entity back to DTO
        return modelMapper.map(savedEntity, PhieuGiamGiaChiTietDTO.class);
    }

    @Override
    public PhieuGiamGiaChiTietDTO update(Integer id, PhieuGiamGiaChiTietDTO phieuGiamGiaChiTietDTO) {
        PhieuGiamGiaChiTiet existingEntity = phieuGiamGiaChiTietRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phiếu giảm giá ct với id " + id));
        PhieuGiamGiaChiTiet updatedEntity = phieuGiamGiaChiTietRepository.save(existingEntity);
        return modelMapper.map(updatedEntity, PhieuGiamGiaChiTietDTO.class);
    }

    @Override
    public PhieuGiamGiaChiTietDTO getOne(Integer id) {
        return modelMapper.map(phieuGiamGiaChiTietRepository.findById(id).orElseThrow(() ->
                new ResourceNotfound("Không tồn tại phieu giam gia ID: " + id)), PhieuGiamGiaChiTietDTO.class);
    }

    @Override
    public List<PhieuGiamGiaChiTietDTO> findByIdPGG(Integer idPhieuGiamGia) {
        List<PhieuGiamGiaChiTiet> phieuGiamGiaCTList = phieuGiamGiaChiTietRepository.findAllByIdPhieuGiamGia(idPhieuGiamGia);
        return phieuGiamGiaCTList.stream()
                .map(phieuGiamGiaChiTiet -> modelMapper.map(phieuGiamGiaChiTiet, PhieuGiamGiaChiTietDTO.class))
                .collect(Collectors.toList());
    }
}
