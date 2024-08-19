package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.entity.HoaDon;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.entity.SanCa;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.HoaDonChiTietRepository;
import com.example.DATN_WebFiveTus.repository.HoaDonRepository;
import com.example.DATN_WebFiveTus.repository.SanCaRepository;
import com.example.DATN_WebFiveTus.service.HoaDonChiTietService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HoaDonChiTietServiceImp implements HoaDonChiTietService {

    private HoaDonChiTietRepository hoaDonChiTietRepository;

    private HoaDonRepository hoaDonRepository;

    private SanCaRepository sanCaRepository;

    private ModelMapper modelMapper;

    @Autowired
    public HoaDonChiTietServiceImp(HoaDonChiTietRepository hoaDonChiTietRepository, HoaDonRepository hoaDonRepository, SanCaRepository sanCaRepository, ModelMapper modelMapper) {
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.sanCaRepository = sanCaRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<HoaDonChiTietDTO> getAll() {
        return hoaDonChiTietRepository.findAll().stream()
                .map((hoaDonChiTiet) -> modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<HoaDonChiTietDTO> getAllJoinFetch() {
        return hoaDonChiTietRepository.getAllJoinFetch().stream()
                .map((hoaDonChiTiet) -> modelMapper.map(hoaDonChiTiet,HoaDonChiTietDTO.class)).collect(Collectors.toList());
    }

    @Override
    public HoaDonChiTietDTO getOne(Integer id) {
        return modelMapper.map(hoaDonChiTietRepository.findById(id).orElseThrow(()->
                new ResourceNotfound("Không tồn tại id: "+id)),HoaDonChiTietDTO.class);
    }

    @Override
    public HoaDonChiTietDTO save(HoaDonChiTietDTO hoaDonChiTietDTO) {
        return null;
    }

    @Override
    public HoaDonChiTietDTO update(Integer id, HoaDonChiTietDTO hoaDonChiTietDTO) {
        return null;
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void deletedAt(Integer id) {

    }

    @Override
    public List<HoaDonChiTietDTO> searchFromHoaDon(Integer idHoaDon) {
        return hoaDonChiTietRepository.searchFromHoaDon(idHoaDon).stream()
                .map((hoaDonChiTiet) -> modelMapper.map(hoaDonChiTiet,HoaDonChiTietDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Page<HoaDonChiTietDTO> getHoaDonChiTietByTrangThai(String trangThai, Pageable pageable) {
        Page<HoaDonChiTiet> page = hoaDonChiTietRepository.findByTrangThai(trangThai, pageable);
        List<HoaDonChiTietDTO> list = page.getContent().stream()
                .map(hoaDonChiTiet -> {
                    HoaDonChiTietDTO dto = modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class);

                    // Lấy thông tin khách hàng từ hóa đơn
                    HoaDon hoaDon = hoaDonRepository.findByIdWithKhachHang(hoaDonChiTiet.getHoaDon().getId());
                    if (hoaDon != null && hoaDon.getKhachHang() != null) {
                        dto.setIdKhachHang(hoaDon.getKhachHang().getId());
                        dto.setHoVaTenKhachHang(hoaDon.getKhachHang().getHoVaTen());
                        dto.setSoDienThoaiKhachHang(hoaDon.getKhachHang().getSoDienThoai());
                        dto.setEmailKhachHang(hoaDon.getKhachHang().getEmail());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, page.getTotalElements());
    }


    @Override
    public HoaDonChiTietDTO getOneHDCT(Integer id) {
        // Tìm HoaDonChiTiet từ repository
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findHoaDonChiTietById(id);


        // Map dữ liệu từ entity sang DTO
        HoaDonChiTietDTO dto = modelMapper.map(hoaDonChiTiet, HoaDonChiTietDTO.class);

        // Set thêm các thông tin cần thiết vào DTO
        dto.setIdHoaDon(hoaDonChiTiet.getHoaDon().getId());
        dto.setMaHoaDon(hoaDonChiTiet.getHoaDon().getMaHoaDon());
        dto.setIdSanCa(hoaDonChiTiet.getSanCa().getId());
        dto.setNgayTaoHoaDon(hoaDonChiTiet.getHoaDon().getNgayTao());
        dto.setNgayDenSan(hoaDonChiTiet.getNgayDenSan());
        dto.setTienSan(hoaDonChiTiet.getTienSan());
        dto.setGhiChu(hoaDonChiTiet.getGhiChu());
        dto.setTrangThai(hoaDonChiTiet.getTrangThai());
        dto.setTenSanBong(hoaDonChiTiet.getSanCa().getSanBong().getTenSanBong());
        dto.setTenCa(hoaDonChiTiet.getSanCa().getCa().getTenCa());
        dto.setThoiGianBatDauCa(hoaDonChiTiet.getSanCa().getCa().getThoiGianBatDau());
        dto.setThoiGianKetThucCa(hoaDonChiTiet.getSanCa().getCa().getThoiGianKetThuc());
        dto.setIdKhachHang(hoaDonChiTiet.getHoaDon().getKhachHang().getId());
        dto.setHoVaTenKhachHang(hoaDonChiTiet.getHoaDon().getKhachHang().getHoVaTen());
        dto.setSoDienThoaiKhachHang(hoaDonChiTiet.getHoaDon().getKhachHang().getSoDienThoai());
        dto.setEmailKhachHang(hoaDonChiTiet.getHoaDon().getKhachHang().getEmail());

        return dto;
    }

}
