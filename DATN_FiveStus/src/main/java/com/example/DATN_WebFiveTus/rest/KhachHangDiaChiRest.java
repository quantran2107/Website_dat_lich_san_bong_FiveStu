package com.example.DATN_WebFiveTus.rest;
import com.example.DATN_WebFiveTus.dto.DiaChiKhachHangDTO;
import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.entity.DiaChiKhachHang;
import com.example.DATN_WebFiveTus.service.DiaChiKhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("dia-chi")
public class KhachHangDiaChiRest {

    private DiaChiKhachHangService diaChiKhachHangService;
    @Autowired

    public KhachHangDiaChiRest(DiaChiKhachHangService diaChiKhachHangService) {
        this.diaChiKhachHangService = diaChiKhachHangService;
    }
    @GetMapping("hien-thi")
    public ResponseEntity<List> GetAll2(){
        List<DiaChiKhachHangDTO>diaChiKhachHangDTOS=diaChiKhachHangService.getAll();
        return ResponseEntity.ok(diaChiKhachHangDTOS);
    }
    @GetMapping("/{khachHangId}")
    public ResponseEntity<List<DiaChiKhachHangDTO>> getAllDiaChiByKhachHangId(@PathVariable Integer khachHangId) {
        // Lấy danh sách địa chỉ của khách hàng theo ID
        List<DiaChiKhachHangDTO> diaChiList = diaChiKhachHangService.findById(khachHangId);

        // Sắp xếp danh sách theo createdAt (từ mới đến cũ)
        diaChiList.sort(Comparator.comparing(DiaChiKhachHangDTO::getCreatedAt).reversed());

        // Trả về danh sách đã sắp xếp
        return ResponseEntity.ok(diaChiList);
    }

    @PostMapping("/them/{khachHangId}")
    public ResponseEntity<String> addNewAddress(@PathVariable Integer khachHangId,
                                                @RequestBody DiaChiKhachHangDTO diaChiKhachHangDTO) {
        try {
            // Gán ID khách hàng cho địa chỉ mới
            diaChiKhachHangDTO.setIdKhachHang(khachHangId);

            // Gọi service để thêm địa chỉ
            diaChiKhachHangService.save(diaChiKhachHangDTO);

            return ResponseEntity.ok("Địa chỉ mới đã được thêm thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi thêm địa chỉ mới: " + e.getMessage());
        }
    }
    @GetMapping("/email/{email}")
    public List<DiaChiKhachHangDTO> getDiaChiByEmail(@PathVariable String email) {
        return diaChiKhachHangService.getDiaChiByEmail(email);
    }
    @GetMapping("/delete/{email}/{id}")
    public ResponseEntity<List<DiaChiKhachHangDTO>> deleteMem(@PathVariable String email, @PathVariable Integer id) {
        // Gọi dịch vụ để xoá mềm địa chỉ cụ thể
        diaChiKhachHangService.deleteDiaChiByEmail(email, id);

        // Lấy lại danh sách địa chỉ còn lại mà chưa bị xoá mềm
        List<DiaChiKhachHangDTO> diaChiKhachHangDTOS = diaChiKhachHangService.getDiaChiByEmail(email);
        return ResponseEntity.ok(diaChiKhachHangDTOS);
    }

    @PutMapping("/update/{email}/{id}")
    public ResponseEntity<DiaChiKhachHangDTO> updateDiaChiByEmail(@PathVariable String email,
                                                                  @PathVariable Integer id,
                                                                  @RequestBody DiaChiKhachHangDTO diaChiKhachHangDTO) {

        // Gọi dịch vụ để cập nhật địa chỉ
        DiaChiKhachHangDTO updatedDiaChi = diaChiKhachHangService.updateDiaChiByEmail(email, id, diaChiKhachHangDTO);
        return ResponseEntity.ok(updatedDiaChi);
    }

    @GetMapping("/find/{email}/{id}")
    public ResponseEntity<?> getDiaChiByEmailAndId(@PathVariable String email, @PathVariable Integer id) {
        Optional<DiaChiKhachHang> diaChi = diaChiKhachHangService.getDiaChiByEmailAndId(email, id);
        return ResponseEntity.ok(diaChi);
    }
    @PostMapping("/add/{email}")
    public ResponseEntity<DiaChiKhachHangDTO> addDiaChi(@PathVariable String email,
                                                        @RequestBody DiaChiKhachHangDTO diaChiKhachHangDTO) {
        DiaChiKhachHangDTO createdDiaChi = diaChiKhachHangService.addDiaChiByEmail(email, diaChiKhachHangDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdDiaChi); // Trả về địa chỉ mới với mã trạng thái 201
    }


}
