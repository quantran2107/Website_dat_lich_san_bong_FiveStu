package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.DiaChiDTO;
import com.example.DATN_WebFiveTus.dto.DoThueDTO;
import com.example.DATN_WebFiveTus.dto.NuocUongDTO;
import com.example.DATN_WebFiveTus.entity.DoThue;
import com.example.DATN_WebFiveTus.entity.NuocUong;
import com.example.DATN_WebFiveTus.repository.DoThueRepository;
import com.example.DATN_WebFiveTus.service.DoThueService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoThueServiceImp implements DoThueService {
    private  DoThueRepository doThueRepository;

    private ModelMapper modelMapper;

    public DoThueServiceImp(DoThueRepository doThueRepository, ModelMapper modelMapper) {
        this.doThueRepository = doThueRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DoThueDTO> getAll() {
        List<DoThue> doThues = doThueRepository.findAll();
        return doThues.stream()
                .map(dothue -> modelMapper.map(dothue, DoThueDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public DoThueDTO getOne(Integer id) {
        return null;
    }

    @Override
    public DoThueDTO save(DoThueDTO doThueDTO) {
        return null;
    }

    @Override
    public DoThueDTO update(Integer id, DoThueDTO doThueDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
