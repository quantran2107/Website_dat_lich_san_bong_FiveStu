package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.DiaChiDTO;
import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.service.DiaChiService;
import com.example.DATN_WebFiveTus.service.KhachHangService;
import com.example.DATN_WebFiveTus.service.SanCaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("dia-chi")
public class DiaChiRest {
//    private DiaChiService diaChiService;
//    @Autowired
//    public DiaChiRest(DiaChiService diaChiService) {
//        this.diaChiService = diaChiService;
//    }
//
//    @GetMapping("hien-thi")
//    public ResponseEntity<List> getAll2(){
//        List<DiaChiDTO> listdiachi=diaChiService.getAllJoinFetch();
//        return ResponseEntity.ok(listdiachi);
//    }
}
