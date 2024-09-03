package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.HoaDonRepository;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.repository.PhieuGiamGiaRepository;
import com.example.DATN_WebFiveTus.service.HoaDonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoaDonServiceImp implements HoaDonService {

    private HoaDonRepository hoaDonRepository;

    private NhanVienReposity nhanVienReposity;

    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    private KhachHangRepository khachHangRepository;

    private ModelMapper modelMapper;

    @Autowired
    public HoaDonServiceImp(HoaDonRepository hoaDonRepository, NhanVienReposity nhanVienReposity, PhieuGiamGiaRepository phieuGiamGiaRepository, KhachHangRepository khachHangRepository, ModelMapper modelMapper) {
        this.hoaDonRepository = hoaDonRepository;
        this.nhanVienReposity = nhanVienReposity;
        this.phieuGiamGiaRepository = phieuGiamGiaRepository;
        this.khachHangRepository = khachHangRepository;
        this.modelMapper = modelMapper;
    }



    @Override
    public List<HoaDonDTO> getAll() {
        return hoaDonRepository.findAll().stream()
                .map((hoaDon) -> modelMapper.map(hoaDon, HoaDonDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<HoaDonDTO> getAllJoinFetch() {
        return hoaDonRepository.getAllJoinFetch().stream()
                .map((hoaDon) -> modelMapper.map(hoaDon,HoaDonDTO.class)).collect(Collectors.toList());
    }

    @Override
    public HoaDonDTO getOne(Integer id) {
        return modelMapper.map(hoaDonRepository.findById(id).orElseThrow(()->
                new ResourceNotfound("Không tồn tại id: "+id)),HoaDonDTO.class);
    }

    @Override
    public HoaDonDTO save(HoaDonDTO hoaDonDTO) {
        HoaDon hoaDon = modelMapper.map(hoaDonDTO,HoaDon.class);
        hoaDon.setMaHoaDon(generateMaHoaDon());
        hoaDon.setTrangThai("Chờ thanh toán");
        Date now = Date.from(Instant.now());
        hoaDon.setNgayTao(now);
        HoaDon hoaDonSave = hoaDonRepository.save(hoaDon);
        return modelMapper.map(hoaDonSave,HoaDonDTO.class);
    }

    private String generateMaHoaDon() {
        String PREFIX = "HD";
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int RANDOM_PART_LENGTH = 8; // Độ dài của phần ngẫu nhiên, để tổng độ dài là 10
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder(PREFIX);
        for (int i = 0; i < RANDOM_PART_LENGTH; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    @Override
    public HoaDonDTO update(Integer id, HoaDonDTO hoaDonDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void deletedAt(Integer id) {

    }

    @Override
    public Page<HoaDonDTO> searchAndFilter(@Param("loai") Boolean loai,
                                           @Param("trangThai") String trangThai,
                                           @Param("keyword") String keyword,
                                           @Param("tongTienMin") Float tongTienMin,
                                           @Param("tongTienMax") Float tongTienMax,
                                           Pageable pageable) {
        List<HoaDon> hoaDonList = hoaDonRepository.searchAndFilter(loai, trangThai, keyword, tongTienMin, tongTienMax);

        // Phân trang thủ công
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<HoaDon> list;

        if (hoaDonList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, hoaDonList.size());
            list = hoaDonList.subList(startItem, toIndex);
        }

        List<HoaDonDTO> hoaDonDTOList = list.stream()
                .map(hoaDon -> modelMapper.map(hoaDon, HoaDonDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(hoaDonDTOList, pageable, hoaDonList.size());
    }

    @Override
    public Page<HoaDonDTO> phanTrang(Pageable pageable) {
        List<HoaDon> hoaDonList = hoaDonRepository.getAllJoinFetch();

        // Phân trang thủ công
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<HoaDon> list;

        if (hoaDonList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, hoaDonList.size());
            list = hoaDonList.subList(startItem, toIndex);
        }

        List<HoaDonDTO> hoaDonDTOList = list.stream()
                .map(hoaDon -> modelMapper.map(hoaDon, HoaDonDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(hoaDonDTOList, pageable, hoaDonList.size());
    }
}
