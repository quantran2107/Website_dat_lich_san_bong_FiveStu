package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.DiaChiDTO;
import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.repository.DiaChiRepository;
import com.example.DATN_WebFiveTus.service.DiaChiService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaChiImp implements DiaChiService {

    private DiaChiRepository diaChiRepository;

    private ModelMapper modelMapper;

    @Autowired
    public DiaChiImp(DiaChiRepository diaChiRepository, ModelMapper modelMapper) {

        this.diaChiRepository = diaChiRepository;

        this.modelMapper = modelMapper;
    }

    @Override
    public List<DiaChiDTO> getAll() {
        return null;
    }

    @Override
    public DiaChiDTO getOne(Integer id) {
        return null;
    }

    @Override
    public DiaChiDTO save(DiaChiDTO diaChiDTO) {
        return null;
    }

    @Override
    public DiaChiDTO update(Integer id, DiaChiDTO diaChiDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<DiaChiDTO> getAllJoinFetch() {
        return diaChiRepository.getAllJoinFetch().stream()
                .map((diaChi) -> modelMapper.map(diaChi, DiaChiDTO.class)).collect(Collectors.toList());

    }
}
