package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.service.CaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("ca")
public class CaRest {

    private CaService caService;

    public CaRest(CaService caService) {
        this.caService = caService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll(){
        System.out.println(caService.getAll().toString());
        return ResponseEntity.ok(caService.getAllJoinFetch());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CaDTO> getOne(@PathVariable("id") Integer id){
        return ResponseEntity.ok(caService.getOne(id));
    }

    @PostMapping("")
    public ResponseEntity<CaDTO> save(@RequestBody CaDTO caDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(caService.save(caDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CaDTO> update(@PathVariable("id") Integer id ,@RequestBody CaDTO caDTO){
        return ResponseEntity.ok(caService.update(id,caDTO));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        caService.deletedAt(id);
        return ResponseEntity.ok().build();
    }
}