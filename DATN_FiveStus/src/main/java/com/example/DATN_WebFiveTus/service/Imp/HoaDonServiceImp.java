package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.HoaDonRepository;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.repository.PhieuGiamGiaRepository;
import com.example.DATN_WebFiveTus.service.HoaDonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoaDonServiceImp implements HoaDonService {

    private HoaDonRepository hoaDonRepository;

    private NhanVienReposity nhanVienReposity;

    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    private KhachHangRepository khachHangRepository;

    private ModelMapper modelMapper;

    @Autowired
    public HoaDonServiceImp(HoaDonRepository hoaDonRepository, NhanVienReposity nhanVienReposity, PhieuGiamGiaRepository phieuGiamGiaRepository, KhachHangRepository khachHangRepository, ModelMapper modelMapper) {
        this.hoaDonRepository = hoaDonRepository;
        this.nhanVienReposity = nhanVienReposity;
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.khachHangRepository = khachHangRepository;
        this.modelMapper = modelMapper;
    }



    @Override
    public List<HoaDonDTO> getAll() {
        return hoaDonRepository.findAll().stream()
                .map((hoaDon) -> modelMapper.map(hoaDon, HoaDonDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<HoaDonDTO> getAllJoinFetch() {
        return hoaDonRepository.getAllJoinFetch().stream()
                .map((hoaDon) -> modelMapper.map(hoaDon,HoaDonDTO.class)).collect(Collectors.toList());
    }

    @Override
    public HoaDonDTO getOne(Integer id) {
        return modelMapper.map(hoaDonRepository.findById(id).orElseThrow(()->new ResourceNotfound("Không tồn tại id: "+id)),HoaDonDTO.class);
    }

    @Override
    public HoaDonDTO save(HoaDonDTO hoaDonDTO) {
        return null;
    }

    @Override
    public HoaDonDTO update(Integer id, HoaDonDTO hoaDonDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void deletedAt(Integer id) {

    }

    @Override
    public Page<HoaDonDTO> phanTrang(Pageable pageable) {
        Page<HoaDon> hoaDonPage = hoaDonRepository.findAll(pageable);
        List<HoaDonDTO> list = hoaDonPage.getContent().stream()
                .map(hoaDon -> modelMapper.map(hoaDon, HoaDonDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, hoaDonPage.getTotalElements());
    }
}
