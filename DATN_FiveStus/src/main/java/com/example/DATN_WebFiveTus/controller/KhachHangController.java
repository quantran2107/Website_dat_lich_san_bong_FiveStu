package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.DiaChiKhachHangDTO;
import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.service.DiaChiKhachHangService;
import com.example.DATN_WebFiveTus.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;
    @Autowired
    private DiaChiKhachHangService diaChiKhachHangService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/quan-ly-khach-hang")
    public String hienThiKhachHang(Model model, @RequestParam(name = "page", defaultValue = "0") int page,
                                   @RequestParam(name = "size", defaultValue = "5") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<KhachHangDTO> khachHangDTOS = khachHangService.getAll(pageable);

        model.addAttribute("page", khachHangDTOS);
        model.addAttribute("khachHangDTO", new KhachHangDTO());
        model.addAttribute("diaChiDTO", new DiaChiKhachHangDTO());

        return "/list/quan-ly-khach-hang";
    }


    @PostMapping("/quan-ly-khach-hang/them")
    public ResponseEntity<String> addKhachHang(@ModelAttribute KhachHangDTO khachHangDTO) {
        try {
            khachHangService.save(khachHangDTO);
            return ResponseEntity.ok("Thêm khách hàng thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi khi thêm khách hàng");
        }
    }



    @GetMapping("/quan-ly-khach-hang-detail")
    public String detailHTKH(@RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "page", defaultValue = "0") int pageDiaChi,
                             @RequestParam(value = "sizeDiaChi", defaultValue = "3") int sizeDiaChi,
                             Model model) {
        if (id != null) {
            KhachHangDTO khachHangDTO = khachHangService.findById(id);
            Pageable pageable = PageRequest.of(pageDiaChi, sizeDiaChi, Sort.by("createdAt").descending()); // Sử dụng pageDiaChi và sizeDiaChi từ request
            Page<DiaChiKhachHangDTO> diaChiPage = diaChiKhachHangService.findByIdDC(id, pageable);
            List<DiaChiKhachHangDTO> diaChiList = new ArrayList<>(diaChiPage.getContent());

            if (diaChiList.isEmpty() && pageDiaChi > 0) {
                return "redirect:/quan-ly-khach-hang-detail?id=" + id + "&page=" + (pageDiaChi - 1); // Sửa đổi thành pageDiaChi - 1
            }

//            Collections.reverse(diaChiList);

            model.addAttribute("khachHang", khachHangDTO);
            model.addAttribute("diaChiList", diaChiList);
            model.addAttribute("totalPages", diaChiPage.getTotalPages());
            model.addAttribute("currentPage", pageDiaChi);
            model.addAttribute("id", id); // Chỉ thêm id vào model nếu nó tồn tại
            model.addAttribute("sizeDiaChi", sizeDiaChi); // Thêm size vào model để nó có thể truyền lại vào view
        }

        return "list/quan-ly-dia-chi-khach-hang";
    }

        @PostMapping("/quan-ly-khach-hang/cap-nhat")
        public ResponseEntity<String> updateKH(@ModelAttribute KhachHangDTO khachHangDTO) {
            try {
                khachHangService.update(khachHangDTO.getId(), khachHangDTO);
                return ResponseEntity.ok("Cập nhật thông tin thành công!");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Đã xảy ra lỗi khi cập nhật thông tin");
            }
        }

}


