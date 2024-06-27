package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.NgayTrongTuanDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.entity.NgayTrongTuan;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.NgayTrongTuanRepository;
import com.example.DATN_WebFiveTus.service.NgayTrongTuanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NgayTrongTuanServiceImp implements NgayTrongTuanService {

    private NgayTrongTuanRepository ngayTrongTuanRepository;

    private ModelMapper modelMapper;

    @Autowired
    public NgayTrongTuanServiceImp(NgayTrongTuanRepository ngayTrongTuanRepository, ModelMapper modelMapper) {
        this.ngayTrongTuanRepository = ngayTrongTuanRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<NgayTrongTuanDTO> getAll() {
        return ngayTrongTuanRepository.findAll().stream().map((ngayTrongTuan) ->modelMapper
                .map(ngayTrongTuan,NgayTrongTuanDTO.class)).collect(Collectors.toList());
    }

    @Override
    public NgayTrongTuanDTO getOne(Integer id) {
        return modelMapper.map(ngayTrongTuanRepository.findById(id).orElseThrow(()
                -> new ResourceNotfound("Không tồn tại ngày trong tuần ID: "+id)), NgayTrongTuanDTO.class);
    }

    @Override
    public NgayTrongTuanDTO save(NgayTrongTuanDTO ngayTrongTuanDTO) {
        NgayTrongTuan ngayTrongTuan=modelMapper.map(ngayTrongTuanDTO,NgayTrongTuan.class);
        NgayTrongTuan ngayTrongTuanSave=ngayTrongTuanRepository.save(ngayTrongTuan);
        return modelMapper.map(ngayTrongTuanSave,NgayTrongTuanDTO.class);
    }

    @Override
    public NgayTrongTuanDTO update(Integer id, NgayTrongTuanDTO ngayTrongTuanDTO) {
        NgayTrongTuan ngayTrongTuan=ngayTrongTuanRepository.findById(id).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại ngày trong tuần: "+id));
        return null;
    }

    @Override
    public void delete(Integer id) {

    }
}
