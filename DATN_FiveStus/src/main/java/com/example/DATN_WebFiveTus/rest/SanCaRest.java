package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.service.SanCaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin("*")
@RestController
@RequestMapping("san-ca")
public class SanCaRest {

    private SanCaService sanCaService;

    @Autowired
    public SanCaRest(SanCaService sanCaService) {
        this.sanCaService = sanCaService;
    }


    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll2(){
        List<SanCaDTO> listSanCa=sanCaService.getAllJoinFetch();
        return ResponseEntity.ok(listSanCa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanCaDTO> getOne(@PathVariable("id") Integer id){
        SanCaDTO sanCaDTODetail=sanCaService.getOne(id);
       return ResponseEntity.ok(sanCaDTODetail);
    }

    @PostMapping("")
    public ResponseEntity<SanCaDTO> save(@RequestBody SanCaDTO sanCaDTO){
        SanCaDTO sanCaDTOSave=sanCaService.save(sanCaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(sanCaDTOSave);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanCaDTO> update(@PathVariable("id") Integer id ,@RequestBody SanCaDTO sanCaDTO){
        SanCaDTO sanCaDTOSave=sanCaService.update(id,sanCaDTO);
        return ResponseEntity.status(HttpStatus.OK).body(sanCaDTOSave);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        sanCaService.deletedAt(id);
       return ResponseEntity.ok().build();
    }
}
