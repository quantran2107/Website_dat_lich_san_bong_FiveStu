package com.example.DATN_WebFiveTus.rest;


import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.entity.SanBong;
import com.example.DATN_WebFiveTus.service.SanBongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("san-bong")
public class SanBongRest {

    private SanBongService sanBongService;

    @Autowired
    public SanBongRest(SanBongService sanBongService) {
        this.sanBongService = sanBongService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll() {
        return ResponseEntity.ok(sanBongService.getAllJoinFetch());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanBongDTO> getOne(@PathVariable("id") Integer id){
        SanBongDTO sanBongDTODetail=sanBongService.getOne(id);
        return ResponseEntity.ok(sanBongDTODetail);
    }

    @PostMapping("")
    public ResponseEntity<SanBongDTO> save(@RequestBody SanBongDTO sanBongDTO){
        SanBongDTO sanBongDTOSave=sanBongService.save(sanBongDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(sanBongDTOSave);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanBongDTO> update(@PathVariable("id") Integer id ,@RequestBody SanBongDTO sanBongDTO){
        SanBongDTO sanBongDTODetail=sanBongService.update(id,sanBongDTO);
        return ResponseEntity.ok(sanBongDTODetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        sanBongService.deletedAt(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/hien-thi-pages")
    public ResponseEntity<Page<SanBongDTO>> getAll2(
            @RequestParam(value = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "2") Integer pageSize) {


        return ResponseEntity.ok(sanBongService.pages(pageNo,pageSize));
    }
//    Ly them ham getByID
    @GetMapping("/findByIdLoaiSan/{id}")
    public ResponseEntity<List<SanBongDTO>> getSanBongsByLoaiSan(@PathVariable("id") Integer loaiSanId){
        List<SanBongDTO> sanBongs = sanBongService.getSanBongsByLoaiSanId(loaiSanId);
        return ResponseEntity.ok(sanBongs);
    }

    @GetMapping("/findByName")
    public ResponseEntity<SanBongDTO> getSanBongByName(@RequestParam("tenSanBong") String tenSanBong){
        SanBongDTO sanBongDTO = sanBongService.getSanBongByName(tenSanBong);
        return ResponseEntity.ok(sanBongDTO);
    }

    @GetMapping("existss")
    public ResponseEntity<Boolean> existsByTenLoaiSan(@RequestParam Integer idLoaiSan,@RequestParam String tenSanBong) {
        Boolean exists = sanBongService.existsByTenSanBongs(idLoaiSan,tenSanBong);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/getListSanBongWithIdLoaiSan")
    public ResponseEntity<List<SanBongDTO>> getListSanBongWithIdLoaiSan(@RequestParam("idLoaiSan") Integer idLoaiSan){
        List<SanBongDTO> sanBongs = sanBongService.getListSanBongWithIdLoaiSan(idLoaiSan);
        return ResponseEntity.ok(sanBongs);
    }
    @GetMapping("/checkTrungSanBong")
    public ResponseEntity<Boolean> checkTrungSanBong(@RequestParam Integer idLoaiSan, @RequestParam String tenSanBong) {
        boolean isExist = sanBongService.checkTrungSanBong(idLoaiSan, tenSanBong);
        return ResponseEntity.ok(isExist); // Trả về true nếu trùng, false nếu không trùng
    }

    @PutMapping("/updateTrangThai/{id}")
    public ResponseEntity<String> updateTrangThai(@PathVariable("id") Integer id,
                                                  @RequestParam("status") String status) {
        try {
            sanBongService.updateTrangThai(id, status);
            return ResponseEntity.ok("Trạng thái đã được cập nhật thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }
    }


}
