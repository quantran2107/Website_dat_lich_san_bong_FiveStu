package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.ThamSoDTO;
import com.example.DATN_WebFiveTus.service.ThamSoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("tham-so")
public class ThamSoRest {

    private ThamSoService thamSoService;

    @Autowired
    public ThamSoRest(ThamSoService thamSoService) {
        this.thamSoService = thamSoService;
    }

    @GetMapping("hien-thi")
    private ResponseEntity<List> HienThi(){
        return ResponseEntity.ok(thamSoService.getAll());
    }

    @GetMapping("/{id}")
    private ResponseEntity<ThamSoDTO> getOne(@PathVariable("id") Integer id){
        return ResponseEntity.ok(thamSoService.getOne(id));
    }

    @PostMapping
    private ResponseEntity<ThamSoDTO> save(@RequestBody ThamSoDTO thamSoDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(thamSoService.save(thamSoDTO));
    }

    @PutMapping("/{id}")
    private ResponseEntity<ThamSoDTO> update(@RequestBody ThamSoDTO thamSoDTO,@PathVariable("id") Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(thamSoService.update(thamSoDTO,id));
    }

    @GetMapping("/haha")
    public String test(){
        return "haha";
    }
}
