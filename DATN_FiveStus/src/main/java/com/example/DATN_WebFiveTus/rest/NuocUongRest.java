package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.NuocUongDTO;
import com.example.DATN_WebFiveTus.service.NuocUongService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("nuoc_uong")
public class NuocUongRest {
    private NuocUongService nuocUongService;

    public NuocUongRest(NuocUongService nuocUongService) {
        this.nuocUongService = nuocUongService;
    }
    @GetMapping("hien-thi")
    public ResponseEntity<List>GetAll2(){
        List<NuocUongDTO>nuocUongDTOList=nuocUongService.getAll();
        return ResponseEntity.ok(nuocUongDTOList);
    }
}
