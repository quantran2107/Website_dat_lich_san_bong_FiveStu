package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.ThamSoDTO;
import com.example.DATN_WebFiveTus.entity.SanBong;
import com.example.DATN_WebFiveTus.entity.ThamSo;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.ThamSoRepository;
import com.example.DATN_WebFiveTus.service.ThamSoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    public List<ThamSoDTO> getAll2() {
        return thamSoRepository.getAll2().stream().map((thamSo) ->modelMapper.map(thamSo,ThamSoDTO.class)).collect(Collectors.toList());
    }

    @Override
    public ThamSoDTO getOne(Integer id) {
        return modelMapper.map(thamSoRepository.findById(id).orElseThrow(()-> new ResourceNotfound("Không tồn tại ID tham số: "+id)),ThamSoDTO.class);
    }

    @Override
    public ThamSoDTO save(ThamSoDTO thamSoDTO) {
        ThamSo thamSo=modelMapper.map(thamSoDTO,ThamSo.class);
        thamSo.setTrangThai(true);
        thamSo.setDeletedAt(false);
        thamSo.setActive(true);
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
        ThamSo thamSo=thamSoRepository.findById(id).orElseThrow(() -> new ResourceNotfound("Không tồn tại id update tham số: "+id));
        thamSoRepository.deletedAt(id);
    }

    @Override
    public ThamSoDTO findByMaThamSo(String maThamSo) {
        return modelMapper.map(thamSoRepository.findByTenThamSo(maThamSo),ThamSoDTO.class);
    }

    @Override
    public Page<ThamSoDTO> searchThamSo(String keyword, Pageable pageable) {
        Page<ThamSo> thamSoPage = thamSoRepository.searchThamSo(keyword, pageable);
        return thamSoPage.map(thamSo -> modelMapper.map(thamSo, ThamSoDTO.class));
    }

    @Override
    public Page<ThamSoDTO> searchThamSoss(String ma, String ten, String typeGiaTri, Boolean trangThai, Pageable pageable) {
        Page<ThamSo> thamSoPage = thamSoRepository.searchThamSoss(ma, ten, typeGiaTri, trangThai,pageable);

        List<ThamSoDTO> thamSoDTOS = thamSoPage.getContent().stream()
                .map((thamSo) ->modelMapper.map(thamSo,ThamSoDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(thamSoDTOS, pageable, thamSoPage.getTotalElements());
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

    @Override
    public ThamSoDTO saveFake(ThamSoDTO thamSoDTO) {
        String giaTriFake = thamSoRepository.findByMaThamSo("TS003").getGiaTri();
        ThamSo thamSo=modelMapper.map(thamSoDTO,ThamSo.class);
        thamSo.setTrangThai(true);
        thamSo.setDeletedAt(false);
        thamSo.setTypeGiaTri(giaTriFake);
        ThamSo thamSoSave=thamSoRepository.save(thamSo);
        return modelMapper.map(thamSoSave,ThamSoDTO.class);
    }

    @Override
    public void updateTrangThai(Integer id, boolean status) {
         thamSoRepository.updateTrangThai(id, status);
    }


}
