package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.HTTTDto;
import com.example.DATN_WebFiveTus.service.CTHTTTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("httt")
public class HinhThucThanhToanRest {

    @Autowired
    private CTHTTTService cthttService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getHtttForIdHD(@PathVariable int id) {
        return ResponseEntity.ok(cthttService.getHtttById(id));
    }

    @PostMapping("add")
    public ResponseEntity<?> addNew(@RequestBody HTTTDto htttDto){
        return ResponseEntity.ok(cthttService.addNew(htttDto));
    }

}
