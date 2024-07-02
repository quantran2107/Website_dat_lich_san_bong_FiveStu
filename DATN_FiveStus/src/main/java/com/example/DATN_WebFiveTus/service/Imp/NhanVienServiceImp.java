package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.service.NhanVienService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
public class NhanVienServiceImp implements NhanVienService {

    private NhanVienReposity nhanVienReposity;
    private ModelMapper modelMapper;

    public NhanVienServiceImp(NhanVienReposity nhanVienReposity, ModelMapper modelMapper) {
        this.nhanVienReposity = nhanVienReposity;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<NhanVienDTO> getAll() {
        return nhanVienReposity.findAll().stream().map((nhanVien) ->modelMapper
                .map(nhanVien, NhanVienDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void save(NhanVienDTO nhanVienDTO) {
        NhanVien nhanVien = modelMapper.map(nhanVienDTO, NhanVien.class);
        nhanVienReposity.save(nhanVien);
    }

    @Override
    public void updateNV(int id, NhanVienDTO nhanVienDTO) {
        NhanVien nhanVien = nhanVienReposity.getReferenceById(id);
        modelMapper.map(nhanVienDTO, nhanVien);
        nhanVienReposity.save(nhanVien);
    }


}
