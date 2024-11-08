package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.entity.LoaiSan;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.LoaiSanRepository;
import com.example.DATN_WebFiveTus.service.LoaiSanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoaiSanServiceImp implements LoaiSanService {

    private LoaiSanRepository loaiSanRepository;

    private ModelMapper modelMapper;

    @Autowired
    public LoaiSanServiceImp(LoaiSanRepository loaiSanRepository, ModelMapper modelMapper) {
        this.loaiSanRepository = loaiSanRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<LoaiSanDTO> getAll() {
        return loaiSanRepository.findAll().stream()
                .map((loaiSan) -> modelMapper.map(loaiSan,LoaiSanDTO.class)).collect(Collectors.toList());
    }

    @Override
    public LoaiSanDTO getOne(Integer id) {
        LoaiSan loaiSan=loaiSanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfound("Không tồn tại id: "+id));
        if(loaiSan.isDeletedAt()==false){
            throw new ResourceNotfound("Ban ghi đã bị xoá: "+id);
        }
        return modelMapper.map(loaiSan,LoaiSanDTO.class);
    }

    @Override
    public LoaiSanDTO save(LoaiSanDTO loaiSanDTO) {
        LoaiSan loaiSan=modelMapper.map(loaiSanDTO,LoaiSan.class);
        loaiSan.setTrangThai("Hoạt động");
        LoaiSan loaiSanSave=loaiSanRepository.save(loaiSan);
        return modelMapper.map(loaiSanSave,LoaiSanDTO.class);
    }

    @Override
    public LoaiSanDTO update(Integer id, LoaiSanDTO loaiSanDTO) {
        LoaiSan loaiSan=loaiSanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfound("Không tồn tại id: "+id));
        loaiSan.setTenLoaiSan(loaiSanDTO.getTenLoaiSan());
        loaiSan.setTrangThai(loaiSanDTO.getTrangThai());
        LoaiSan loaiSanUpdate=loaiSanRepository.save(loaiSan);
        return modelMapper.map(loaiSanUpdate,LoaiSanDTO.class);
    }

    @Override
    public void delete(Integer id) {
        LoaiSan loaiSan=loaiSanRepository.findById(id).orElseThrow(()-> new ResourceNotfound("Không tồn tại ID loại sân bóng: "+id));
        loaiSanRepository.deleteById(id);
    }

    @Override
    public List<LoaiSanDTO> getAllJoinFetch() {
        return loaiSanRepository.getAllJoinFetch().stream()
                .map((loaiSan) -> modelMapper.map(loaiSan,LoaiSanDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deletedAt(Integer id) {
        LoaiSan loaiSan=loaiSanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfound("Không tồn tại id: "+id));
        loaiSanRepository.deletedAt(id);
    }

    @Override
    public Boolean existsByTenLoaiSan(String tenLoaiSan) {
        return loaiSanRepository.existsByTenLoaiSan(tenLoaiSan);
    }
}
