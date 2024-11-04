package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.dto.NgayTrongTuanDTO;
import com.example.DATN_WebFiveTus.service.NgayTrongTuanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("ngay-trong-tuan")
public class NgayTrongTuanRest {

    private NgayTrongTuanService ngayTrongTuanService;

    @Autowired
    public NgayTrongTuanRest(NgayTrongTuanService ngayTrongTuanService) {
        this.ngayTrongTuanService = ngayTrongTuanService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll(){
        return ResponseEntity.ok(ngayTrongTuanService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NgayTrongTuanDTO> getOne(@PathVariable("id") Integer id){
        return ResponseEntity.ok(ngayTrongTuanService.getOne(id));
    }

    @PostMapping("")
    public ResponseEntity<NgayTrongTuanDTO> save(@RequestBody NgayTrongTuanDTO ngayTrongTuanDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(ngayTrongTuanService.save(ngayTrongTuanDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NgayTrongTuanDTO> update(@PathVariable("id") Integer id ,@RequestBody NgayTrongTuanDTO ngayTrongTuanDTO){
        return ResponseEntity.ok(ngayTrongTuanService.update(id,ngayTrongTuanDTO));
    }

    @GetMapping("/tim-kiem-ngay-trong-tuan")
    public NgayTrongTuanDTO findByNgayTrongTuan(@RequestParam(defaultValue = "false") String thuTrongTuan){
        return ngayTrongTuanService.findByNgayTrongTuan(thuTrongTuan);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
//        ngayTrongTuanService.deletedAt(id);
//        return ResponseEntity.ok().build();
//    }
}
