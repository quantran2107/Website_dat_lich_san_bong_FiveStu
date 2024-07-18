package com.example.DATN_WebFiveTus.service;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.entity.PhieuGiamGia;
import com.example.DATN_WebFiveTus.repository.PhieuGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UpdateService {

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Scheduled(cron = "0 0 0 * * *") // Chạy vào 00:00:00 mỗi ngày
    public void updateAtMidnight() {
        List<PhieuGiamGia> vouchers = phieuGiamGiaRepository.findAll();

        for (PhieuGiamGia voucher : vouchers) {
            LocalDate ngayKetThuc = voucher.getNgayKetThuc().toLocalDate(); // Giả sử getNgayKetThuc() trả về LocalDate

            if (ngayKetThuc.isBefore(LocalDate.now()) || voucher.getSoLuong() <= 0) {
                voucher.setTrangThai("Đã kết thúc");
            }
        }

        phieuGiamGiaRepository.saveAll(vouchers);
    }
}
