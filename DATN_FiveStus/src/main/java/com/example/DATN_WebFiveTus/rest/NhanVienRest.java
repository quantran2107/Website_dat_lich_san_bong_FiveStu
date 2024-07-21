package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.service.NhanVienService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("nhan-vien")
public class NhanVienRest {

    private NhanVienService nhanVienService;
    @Autowired
    public NhanVienRest(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll(){
        return ResponseEntity.ok(nhanVienService.getAll());
    }

    @GetMapping("active")
    public ResponseEntity<List> getActive(){
        return ResponseEntity.ok(nhanVienService.getActiveNV());
    }

    @GetMapping("inactive")
    public ResponseEntity<List> getInactive(){
        return ResponseEntity.ok(nhanVienService.getInactiveNV());
    }


    @PutMapping("update")
    public ResponseEntity<Boolean> update(@RequestBody NhanVienDTO nv){
        return ResponseEntity.ok(nhanVienService.updateNew(nv));
    }

    @PostMapping("add")
    public ResponseEntity<Boolean> add(@RequestBody NhanVienDTO nv) throws MessagingException {
        return ResponseEntity.ok(nhanVienService.addNew(nv));
    }
    @PostMapping("upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(nhanVienService.addMore(file));
    }
}
