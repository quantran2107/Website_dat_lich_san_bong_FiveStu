package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.exception.RoleNotFoundException;
import com.example.DATN_WebFiveTus.service.NhanVienService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("nhan-vien")
public class NhanVienRest {

    private NhanVienService nhanVienService;
    @Autowired
    public NhanVienRest(NhanVienService nhanVienService) {
        this.nhanVienService = nhanVienService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(nhanVienService.getAll());
    }

    @GetMapping("active")
    public ResponseEntity<?> getActive(){
        return ResponseEntity.ok(nhanVienService.getActiveNV());
    }

    @GetMapping("inactive")
    public ResponseEntity<?> getInactive(){
        return ResponseEntity.ok(nhanVienService.getInactiveNV());
    }


    @PutMapping("update")
    public ResponseEntity<Boolean> update(@RequestBody NhanVienDTO nv){
        return ResponseEntity.ok(nhanVienService.updateNew(nv));
    }

    @PostMapping("add")
    public ResponseEntity<?> add(@RequestBody NhanVienDTO nv) throws MessagingException, RoleNotFoundException {
        return ResponseEntity.ok(nhanVienService.addNew(nv));
    }
    @PostMapping("upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file){
        if(file.isEmpty()){
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(nhanVienService.addMore(file));
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getOne(@PathVariable("id") int id){
        return nhanVienService.getOneNv(id);
    }

    @GetMapping("search-for-code/{code}")
    public ResponseEntity<?> getNVForCode(@PathVariable("code") String maNV){
        return nhanVienService.getForCode(maNV);
    }

    @GetMapping("get-nv")
    public ResponseEntity<?> getNV(HttpServletRequest request){
        return ResponseEntity.ok(nhanVienService.getNV(request));
    }
}
