package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.entity.SanBong;
import com.example.DATN_WebFiveTus.entity.SanCa;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.SanBongRepository;
import com.example.DATN_WebFiveTus.service.SanBongService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SanBongServiceImp implements SanBongService {

    private SanBongRepository sanBongRepository;

    private ModelMapper modelMapper;

    @Autowired
    public SanBongServiceImp(SanBongRepository sanBongRepository, ModelMapper modelMapper) {
        this.sanBongRepository = sanBongRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SanBongDTO> getAll() {
        return sanBongRepository.findAll().stream().map((sanBong) ->modelMapper
                .map(sanBong,SanBongDTO.class)).collect(Collectors.toList());
    }

    @Override
    public SanBongDTO getOne(Integer id) {
        SanBong sanBong=sanBongRepository.findById(id).orElseThrow(() -> new ResourceNotfound("Không tồn tại sân bóng ID: "+id));
        if(sanBong.isDeletedAt()==false){
            throw new ResourceNotfound("Bản ghi đã được xoá: "+id);
        }
        return modelMapper.map(sanBong,SanBongDTO.class);
    }

    @Override
    public SanBongDTO save(SanBongDTO sanBongDTO) {
        SanBong sanBong=modelMapper.map(sanBongDTO,SanBong.class);
        SanBong sanBongSave=sanBongRepository.save(sanBong);
        return modelMapper.map(sanBongSave,SanBongDTO.class);
    }

    @Override
    public SanBongDTO update(Integer id, SanBongDTO sanBongDTO) {
        SanBong sanBong=sanBongRepository.findById(id).orElseThrow(() -> new ResourceNotfound("Không tồn tại sân bóng ID: "+id));
        sanBong.setTenSanBong(sanBongDTO.getTenSanBong());
        sanBong.setTrangThai(sanBongDTO.getTrangThai());
        SanBong sanBongUpdate=sanBongRepository.save(sanBong);
        return modelMapper.map(sanBongUpdate,SanBongDTO.class);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<SanBongDTO> getAllJoinFetch() {
        return sanBongRepository.getAllJoinFetch().stream().map((sanBong) ->modelMapper
                .map(sanBong,SanBongDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deletedAt(Integer id) {
        SanBong sanBong=sanBongRepository.findById(id).orElseThrow(()-> new ResourceNotfound("Không tồn tại xoá id: "+id));
        sanBongRepository.deletedAt(id);
    }
}
