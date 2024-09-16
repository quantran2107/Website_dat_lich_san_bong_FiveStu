package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.entity.Ca;
import com.example.DATN_WebFiveTus.entity.SanBong;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.CaRepository;
import com.example.DATN_WebFiveTus.service.CaService;
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
public class CaServiceImp implements CaService {

    private CaRepository caRepository;

    private ModelMapper modelMapper;

    @Autowired
    public CaServiceImp(CaRepository caRepository, ModelMapper modelMapper) {
        this.caRepository = caRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CaDTO> getAll() {
        return caRepository.findAll().stream()
                .map((ca) -> modelMapper.map(ca,CaDTO.class)).collect(Collectors.toList());
    }

    @Override
    public CaDTO getOne(Integer id) {
        return modelMapper.map(caRepository.findById(id).orElseThrow(()
                -> new ResourceNotfound("Không tồn tại ca id: "+id)),CaDTO.class);
    }

    @Override
    public CaDTO save(CaDTO caDTO) {
        Ca ca=modelMapper.map(caDTO,Ca.class);
        ca.setTrangThai("Đang hoạt động");
        Ca caSave=caRepository.save(ca);
        return modelMapper.map(caSave,CaDTO.class);
    }

    @Override
    public CaDTO update(Integer id, CaDTO caDTO) {
        Ca ca=caRepository.findById(id).orElseThrow(()
                -> new ResourceNotfound("Không tồn tại ca id: "+id));
        ca.setTenCa(caDTO.getTenCa());
        ca.setTrangThai(caDTO.getTrangThai());
        ca.setThoiGianBatDau(caDTO.getThoiGianBatDau());
        ca.setThoiGianKetThuc(caDTO.getThoiGianKetThuc());
        Ca caUpdate=caRepository.save(ca);
        return modelMapper.map(caUpdate,CaDTO.class);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void deletedAt(Integer id) {
        Ca ca=caRepository.findById(id).orElseThrow(()->new ResourceNotfound("Không tồn tạo id: "+id));
        caRepository.deletedAt(id);
    }

    @Override
    public List<CaDTO> getAllJoinFetch() {
        return caRepository.getAllJoinFetch().stream()
                .map((ca) -> modelMapper.map(ca,CaDTO.class)).collect(Collectors.toList());
    }


    @Override
    public List<CaDTO> listAllSortPage(Integer pageNum, String sortDirection, int[] totalPageElement) {
        Sort sortS = Sort.by("tenCa");
        if (sortDirection.equalsIgnoreCase("asc")) {
            sortS = sortS.ascending();
        } else if (sortDirection.equalsIgnoreCase("desc")) {
            sortS = sortS.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, 5, sortS);
        Page<Ca> caPage = caRepository.findByCaPage(pageable);

        totalPageElement[0] = caPage.getTotalPages();
        totalPageElement[1] = (int) caPage.getTotalElements();

        return caPage.getContent().stream()
                .map(ca -> modelMapper.map(ca, CaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CaDTO> searchKeyWords(Integer pageNum, String keyWords, String sortDirection, int[] totalPageElement, Integer id) {
        Sort sortS = Sort.by("tenCa");
        if (sortDirection.equalsIgnoreCase("asc")) {
            sortS = sortS.ascending();
        } else if (sortDirection.equalsIgnoreCase("desc")) {
            sortS = sortS.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, 5, sortS);
        Page<Ca> caPage = caRepository.search(id,keyWords.trim(),pageable);

        totalPageElement[0] = caPage.getTotalPages();
        totalPageElement[1] = (int) caPage.getTotalElements();

        return caPage.getContent().stream()
                .map(ca -> modelMapper.map(ca, CaDTO.class))
                .collect(Collectors.toList());
    }
}
