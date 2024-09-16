package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.DoThueDTO;
import com.example.DATN_WebFiveTus.entity.DoThue;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.DoThueRepository;
import com.example.DATN_WebFiveTus.service.DoThueService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<DoThue> doThues = doThueRepository.getAll();
        return doThues.stream()
                .map(dothue -> modelMapper.map(dothue, DoThueDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public DoThueDTO getOne(Integer id) {
        return modelMapper.map(doThueRepository.findById(id).orElseThrow(() ->
                new ResourceNotfound("Không tồn tại phieu giam gia ID: " + id)), DoThueDTO.class);
    }

    @Override
    public DoThueDTO save(DoThueDTO doThueDTO) {
        DoThue doThue = modelMapper.map(doThueDTO, DoThue.class);
        doThue.setDeletedAt(false); // Đặt giá trị deletedAt trước khi lưu
        DoThue savedEntity = doThueRepository.save(doThue);
        return modelMapper.map(savedEntity, DoThueDTO.class);
    }

    @Override
    public DoThueDTO update(Integer id, DoThueDTO doThueDTO) {
        DoThue doThue= doThueRepository.findById(id).orElseThrow(() ->
                new ResourceNotfound("Không tồn tại do thue ID: " + id));
        doThue.setTenDoThue(doThueDTO.getTenDoThue());
        doThue.setDonGia(doThueDTO.getDonGia());
        doThue.setSoLuong(doThueDTO.getSoLuong());
        doThue.setTrangThai(doThueDTO.getTrangThai());
        DoThue doThueUpdate=doThueRepository.save(doThue);
        return modelMapper.map(doThueUpdate,DoThueDTO.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        DoThue doThue = doThueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DoThue not found with id " + id));
        doThue.setDeletedAt(true); // Giả sử có phương thức setDeletedAt trong entity
        doThueRepository.save(doThue); // Cập nhật entity trong cơ sở dữ liệu
    }




    @Override
    @Transactional(readOnly = true)
    public Page<DoThueDTO> phanTrang(Pageable pageable) {
        Page<DoThue> trangDoThue = doThueRepository.getAllJoinFetch(pageable);
        List<DoThueDTO> danhSachDTO = trangDoThue.getContent().stream()
                .map(doThue -> modelMapper.map(doThue, DoThueDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(danhSachDTO, pageable, trangDoThue.getTotalElements());
    }

    @Override
    public Page<DoThueDTO> searchDoThue(String keyword, String trangThai, Float donGiaMin, Float donGiaMax, Pageable pageable) {
        // Lấy dữ liệu phân trang cùng với tổng số bản ghi
        Page<DoThue> doThuePage = doThueRepository.searchDoThue(keyword, trangThai, donGiaMin, donGiaMax, pageable);

        // Chuyển đổi danh sách DoThue thành DoThueDTO
        List<DoThueDTO> doThueDTOList = doThuePage.getContent().stream()
                .map(doThue -> modelMapper.map(doThue, DoThueDTO.class))
                .collect(Collectors.toList());

        // Trả về đối tượng PageImpl với tổng số bản ghi từ Page
        return new PageImpl<>(doThueDTOList, pageable, doThuePage.getTotalElements());
    }





}
