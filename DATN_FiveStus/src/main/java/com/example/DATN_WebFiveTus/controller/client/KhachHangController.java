package com.example.DATN_WebFiveTus.controller.client;

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
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class KhachHangController {

    @Autowired
    private KhachHangService khachHangService;
    @Autowired
    private DiaChiKhachHangService diaChiKhachHangService;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/quan-ly-khach-hang")
    public String hienThiKhachHang(){
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
            // Lấy thông tin khách hàng theo id
            KhachHangDTO khachHangDTO = khachHangService.findById(id);

            // Tạo đối tượng Pageable để phân trang địa chỉ khách hàng
            Pageable pageable = PageRequest.of(pageDiaChi, sizeDiaChi, Sort.by("createdAt").descending()); // Sử dụng pageDiaChi và sizeDiaChi từ request

            // Lấy tất cả địa chỉ của khách hàng theo id mà không lọc deletedAt
            Page<DiaChiKhachHangDTO> diaChiPage = diaChiKhachHangService.findByIdDC(id, pageable);

            // Lọc các địa chỉ có deleteAt = false (chỉ lấy những địa chỉ không bị xóa)
            List<DiaChiKhachHangDTO> diaChiList = diaChiPage.getContent().stream()
                    .filter(diaChi -> Boolean.FALSE.equals(diaChi.getDeletedAt()))  // Kiểm tra điều kiện deleteAt = false
                    .collect(Collectors.toList());

            // Kiểm tra nếu danh sách địa chỉ rỗng và trang hiện tại > 0, thì chuyển đến trang trước
            if (diaChiList.isEmpty() && pageDiaChi > 0) {
                return "redirect:/quan-ly-khach-hang-detail?id=" + id + "&page=" + (pageDiaChi - 1); // Sửa đổi thành pageDiaChi - 1
            }

            // Tính tổng số trang dựa trên số lượng tất cả các địa chỉ không bị xóa (không phải số lượng đã lọc)
            int totalPages = (int) Math.ceil((double) diaChiPage.getTotalElements() / sizeDiaChi);

            // Thêm các đối tượng vào model để truyền dữ liệu vào view
            model.addAttribute("khachHang", khachHangDTO);
            model.addAttribute("diaChiList", diaChiList);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("currentPage", pageDiaChi);
            model.addAttribute("id", id);
            model.addAttribute("sizeDiaChi", sizeDiaChi);
        }

        // Trả về view để hiển thị danh sách địa chỉ khách hàng
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
    @GetMapping("/quan-ly-khach-hang/kiem-tra-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam("email") String email) {
        try {
            boolean exists = khachHangService.isEmailExists(email);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

    @GetMapping("/quan-ly-khach-hang/kiem-tra-so-dien-thoai")
    public ResponseEntity<Boolean> checkSoDienThoaiExists(@RequestParam("soDienThoai") String soDienThoai) {
        try {
            boolean exists = khachHangService.isSoDienThoaiExists(soDienThoai);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }
    @GetMapping("/quan-ly-khach-hang/kiem-tra-ma-khach-hang")
    public ResponseEntity<Boolean> checkMaKhachHangExists(@RequestParam("maKhachHang") String maKhachHang) {
        try {
            boolean exists = khachHangService.isMaKhachHangExists(maKhachHang);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(false);
        }
    }

}


