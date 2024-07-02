package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.HoaDonChiTietRepository;
import com.example.DATN_WebFiveTus.repository.HoaDonRepository;
import com.example.DATN_WebFiveTus.repository.SanCaRepository;
import com.example.DATN_WebFiveTus.service.HoaDonChiTietService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoaDonChiTietServiceImp implements HoaDonChiTietService {

    private HoaDonChiTietRepository hoaDonChiTietRepository;

    private HoaDonRepository hoaDonRepository;

    private SanCaRepository sanCaRepository;

    private ModelMapper modelMapper;

    @Autowired
    public HoaDonChiTietServiceImp(HoaDonChiTietRepository hoaDonChiTietRepository, HoaDonRepository hoaDonRepository, SanCaRepository sanCaRepository, ModelMapper modelMapper) {
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.sanCaRepository = sanCaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<HoaDonChiTietDTO> getAll() {
        return hoaDonChiTietRepository.findAll().stream()
                .map((hoaDonChiTiet) -> modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<HoaDonChiTietDTO> getAllJoinFetch() {
        return hoaDonChiTietRepository.getAllJoinFetch().stream()
                .map((hoaDonChiTiet) -> modelMapper.map(hoaDonChiTiet,HoaDonChiTietDTO.class)).collect(Collectors.toList());
    }

    @Override
    public HoaDonChiTietDTO getOne(Integer id) {
        return modelMapper.map(hoaDonChiTietRepository.findById(id).orElseThrow(()->
                new ResourceNotfound("Không tồn tại id: "+id)),HoaDonChiTietDTO.class);
    }

    @Override
    public HoaDonChiTietDTO save(HoaDonChiTietDTO hoaDonChiTietDTO) {
        return null;
    }

    @Override
    public HoaDonChiTietDTO update(Integer id, HoaDonChiTietDTO hoaDonChiTietDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void deletedAt(Integer id) {

    }

    @Override
    public List<HoaDonChiTietDTO> searchFromHoaDon(Integer idHoaDon) {
        return hoaDonChiTietRepository.searchFromHoaDon(idHoaDon).stream()
                .map((hoaDonChiTiet) -> modelMapper.map(hoaDonChiTiet,HoaDonChiTietDTO.class)).collect(Collectors.toList());
    }
}
