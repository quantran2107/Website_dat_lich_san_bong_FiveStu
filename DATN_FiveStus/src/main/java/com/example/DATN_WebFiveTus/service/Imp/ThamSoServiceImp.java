package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.dto.ThamSoDTO;
import com.example.DATN_WebFiveTus.entity.SanCa;
import com.example.DATN_WebFiveTus.entity.ThamSo;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.ThamSoRepository;
import com.example.DATN_WebFiveTus.service.ThamSoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThamSoServiceImp implements ThamSoService {

    private ModelMapper modelMapper;

    private ThamSoRepository thamSoRepository;

    @Autowired
    public ThamSoServiceImp(ModelMapper modelMapper, ThamSoRepository thamSoRepository) {
        this.modelMapper = modelMapper;
        this.thamSoRepository = thamSoRepository;
    }

    @Override
    public List<ThamSoDTO> getAll() {
        return thamSoRepository.findAll().stream().map((thamSo) ->modelMapper.map(thamSo,ThamSoDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ThamSoDTO getOne(Integer id) {
        return modelMapper.map(thamSoRepository.findById(id).orElseThrow(()-> new ResourceNotfound("Không tồn tại ID tham số: "+id)),ThamSoDTO.class);
    }

    @Override
    public ThamSoDTO save(ThamSoDTO thamSoDTO) {
        ThamSo thamSo=modelMapper.map(thamSoDTO,ThamSo.class);
        thamSo.setTrangThai(true);
        ThamSo thamSoSave=thamSoRepository.save(thamSo);
        return modelMapper.map(thamSoSave,ThamSoDTO.class);
    }

    @Override
    public ThamSoDTO update(ThamSoDTO thamSoDTO, Integer id) {
        ThamSo thamSo=thamSoRepository.findById(id).orElseThrow(() -> new ResourceNotfound("Không tồn tại id update tham số: "+id));

        thamSo.setMa(thamSoDTO.getMa());
        thamSo.setTen(thamSoDTO.getTen());
        thamSo.setGiaTri(thamSoDTO.getGiaTri());
        thamSo.setTypeGiaTri(thamSoDTO.getTypeGiaTri());
        thamSo.setMoTa(thamSoDTO.getMoTa());
        thamSo.setTrangThai(thamSoDTO.isTrangThai());

        ThamSo thamSoUpdate=thamSoRepository.save(thamSo);

        return modelMapper.map(thamSoUpdate,ThamSoDTO.class);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public ThamSoDTO findByTenThamSo(String ma) {
        return modelMapper.map(thamSoRepository.findByTenThamSo(ma),ThamSoDTO.class);
    }

    @Override
    public List<ThamSoDTO> listAllSortPage(Integer pageNum, String sortDirection, int[] totalPageElement) {
        Sort sortS = Sort.by("ten");
        if (sortDirection.equalsIgnoreCase("asc")) {
            sortS = sortS.ascending();
        } else if (sortDirection.equalsIgnoreCase("desc")) {
            sortS = sortS.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, 5, sortS);
        Page<ThamSo> thamSoPage = thamSoRepository.findByPageThamSo(pageable);

        totalPageElement[0] = thamSoPage.getTotalPages();
        totalPageElement[1] = (int) thamSoPage.getTotalElements();

        return thamSoPage.getContent().stream()
                .map(thamSo -> modelMapper.map(thamSo, ThamSoDTO.class))
                .collect(Collectors.toList());
    }
}
