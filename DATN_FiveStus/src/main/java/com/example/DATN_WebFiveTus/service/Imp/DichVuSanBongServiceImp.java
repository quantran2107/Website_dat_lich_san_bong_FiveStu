package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.DichVuSanBongDTO;
import com.example.DATN_WebFiveTus.entity.DichVuSanBong;
import com.example.DATN_WebFiveTus.entity.DoThue;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import com.example.DATN_WebFiveTus.entity.NuocUong;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.DichVuSanBongRepository;
import com.example.DATN_WebFiveTus.repository.DoThueRepository;
import com.example.DATN_WebFiveTus.repository.HoaDonChiTietRepository;
import com.example.DATN_WebFiveTus.repository.NuocUongRepository;
import com.example.DATN_WebFiveTus.service.DichVuSanBongService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DichVuSanBongServiceImp implements DichVuSanBongService {

    private DichVuSanBongRepository dichVuSanBongRepository;

    private DoThueRepository doThueRepository;

    private NuocUongRepository nuocUongRepository;

    private HoaDonChiTietRepository hoaDonChiTietRepository;

    private ModelMapper modelMapper;

    @Autowired
    public DichVuSanBongServiceImp(DichVuSanBongRepository dichVuSanBongRepository, DoThueRepository doThueRepository, NuocUongRepository nuocUongRepository
            , HoaDonChiTietRepository hoaDonChiTietRepository, ModelMapper modelMapper) {
        this.dichVuSanBongRepository = dichVuSanBongRepository;
        this.doThueRepository = doThueRepository;
        this.nuocUongRepository = nuocUongRepository;
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<DichVuSanBongDTO> getAll() {
        return dichVuSanBongRepository.findAll().stream()
                .map((dichVuSanBong) -> modelMapper.map(dichVuSanBong, DichVuSanBongDTO.class)).collect(Collectors.toList());
    }

    @Override
    public DichVuSanBongDTO getOne(Integer id) {
        return modelMapper.map(dichVuSanBongRepository.findById(id).orElseThrow(() -> new ResourceNotfound("Không tồn tại id DVSB: " + id)), DichVuSanBongDTO.class);
    }

    @Override
    public DichVuSanBongDTO save(DichVuSanBongDTO dichVuSanBongDTO) {
        // Chuyển đổi DTO thành thực thể
        DichVuSanBong dichVuSanBong = modelMapper.map(dichVuSanBongDTO, DichVuSanBong.class);

        // Xử lý DoThue
        if (dichVuSanBongDTO.getIdDoThue() != null) {
            DoThue doThue = doThueRepository.findById(dichVuSanBongDTO.getIdDoThue())
                    .orElseThrow(() -> new ResourceNotfound("Không tồn tại id do thue: " + dichVuSanBongDTO.getIdDoThue()));
            dichVuSanBong.setDoThue(doThue);
        } else {
            dichVuSanBong.setDoThue(null); // Thiết lập là null nếu không có id
            dichVuSanBong.setSoLuongDoThue(0);
        }

        // Xử lý NuocUong
        if (dichVuSanBongDTO.getIdNuocUong() != null) {
            NuocUong nuocUong = nuocUongRepository.findById(dichVuSanBongDTO.getIdNuocUong())
                    .orElseThrow(() -> new ResourceNotfound("Không tồn tại id nuoc uong: " + dichVuSanBongDTO.getIdNuocUong()));
            dichVuSanBong.setNuocUong(nuocUong);
        } else {
            dichVuSanBong.setNuocUong(null); // Thiết lập là null nếu không có id
            dichVuSanBong.setSoLuongNuocUong(0);
        }

        // Xử lý HoaDonChiTiet
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(dichVuSanBongDTO.getIdHoaDonChiTiet())
                .orElseThrow(() -> new ResourceNotfound("Không tồn tại id hdct: " + dichVuSanBongDTO.getIdHoaDonChiTiet()));
        dichVuSanBong.setHoaDonChiTiet(hoaDonChiTiet);
        dichVuSanBong.setDeletedAt(false);
        // Lưu thực thể
        DichVuSanBong dichVuSanBongSave = dichVuSanBongRepository.save(dichVuSanBong);
        return modelMapper.map(dichVuSanBongSave, DichVuSanBongDTO.class);
    }

    @Override
    public DichVuSanBongDTO update(Integer id, DichVuSanBongDTO dichVuSanBongDTO) {
        DichVuSanBong dichVuSanBong = dichVuSanBongRepository.findById(id).orElseThrow(() -> new ResourceNotfound("Không tồn tại id DVSB: " + id));
//        DoThue doThue = doThueRepository.findById(dichVuSanBongDTO.getIdDoThue()).orElseThrow(() -> new ResourceNotfound("Không tồn tại id do thue: " + dichVuSanBongDTO.getIdDoThue()));
//        NuocUong nuocUong = nuocUongRepository.findById(dichVuSanBongDTO.getIdNuocUong()).orElseThrow(() -> new ResourceNotfound("Không tồn tại id nuoc uong: " + dichVuSanBongDTO.getIdNuocUong()));
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(dichVuSanBongDTO.getIdHoaDonChiTiet()).orElseThrow(() -> new ResourceNotfound("Không tồn tại id hdct: " + dichVuSanBongDTO.getIdHoaDonChiTiet()));

        // Xử lý DoThue
        if (dichVuSanBongDTO.getIdDoThue() != null) {
            DoThue doThue = doThueRepository.findById(dichVuSanBongDTO.getIdDoThue())
                    .orElseThrow(() -> new ResourceNotfound("Không tồn tại id do thue: " + dichVuSanBongDTO.getIdDoThue()));
            dichVuSanBong.setDoThue(doThue);
        } else {
            dichVuSanBong.setDoThue(null); // Thiết lập là null nếu không có id
            dichVuSanBong.setSoLuongDoThue(0);
        }

        // Xử lý NuocUong
        if (dichVuSanBongDTO.getIdNuocUong() != null) {
            NuocUong nuocUong = nuocUongRepository.findById(dichVuSanBongDTO.getIdNuocUong())
                    .orElseThrow(() -> new ResourceNotfound("Không tồn tại id nuoc uong: " + dichVuSanBongDTO.getIdNuocUong()));
            dichVuSanBong.setNuocUong(nuocUong);
        } else {
            dichVuSanBong.setNuocUong(null); // Thiết lập là null nếu không có id
            dichVuSanBong.setSoLuongNuocUong(0);
        }

//        dichVuSanBong.setDoThue(doThue);
//        dichVuSanBong.setNuocUong(nuocUong);
        dichVuSanBong.setHoaDonChiTiet(hoaDonChiTiet);
        dichVuSanBong.setSoLuongDoThue(dichVuSanBongDTO.getSoLuongDoThue());
        dichVuSanBong.setSoLuongNuocUong(dichVuSanBongDTO.getSoLuongNuocUong());
        dichVuSanBong.setDonGia(dichVuSanBongDTO.getDonGia());
        dichVuSanBong.setTrangThai(dichVuSanBongDTO.getTrangThai());
        dichVuSanBong.setDeletedAt(dichVuSanBongDTO.isDeletedAt());
        DichVuSanBong dichVuSanBongUpdate = dichVuSanBongRepository.save(dichVuSanBong);
        return modelMapper.map(dichVuSanBongUpdate, DichVuSanBongDTO.class);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<DichVuSanBongDTO> searchDichVuSanBong(Integer idHoaDonChiTiet) {
        return dichVuSanBongRepository.searchDichVuSanBong(idHoaDonChiTiet).stream()
                .map((dichVuSanBong) -> modelMapper.map(dichVuSanBong, DichVuSanBongDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<DichVuSanBongDTO> getAllJoinFetch() {
        return dichVuSanBongRepository.getAllJoinFetch().stream()
                .map((dichVuSanBong) -> modelMapper.map(dichVuSanBong, DichVuSanBongDTO.class)).collect(Collectors.toList());
    }
}
