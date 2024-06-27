package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.NuocUongDTO;
import com.example.DATN_WebFiveTus.entity.NuocUong;
import com.example.DATN_WebFiveTus.repository.NuocUongRepository;
import com.example.DATN_WebFiveTus.service.NuocUongService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NuocUongServiceImp implements NuocUongService {
    private NuocUongRepository nuocUongRepository;
    private ModelMapper modelMapper;

    public NuocUongServiceImp(NuocUongRepository nuocUongRepository, ModelMapper modelMapper) {
        this.nuocUongRepository = nuocUongRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<NuocUongDTO> getAll() {
        List<NuocUong> nuocUongList = nuocUongRepository.findAll();
        return nuocUongList.stream()
                .map(nuocUong -> modelMapper.map(nuocUong, NuocUongDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public NuocUongDTO getOne(Integer id) {
        return null;
    }

    @Override
    public NuocUongDTO save(NuocUongDTO nuocUongDTO) {
        return null;
    }

    @Override
    public NuocUongDTO update(Integer id, NuocUongDTO nuocUongDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
