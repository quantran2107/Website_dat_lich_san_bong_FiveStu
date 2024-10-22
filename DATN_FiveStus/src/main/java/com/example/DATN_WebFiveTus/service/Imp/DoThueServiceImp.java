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
                .map(dothue -> {
                    DoThueDTO dto = modelMapper.map(dothue, DoThueDTO.class);
                    dto.setImageData(dothue.getImageData()); // Lưu trữ URL hình ảnh
                    return dto;
                })
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
        doThue.setDonGias(doThueDTO.getDonGias());
        doThue.setSoLuongs(doThueDTO.getSoLuongs());
        doThue.setTrangThai(doThueDTO.getTrangThai());
        DoThue doThueUpdate=doThueRepository.save(doThue);
        return modelMapper.map(doThueUpdate,DoThueDTO.class);

//        // Tìm kiếm đối tượng DoThue theo ID trong cơ sở dữ liệu
//        Optional<DoThue> optionalDoThue = doThueRepository.findById(id);
//        if (!optionalDoThue.isPresent()) {
//            throw new ResourceNotFoundException("DoThue not found with ID: " + id);
//        }
//
//        // Lấy đối tượng DoThue hiện tại từ Optional
//        DoThue existingDoThue = optionalDoThue.get();
//
//        // Cập nhật các thuộc tính mới từ DoThueDTO
//        existingDoThue.setTenDoThue(doThueDTO.getTenDoThue());
//        existingDoThue.setDonGia(doThueDTO.getDonGia());
//        existingDoThue.setSoLuong(doThueDTO.getSoLuong());
//        existingDoThue.setTrangThai(doThueDTO.getTrangThai());
////        existingDoThue.setDeletedAt(doThueDTO.getDeletedAt());
//
//        // Cập nhật ảnh nếu có
//        if (doThueDTO.getImageData() != null) {
//            existingDoThue.setImageData(doThueDTO.getImageData());
//        }
//
//        // Lưu đối tượng đã cập nhật vào cơ sở dữ liệu
//        DoThue updatedDoThue = doThueRepository.save(existingDoThue);
//
//        // Chuyển đổi từ DoThue entity sang DoThueDTO để trả về
//        return modelMapper.map(updatedDoThue, DoThueDTO.class);

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

    @Override
    public List<DoThueDTO> getAllJoinFetch2() {
        List<DoThue> doThues = doThueRepository.getAllJoinFetch2();
        return doThues.stream()
                .map(dothue -> modelMapper.map(dothue, DoThueDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean checkIdDichVuDoThue(Integer id, Integer idHoaDonChiTiet) {
        return doThueRepository.checkIdDichVuDoThue(id, idHoaDonChiTiet).isPresent();
    }

    @Override
    public int getIdDoThue(Integer idDoThue, Integer idHoaDonChiTiet) {
        return doThueRepository.getIdDoThue(idDoThue,idHoaDonChiTiet);
    }

    @Override
    public List<DoThueDTO> searchTenDoThue(String tenDoThue) {
        return doThueRepository.searchTenDoThue(tenDoThue).stream()
                .map(dothue -> modelMapper.map(dothue, DoThueDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public DoThueDTO updateSoLuong(Integer id, DoThueDTO doThueDTO) {
        DoThue doThue= doThueRepository.findById(id).orElseThrow();

        doThue.setSoLuongs(doThueDTO.getSoLuongs());
        DoThue doThueUpdate=doThueRepository.save(doThue);
        return modelMapper.map(doThueUpdate,DoThueDTO.class);
    }


}
