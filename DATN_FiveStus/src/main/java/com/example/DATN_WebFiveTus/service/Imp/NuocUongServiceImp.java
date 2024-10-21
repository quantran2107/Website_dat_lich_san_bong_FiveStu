package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.DoThueDTO;
import com.example.DATN_WebFiveTus.dto.NuocUongDTO;
import com.example.DATN_WebFiveTus.entity.DoThue;
import com.example.DATN_WebFiveTus.entity.NuocUong;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.NuocUongRepository;
import com.example.DATN_WebFiveTus.service.NuocUongService;
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
public class NuocUongServiceImp implements NuocUongService {

    private NuocUongRepository nuocUongRepository;
    private ModelMapper modelMapper;

    public NuocUongServiceImp(NuocUongRepository nuocUongRepository, ModelMapper modelMapper) {
        this.nuocUongRepository = nuocUongRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<NuocUongDTO> getAll() {
        List<NuocUong> nuocUongList = nuocUongRepository.getAll();
        return nuocUongList.stream()
                .map(nuocUong -> modelMapper.map(nuocUong, NuocUongDTO.class))
                .collect(Collectors.toList());
    }

//    @Override
//    public List<NuocUongDTO> getAll() {
//        List<NuocUong> nuocUongList = nuocUongRepository.getAll();
//        return nuocUongList.stream()
//                .map(nuocUong -> modelMapper.map(nuocUong, NuocUongDTO.class))
//                .collect(Collectors.toList());
//    }

    @Override
    public NuocUongDTO getOne(Integer id) {
        return modelMapper.map(nuocUongRepository.findById(id).orElseThrow(() ->
                new ResourceNotfound("Không tồn tại phieu giam gia ID: " + id)), NuocUongDTO.class);
    }

    @Override
    public NuocUongDTO save(NuocUongDTO nuocUongDTO) {
        NuocUong nuocUong = modelMapper.map(nuocUongDTO, NuocUong.class);
        nuocUong.setDeletedAt(false); // Đặt giá trị deletedAt trước khi lưu
        NuocUong savedEntity = nuocUongRepository.save(nuocUong);
        return modelMapper.map(savedEntity, NuocUongDTO.class);
    }

    @Override
    public NuocUongDTO update(Integer id, NuocUongDTO nuocUongDTO) {
        // Tìm bản ghi dựa trên ID
        NuocUong nuocUong = nuocUongRepository.findById(id)
                .orElseThrow(() -> new ResourceNotfound("Không tồn tại do thue ID: " + id));

        // Cập nhật các trường dữ liệu từ doThueDTO
        nuocUong.setTenNuocUong(nuocUongDTO.getTenNuocUong());
        nuocUong.setDonGias(nuocUongDTO.getDonGias());
        nuocUong.setSoLuongs(nuocUongDTO.getSoLuongs());
//        doThue.setTrangThai(doThueDTO.getTrangThai());

        // Cập nhật URL ảnh nếu có
        if (nuocUongDTO.getImageData() != null) {
            nuocUong.setImageData(nuocUongDTO.getImageData());
        }

        // Lưu lại bản ghi đã cập nhật
        NuocUong updatedEntity = nuocUongRepository.save(nuocUong);
        return modelMapper.map(updatedEntity, NuocUongDTO.class);
    }


    @Override
    @Transactional
    public void delete(Integer id) {
        NuocUong nuocUong = nuocUongRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DoThue not found with id " + id));
        nuocUong.setDeletedAt(true); // Giả sử có phương thức setDeletedAt trong entity
        nuocUongRepository.save(nuocUong); // Cập nhật entity trong cơ sở dữ liệu
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NuocUongDTO> phanTrang(Pageable pageable) {
        Page<NuocUong> trangNuocUong = nuocUongRepository.getAllJoinFetch(pageable);
        List<NuocUongDTO> danhSachDTO = trangNuocUong.getContent().stream()
                .map(nuocUong -> modelMapper.map(nuocUong, NuocUongDTO.class))
                .collect(Collectors.toList());
        return new PageImpl<>(danhSachDTO, pageable, trangNuocUong.getTotalElements());
    }

    @Override
    public Page<NuocUongDTO> searchNuocUong(String keyword, String trangThai, Float donGiaMin, Float donGiaMax, Pageable pageable) {
        // Lấy dữ liệu phân trang cùng với tổng số bản ghi
        Page<NuocUong> nuocUongPage = nuocUongRepository.searchNuocUong(keyword, trangThai, donGiaMin, donGiaMax, pageable);

        // Chuyển đổi danh sách DoThue thành DoThueDTO
        List<NuocUongDTO> nuocUongDTOList = nuocUongPage.getContent().stream()
                .map(nuocUong -> modelMapper.map(nuocUong, NuocUongDTO.class))
                .collect(Collectors.toList());

        // Trả về đối tượng PageImpl với tổng số bản ghi từ Page
        return new PageImpl<>(nuocUongDTOList, pageable, nuocUongPage.getTotalElements());
    }

    @Override
    public List<NuocUongDTO> getAllJoinFetch2() {
        List<NuocUong> nuocUongList = nuocUongRepository.getAllJoinFetch2();
        return nuocUongList.stream()
                .map(nuocUong -> modelMapper.map(nuocUong, NuocUongDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean checkIdDichVuNuocUong(Integer id, Integer idHoaDonChiTiet) {
        return nuocUongRepository.checkIdDichVuNuocUong(id, idHoaDonChiTiet).isPresent();
    }

    @Override
    public int getIdNuocUong(Integer idNuocUong, Integer idHoaDonChiTiet) {
        return nuocUongRepository.getIdNuocUong(idNuocUong,idHoaDonChiTiet);
    }

    @Override
    public List<NuocUongDTO> searchTenNuocUong(String tenNuocUong) {
        return nuocUongRepository.searchTenNuocUong(tenNuocUong).stream()
                .map(dothue -> modelMapper.map(dothue, NuocUongDTO.class))
                .collect(Collectors.toList());
    }
}
