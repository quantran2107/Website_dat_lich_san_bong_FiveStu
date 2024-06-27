package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.DichVuSanBongDTO;
import com.example.DATN_WebFiveTus.repository.DichVuSanBongRepository;
import com.example.DATN_WebFiveTus.service.DichVuSanBongService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DichVuSanBongServiceImp implements DichVuSanBongService {

    private DichVuSanBongRepository dichVuSanBongRepository;

    private ModelMapper modelMapper;



    public DichVuSanBongServiceImp(DichVuSanBongRepository dichVuSanBongRepository, ModelMapper modelMapper) {
        this.dichVuSanBongRepository = dichVuSanBongRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DichVuSanBongDTO> getAll() {
        return dichVuSanBongRepository.findAll().stream()
                .map((dichVuSanBong) -> modelMapper.map(dichVuSanBong,DichVuSanBongDTO.class)).collect(Collectors.toList());
    }

    @Override
    public DichVuSanBongDTO getOne(Integer id) {
        return null;
    }

    @Override
    public DichVuSanBongDTO save(DichVuSanBongDTO dichVuSanBongDTO) {
        return null;
    }

    @Override
    public DichVuSanBongDTO update(Integer id, DichVuSanBongDTO dichVuSanBongDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
