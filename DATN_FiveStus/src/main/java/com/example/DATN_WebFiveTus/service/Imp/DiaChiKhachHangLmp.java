package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.DiaChiKhachHangDTO;
import com.example.DATN_WebFiveTus.entity.DiaChiKhachHang;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.repository.DiaChiKhachHangRepository;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
import com.example.DATN_WebFiveTus.service.DiaChiKhachHangService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DiaChiKhachHangLmp implements DiaChiKhachHangService {
    @Autowired
    private DiaChiKhachHangRepository diaChiKhachHangRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private KhachHangRepository khachHangRepository;

    @Override
    public List<DiaChiKhachHangDTO> getAll() {
        // Lấy tất cả địa chỉ khách hàng
        List<DiaChiKhachHang> diaChiKhachHangs = diaChiKhachHangRepository.findAll();

        // Lọc các địa chỉ có deletedAt = false (chỉ lấy các địa chỉ không bị xóa)
        return diaChiKhachHangs.stream()
                .filter(diaChiKhachHang -> Boolean.FALSE.equals(diaChiKhachHang.getDeletedAt())) // Kiểm tra trường deletedAt
                .map(diaChiKhachHang -> modelMapper.map(diaChiKhachHang, DiaChiKhachHangDTO.class))
                .collect(Collectors.toList());
    }




    @Override
    public DiaChiKhachHangDTO getOne(Integer id) {
        DiaChiKhachHang diaChiKhachHang = diaChiKhachHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ có id: " + id));
        return modelMapper.map(diaChiKhachHang, DiaChiKhachHangDTO.class);
    }

    @Override
    public void save(DiaChiKhachHangDTO diaChiKhachHangDTO) {
        DiaChiKhachHang diaChiKhachHang = modelMapper.map(diaChiKhachHangDTO, DiaChiKhachHang.class);
        diaChiKhachHangRepository.save(diaChiKhachHang);
    }

    @Override
    public void update(Integer id, DiaChiKhachHangDTO diaChiKhachHangDTO) {
        DiaChiKhachHang diaChi = diaChiKhachHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ có id: " + id));

        // Cập nhật thông tin từ diaChiDTO vào diaChi
        diaChi.setDiaChiCuThe(diaChiKhachHangDTO.getDiaChiCuThe());
        diaChi.setThanhPho(diaChiKhachHangDTO.getThanhPho());
        diaChi.setQuanHuyen(diaChiKhachHangDTO.getQuanHuyen());
        diaChi.setPhuongXa(diaChiKhachHangDTO.getPhuongXa());
        diaChi.setGhiChu(diaChiKhachHangDTO.getGhiChu());

        // Lưu thông tin địa chỉ đã cập nhật vào cơ sở dữ liệu
        diaChiKhachHangRepository.save(diaChi);
    }


    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<DiaChiKhachHangDTO> getAllJoinFetch() {
        return null;
    }

    public List<DiaChiKhachHangDTO> findById(Integer idKhachHang) {
        List<DiaChiKhachHang> diaChiKhachHangs = diaChiKhachHangRepository.findByIdKhachHang_Id(idKhachHang);
        return diaChiKhachHangs.stream()
                .map(diaChiKhachHang -> modelMapper.map(diaChiKhachHang, DiaChiKhachHangDTO.class))
                .collect(Collectors.toList());

    }

    @Override
    public DiaChiKhachHangDTO updateDiaChi(Integer diaChiId, DiaChiKhachHangDTO diaChiKhachHangDTO) {
        Optional<DiaChiKhachHang> diaChi = diaChiKhachHangRepository.findById(diaChiId);

        if (diaChi.isPresent()) {
            // Cập nhật các trường thông tin địa chỉ
            DiaChiKhachHang existingDiaChi = diaChi.get();
            existingDiaChi.setDiaChiCuThe(diaChiKhachHangDTO.getDiaChiCuThe());
            existingDiaChi.setThanhPho(diaChiKhachHangDTO.getThanhPho());
            existingDiaChi.setQuanHuyen(diaChiKhachHangDTO.getQuanHuyen());
            existingDiaChi.setPhuongXa(diaChiKhachHangDTO.getPhuongXa());
            existingDiaChi.setGhiChu(diaChiKhachHangDTO.getGhiChu());

            // Lưu lại địa chỉ đã cập nhật vào cơ sở dữ liệu
            diaChiKhachHangRepository.save(existingDiaChi);

            // Trả về địa chỉ đã cập nhật dưới dạng DTO
            return modelMapper.map(existingDiaChi, DiaChiKhachHangDTO.class);
        }
        return null;  // Nếu không tìm thấy địa chỉ
    }

    @Override
    public Page<DiaChiKhachHangDTO> findByIdDC(Integer idKhachHang, Pageable pageable) {
        Page<DiaChiKhachHang> diaChiKhachHangPage = diaChiKhachHangRepository.findByIdKhachHang_Id(idKhachHang, pageable);
        return diaChiKhachHangPage.map(diaChiKhachHang -> modelMapper.map(diaChiKhachHang, DiaChiKhachHangDTO.class));
    }

    @Override
    public List<DiaChiKhachHangDTO> getDiaChiByEmail(String email) {
        KhachHang khachHang = khachHangRepository.findKhachHangByEmail1(email)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại với email: " + email));

        List<DiaChiKhachHang> diaChiKhachHangList = diaChiKhachHangRepository.findActiveAddressesByCustomerId(khachHang.getEmail());

        return diaChiKhachHangList.stream()
                .map(diaChiKhachHang -> modelMapper.map(diaChiKhachHang, DiaChiKhachHangDTO.class))
                .collect(Collectors.toList());
    }


    public boolean deleteDiaChiByEmail(String email, Integer id) {
        DiaChiKhachHang diaChiKhachHang = diaChiKhachHangRepository.findDiaChiByEmailAndId(email, id)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại với ID: " + id + " và email: " + email));

        // Đánh dấu là đã xoá mềm
        diaChiKhachHang.setDeletedAt(true);
        diaChiKhachHangRepository.save(diaChiKhachHang);

        return true;
    }

    @Override
    public DiaChiKhachHangDTO updateDiaChiByEmail(String email, Integer id, DiaChiKhachHangDTO diaChiKhachHangDTO) {
        // Tìm khách hàng theo email và địa chỉ theo id
        DiaChiKhachHang diaChiKhachHang = diaChiKhachHangRepository.findDiaChiByEmailAndId(email, id)
                .orElseThrow(() -> new RuntimeException("Địa chỉ không tồn tại với ID: " + id + " và email: " + email));

        // Cập nhật thông tin địa chỉ cụ thể
        diaChiKhachHang.setDiaChiCuThe(diaChiKhachHangDTO.getDiaChiCuThe());
        diaChiKhachHang.setThanhPho(diaChiKhachHangDTO.getThanhPho());
        diaChiKhachHang.setQuanHuyen(diaChiKhachHangDTO.getQuanHuyen());
        diaChiKhachHang.setPhuongXa(diaChiKhachHangDTO.getPhuongXa());

        // Lưu lại địa chỉ đã cập nhật
        diaChiKhachHangRepository.save(diaChiKhachHang);

        // Trả về DTO đã cập nhật
        return modelMapper.map(diaChiKhachHang, DiaChiKhachHangDTO.class);
    }

    @Override
    public Optional<DiaChiKhachHang> getDiaChiByEmailAndId(String email, Integer id) {
        return diaChiKhachHangRepository.findDiaChiByEmailAndId(email, id);
    }

    @Override
    public void addDiaChiByEmail(String email, DiaChiKhachHangDTO diaChiKhachHangDTO) {
        KhachHang khachHang = khachHangRepository.findKhachHangByEmail1(email)
                .orElseThrow(() -> new RuntimeException("Khách hàng không tồn tại với email: " + email));

        // Chuyển đổi DTO thành thực thể
        DiaChiKhachHang diaChiKhachHang = modelMapper.map(diaChiKhachHangDTO, DiaChiKhachHang.class);

        // Gán khách hàng cho địa chỉ
        diaChiKhachHang.setIdKhachHang(khachHang);
        diaChiKhachHang.setDeletedAt(Boolean.FALSE);

        // Lưu địa chỉ mới
        diaChiKhachHangRepository.save(diaChiKhachHang);
    }


}
