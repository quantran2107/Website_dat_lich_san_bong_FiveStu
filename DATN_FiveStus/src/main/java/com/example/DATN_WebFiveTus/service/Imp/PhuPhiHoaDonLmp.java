
package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.dto.PhuPhiHoaDonDTO;
import com.example.DATN_WebFiveTus.entity.PhuPhiHoaDon;
import com.example.DATN_WebFiveTus.repository.PhuPhiHoaDonRepository;
import com.example.DATN_WebFiveTus.service.PhuPhiHoaDonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PhuPhiHoaDonLmp implements PhuPhiHoaDonService {
    @Autowired
    private PhuPhiHoaDonRepository phuPhiHoaDonRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public PhuPhiHoaDonDTO save(PhuPhiHoaDonDTO phuPhiHoaDonDTO) {
        PhuPhiHoaDon phuPhiHoaDon = modelMapper.map(phuPhiHoaDonDTO, PhuPhiHoaDon.class);
        phuPhiHoaDon.setDeletedAt(false);
        String generatedMa = generateMa();
        phuPhiHoaDon.setMa(generatedMa);
        PhuPhiHoaDon saved = phuPhiHoaDonRepository.save(phuPhiHoaDon);
        return modelMapper.map(saved, PhuPhiHoaDonDTO.class);
    }

    @Override
    public String generateMa() {
        return "PP-" + System.currentTimeMillis(); // Mã sẽ có định dạng: PP-<timestamp>
    }

    @Override
    public List<PhuPhiHoaDonDTO> findByHoaDonChiTietId(int hoaDonChiTietId) {
        List<PhuPhiHoaDon> phuPhiHoaDonList = phuPhiHoaDonRepository.findByHoaDonChiTiet_Id(hoaDonChiTietId);
        return phuPhiHoaDonList.stream()
                .map(phuPhiHoaDon -> modelMapper.map(phuPhiHoaDon, PhuPhiHoaDonDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public PhuPhiHoaDonDTO updateDeletedAt(int id) {
        Optional<PhuPhiHoaDon> optionalPhuPhiHoaDon = phuPhiHoaDonRepository.findById(id);

        if (optionalPhuPhiHoaDon.isPresent()) {
            PhuPhiHoaDon phuPhiHoaDon = optionalPhuPhiHoaDon.get();

            // Kiểm tra nếu deletedAt hiện tại là false
            if (!phuPhiHoaDon.isDeletedAt()) {
                phuPhiHoaDon.setDeletedAt(true);  // Cập nhật deletedAt thành true

                // Lưu thay đổi vào cơ sở dữ liệu
                PhuPhiHoaDon updatedPhuPhi = phuPhiHoaDonRepository.save(phuPhiHoaDon);

                // Chuyển entity đã được cập nhật thành DTO và trả về
                return modelMapper.map(updatedPhuPhi, PhuPhiHoaDonDTO.class);
            }
        }
        return null;
    }
    }

//    @Override
//    public void deletePhuPhi(int id) {
//        phuPhiHoaDonRepository.deleteById(id);
//    }

