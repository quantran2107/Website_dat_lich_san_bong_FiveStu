package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.entity.Ca;
import com.example.DATN_WebFiveTus.entity.NgayTrongTuan;
import com.example.DATN_WebFiveTus.entity.SanBong;
import com.example.DATN_WebFiveTus.entity.SanCa;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.CaRepository;
import com.example.DATN_WebFiveTus.repository.NgayTrongTuanRepository;
import com.example.DATN_WebFiveTus.repository.SanBongRepository;
import com.example.DATN_WebFiveTus.repository.SanCaRepository;
import com.example.DATN_WebFiveTus.service.SanCaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SanCaServiceImp implements SanCaService {

    private SanCaRepository sanCaRepository;

    private NgayTrongTuanRepository ngayTrongTuanRepository;

    private SanBongRepository sanBongRepository;

    private CaRepository caRepository;

    private ModelMapper modelMapper;

    @Autowired
    public SanCaServiceImp(SanCaRepository sanCaRepository, NgayTrongTuanRepository ngayTrongTuanRepository,
                           SanBongRepository sanBongRepository, CaRepository caRepository, ModelMapper modelMapper) {
        this.sanCaRepository = sanCaRepository;
        this.ngayTrongTuanRepository = ngayTrongTuanRepository;
        this.sanBongRepository = sanBongRepository;
        this.caRepository = caRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SanCaDTO> getAll() {
        return sanCaRepository.findAll().stream()
                .map((sanCa) -> modelMapper.map(sanCa,SanCaDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<SanCaDTO> getAllJoinFetch() {
        return sanCaRepository.getAllJoinFetch().stream()
                .map((sanCa) -> modelMapper.map(sanCa,SanCaDTO.class)).collect(Collectors.toList());
    }

    @Override
    public SanCaDTO getOne(Integer id) {
        return modelMapper.map(sanCaRepository.findById(id).orElseThrow(()->new ResourceNotfound("Không tồn tại id: "+id)),SanCaDTO.class);
    }

    @Override
    public SanCaDTO save(SanCaDTO sanCaDTO) {
        Ca ca=caRepository.findById(sanCaDTO.getIdCa()).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại id ca: "+sanCaDTO.getIdCa()));

        SanBong sanBong=sanBongRepository.findById(sanCaDTO.getIdSanBong()).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại id sân bóng: "+sanCaDTO.getIdSanBong()));

        NgayTrongTuan ngayTrongTuan=ngayTrongTuanRepository.findById(sanCaDTO.getIdNgayTrongTuan()).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại id ngày trong tuần: "+sanCaDTO.getIdNgayTrongTuan()));


        SanCa sanCa=modelMapper.map(sanCaDTO,SanCa.class);
        sanCa.setCa(ca);
        sanCa.setSanBong(sanBong);
        sanCa.setNgayTrongTuan(ngayTrongTuan);
        SanCa sanCaSave=sanCaRepository.save(sanCa);
        return modelMapper.map(sanCaSave,SanCaDTO.class);
    }

    @Override
    public SanCaDTO update(Integer id, SanCaDTO sanCaDTO) {
        SanCa sanCa=sanCaRepository.findById(id).orElseThrow(()-> new ResourceNotfound("Không tồn tại update id: "+id));
        Ca ca=caRepository.findById(sanCaDTO.getIdCa()).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại id ca: "+sanCaDTO.getIdCa()));

        SanBong sanBong=sanBongRepository.findById(sanCaDTO.getIdSanBong()).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại id sân bóng: "+sanCaDTO.getIdSanBong()));

        NgayTrongTuan ngayTrongTuan=ngayTrongTuanRepository.findById(sanCaDTO.getIdNgayTrongTuan()).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại id ngày trong tuần: "+sanCaDTO.getIdNgayTrongTuan()));
        sanCa.setCa(ca);
        sanCa.setSanBong(sanBong);
        sanCa.setNgayTrongTuan(ngayTrongTuan);
        sanCa.setTrangThai(sanCaDTO.getTrangThai());
        SanCa sanCaUpdate=sanCaRepository.save(sanCa);
        return modelMapper.map(sanCaUpdate,SanCaDTO.class);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void deletedAt(Integer id) {
        SanCa sanCa=sanCaRepository.findById(id).orElseThrow(()-> new ResourceNotfound("Không tồn tại xoá id: "+id));
        sanCaRepository.deletedAt(id);
    }
}
