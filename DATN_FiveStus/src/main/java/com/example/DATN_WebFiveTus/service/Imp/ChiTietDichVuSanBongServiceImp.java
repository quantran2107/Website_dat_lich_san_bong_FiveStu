package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.ChiTietDichVuSanBongDTO;
import com.example.DATN_WebFiveTus.repository.ChiTietDichVuSanBongRepository;
import com.example.DATN_WebFiveTus.service.ChiTietDichVuSanBongService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChiTietDichVuSanBongServiceImp implements ChiTietDichVuSanBongService {

    private ChiTietDichVuSanBongRepository chiTietDichVuSanBongRepository;

    private ModelMapper modelMapper;

    @Autowired
    public ChiTietDichVuSanBongServiceImp(ChiTietDichVuSanBongRepository chiTietDichVuSanBongRepository, ModelMapper modelMapper) {
        this.chiTietDichVuSanBongRepository = chiTietDichVuSanBongRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ChiTietDichVuSanBongDTO> getAll() {
        return chiTietDichVuSanBongRepository.findAll().stream()
                .map((chiTietDichVuSanBong ) ->modelMapper.map(chiTietDichVuSanBong,ChiTietDichVuSanBongDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ChiTietDichVuSanBongDTO getOne(Integer id) {
        return null;
    }

    @Override
    public ChiTietDichVuSanBongDTO save(ChiTietDichVuSanBongDTO chiTietDichVuSanBongDTO) {
        return null;
    }

    @Override
    public ChiTietDichVuSanBongDTO update(Integer id, ChiTietDichVuSanBongDTO chiTietDichVuSanBongDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
