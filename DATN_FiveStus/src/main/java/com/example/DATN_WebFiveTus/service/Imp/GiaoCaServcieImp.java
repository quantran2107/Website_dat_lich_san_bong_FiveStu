package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.GiaoCaDTO;
import com.example.DATN_WebFiveTus.entity.GiaoCa;
import com.example.DATN_WebFiveTus.repository.GiaoCaRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.service.GiaoCaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class GiaoCaServcieImp implements GiaoCaService {


    private GiaoCaRepository giaoCaRepository;

    private NhanVienReposity nhanVienReposity;

    private ModelMapper modelMapper;

    @Autowired
    public GiaoCaServcieImp(GiaoCaRepository giaoCaRepository, NhanVienReposity nhanVienReposity, ModelMapper modelMapper) {
        this.giaoCaRepository = giaoCaRepository;
        this.nhanVienReposity = nhanVienReposity;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<GiaoCaDTO> getAll() {
        return giaoCaRepository.findAll().stream().map((giaoCa) -> modelMapper.map(giaoCa, GiaoCaDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void save(GiaoCaDTO giaoCaDTO) {
        GiaoCa giaoCa = modelMapper.map(giaoCaDTO, GiaoCa.class);
        giaoCa.setThoiGianKetCa(LocalDateTime.now());
        giaoCaRepository.save(giaoCa);
    }
}
