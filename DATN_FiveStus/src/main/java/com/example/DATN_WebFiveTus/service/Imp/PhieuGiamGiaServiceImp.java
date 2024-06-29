package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.example.DATN_WebFiveTus.repository.PhieuGiamGiaRepository;
import com.example.DATN_WebFiveTus.service.PhieuGiamGiaService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaServiceImp implements PhieuGiamGiaService {
    private PhieuGiamGiaRepository phieuGiamGiaRepository;
    private ModelMapper modelMapper;

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
        return null;
    }

    @Override
    public PhieuGiamGiaDTO save(PhieuGiamGiaDTO phieuGiamGiaDTO) {
        return null;
    }

    @Override
    public PhieuGiamGiaDTO update(Integer id, PhieuGiamGiaDTO phieuGiamGiaDTO) {
        return null;
    }

    @Override
    public Page<PhieuGiamGiaDTO> phanTrang(Pageable pageable) {
        Page<PhieuGiamGia> phieuGiamGiaPage = phieuGiamGiaRepository.findAll(pageable);
        List<PhieuGiamGiaDTO> list = phieuGiamGiaPage.getContent().stream()
                .map(phieuGiamGia -> modelMapper.map(phieuGiamGia, PhieuGiamGiaDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(
                phieuGiamGiaPage.getContent().stream()
                        .map(phieuGiamGia -> modelMapper.map(phieuGiamGia, PhieuGiamGiaDTO.class))
                        .collect(Collectors.toList())
                , pageable
                , phieuGiamGiaPage.getTotalElements());
    }

    @Override
    public void delete(Integer id) {

    }
}
