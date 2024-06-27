package com.example.DATN_WebFiveTus.rest;
import com.example.DATN_WebFiveTus.dto.DoThueDTO;
import com.example.DATN_WebFiveTus.service.DoThueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("do_thue")
public class DoThueRest {
    private DoThueService doThueService;

    public DoThueRest(DoThueService doThueService) {
        this.doThueService = doThueService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll2(){
        List<DoThueDTO>thueDTOList=doThueService.getAll();
        return ResponseEntity.ok(thueDTOList);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<SanCaDTO> getOne(@PathVariable("id") Integer id){
//        SanCaDTO sanCaDTODetail=sanCaService.getOne(id);
//        return ResponseEntity.ok(sanCaDTODetail);
//    }
//
//    @PostMapping("")
//    public ResponseEntity<SanCaDTO> save(@RequestBody SanCaDTO sanCaDTO){
//        SanCaDTO sanCaDTOSave=sanCaService.save(sanCaDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(sanCaDTOSave);
//    }
}
