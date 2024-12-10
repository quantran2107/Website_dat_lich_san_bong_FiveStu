package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.entity.LoaiSan;
import com.example.DATN_WebFiveTus.entity.SanBong;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.LoaiSanRepository;
import com.example.DATN_WebFiveTus.repository.SanBongRepository;
import com.example.DATN_WebFiveTus.service.SanBongService;
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
public class SanBongServiceImp implements SanBongService {
private LoaiSanRepository loaiSanRepository;

    private SanBongRepository sanBongRepository;

    private ModelMapper modelMapper;


    @Autowired
    public SanBongServiceImp(LoaiSanRepository loaiSanRepository, SanBongRepository sanBongRepository, ModelMapper modelMapper) {
        this.loaiSanRepository = loaiSanRepository;
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
        return modelMapper.map(sanBongRepository.findById(id).orElseThrow(() -> new ResourceNotfound("Không tồn tại sân bóng ID: "+id)),SanBongDTO.class);
    }

    @Override
    public SanBongDTO save(SanBongDTO sanBongDTO) {
        LoaiSan loaiSan=loaiSanRepository.findById(sanBongDTO.getIdLoaiSan()).orElseThrow(() -> new ResourceNotfound("Không tồn tại loại sân bóng ID ạ: "+sanBongDTO.getIdLoaiSan()));
        SanBong sanBong=modelMapper.map(sanBongDTO,SanBong.class);
        sanBong.setLoaiSan(loaiSan);
        sanBong.setTrangThai("Hoạt động");
        SanBong sanBongSave=sanBongRepository.save(sanBong);
        return modelMapper.map(sanBongSave,SanBongDTO.class);
    }

    @Override
    public SanBongDTO update(Integer id, SanBongDTO sanBongDTO) {
        LoaiSan loaiSan=loaiSanRepository.findById(sanBongDTO.getIdLoaiSan()).orElseThrow(() -> new ResourceNotfound("Không tồn tại loại sân bóng ID: "+sanBongDTO.getIdLoaiSan()));
        SanBong sanBong=sanBongRepository.findById(id).orElseThrow(() -> new ResourceNotfound("Không tồn tại sân bóng ID: "+id));
        sanBong.setTenSanBong(sanBongDTO.getTenSanBong());
        sanBong.setTrangThai(sanBongDTO.getTrangThai());
        sanBong.setLoaiSan(loaiSan);
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
    public List<SanBongDTO> getAllJoinFetchActive() {
        return sanBongRepository.getAllJoinFetchActive().stream().map((sanBong) ->modelMapper
                .map(sanBong,SanBongDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void deletedAt(Integer id) {
        SanBong sanBong=sanBongRepository.findById(id).orElseThrow(()-> new ResourceNotfound("Không tồn tại xoá id: "+id));
        sanBongRepository.deletedAt(id);
    }

    @Override
    public Page<SanBongDTO> pages(Integer pageNo, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<SanBong> sanBongPage = sanBongRepository.getAllJoinFetchPageable(pageable);
        return sanBongPage.map(sanBong -> modelMapper.map(sanBong, SanBongDTO.class));
    }

    @Override
    public List<SanBongDTO> getSanBongsByLoaiSanId(Integer loaiSanId) {
        return  sanBongRepository.findByLoaiSanId(loaiSanId).stream().map((sanBong) ->modelMapper
                .map(sanBong,SanBongDTO.class)).collect(Collectors.toList());
    }


    @Override
    public List<SanBongDTO> listAllSortPage(Integer pageNum, String sortDirection, int[] totalPageElement) {
        Sort sortS = Sort.by("tenSanBong");
        if (sortDirection.equalsIgnoreCase("asc")) {
            sortS = sortS.ascending();
        } else if (sortDirection.equalsIgnoreCase("desc")) {
            sortS = sortS.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, 5, sortS);
        Page<SanBong> sanBongPage = sanBongRepository.findBySanBongPage(pageable);

        totalPageElement[0] = sanBongPage.getTotalPages();
        totalPageElement[1] = (int) sanBongPage.getTotalElements();

        return sanBongPage.getContent().stream()
                .map(sanBong -> modelMapper.map(sanBong, SanBongDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SanBongDTO> searchKeyWords(Integer pageNum, String keyWords, String sortDirection, int[] totalPageElement, Integer id) {
        Sort sortS = Sort.by("tenSanBong");
        if (sortDirection.equalsIgnoreCase("asc")) {
            sortS = sortS.ascending();
        } else if (sortDirection.equalsIgnoreCase("desc")) {
            sortS = sortS.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, 5, sortS);
        Page<SanBong> sanBongPage = sanBongRepository.search(id,keyWords.trim(),pageable);

        totalPageElement[0] = sanBongPage.getTotalPages();
        totalPageElement[1] = (int) sanBongPage.getTotalElements();

        return sanBongPage.getContent().stream()
                .map(sanBong -> modelMapper.map(sanBong, SanBongDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public SanBongDTO getSanBongByName(String tenSanBong) {
        return modelMapper.map(sanBongRepository.getSanBongByName(tenSanBong),SanBongDTO.class);
    }

    @Override
    public Boolean existsByTenSanBongs(Integer idLoaiSan, String tenSanBong) {
        return sanBongRepository.existsByTenSanBongs(idLoaiSan, tenSanBong);
    }

    @Override
    public List<SanBongDTO> getListSanBongWithIdLoaiSan(Integer idLoaiSan) {
        return  sanBongRepository.getListSanBongWithIdLoaiSan(idLoaiSan).stream().map((sanBong) ->modelMapper
                .map(sanBong,SanBongDTO.class)).collect(Collectors.toList());
    }

    @Override
    public boolean checkTrungSanBong(Integer idLoaiSan, String tenSanBong) {
        // Kiểm tra nếu tên sân bóng đã tồn tại trong cùng loại sân
        SanBong sanBong = sanBongRepository.checkTrungSanBongWithLoaiSanAndName(idLoaiSan, tenSanBong);
        return sanBong != null; // Nếu có đối tượng sanBong, trả về true (trùng tên), nếu không trả về false
    }

    @Override
    public void updateTrangThai(Integer id, String status) {
        sanBongRepository.updateTrangThai(id,status);
    }


}
