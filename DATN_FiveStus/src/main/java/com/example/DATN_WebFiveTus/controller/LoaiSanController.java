package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.dto.NgayTrongTuanDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.rest.CaRest;
import com.example.DATN_WebFiveTus.rest.LoaiSanRest;
import com.example.DATN_WebFiveTus.rest.NgayTrongTuanRest;
import com.example.DATN_WebFiveTus.rest.SanBongRest;
import com.example.DATN_WebFiveTus.service.LoaiSanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller

public class LoaiSanController {

    private RestTemplate restTemplate;

    private SanBongRest sanBongRest;

    private LoaiSanRest loaiSanRest;

    private CaRest caRest;

    private NgayTrongTuanRest ngayTrongTuanRest;

    @Autowired
    public LoaiSanController(RestTemplate restTemplate, SanBongRest sanBongRest, LoaiSanRest loaiSanRest, CaRest caRest, NgayTrongTuanRest ngayTrongTuanRest) {
        this.restTemplate = restTemplate;
        this.sanBongRest = sanBongRest;
        this.loaiSanRest = loaiSanRest;
        this.caRest = caRest;
        this.ngayTrongTuanRest = ngayTrongTuanRest;
    }

    @GetMapping("/listLoaiSan")
    public String HienThi(Model model) {
        List<LoaiSanDTO> listRest = loaiSanRest.getAll().getBody();
        Set<String> listTTLS = new HashSet<>(listRest.stream()
                .map(LoaiSanDTO::getTrangThai)
                .collect(Collectors.toList()));

        model.addAttribute("listTTLS",listTTLS);

        model.addAttribute("listLS", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/loai-san/hien-thi",
                LoaiSanDTO[].class
        )));

        model.addAttribute("listNTT", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/ngay-trong-tuan/hien-thi",
                NgayTrongTuanDTO[].class
        )));

        model.addAttribute("listC", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/ca/hien-thi",
                CaDTO[].class
        )));

        model.addAttribute("listSB", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/san-bong/hien-thi",
                SanBongDTO[].class
        )));

        model.addAttribute("sanBong",new SanBongDTO());
        model.addAttribute("ca",new CaDTO());
        model.addAttribute("ngayTrongTuan",new NgayTrongTuanDTO());
        model.addAttribute("loaiSan",new LoaiSanDTO());
        return "/list/quan-ly-san-bong";
    }

    @PostMapping("/loaiSan/add")
    public String add(@ModelAttribute("loaiSan") LoaiSanDTO loaiSanDTO) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:8080/loai-san");

        restTemplate.postForObject(builder.toUriString(), loaiSanDTO, Void.class);

        return "redirect:/listSanBong";
    }

//    @GetMapping("/loaiSan/edit/{id}")
//    public String edit(@PathVariable("id") Integer id, Model model) {
//        System.out.println("Haitam77");
//        LoaiSanDTO loaiSan = restTemplate.getForObject(
//                "http://localhost:8080/loai-san/{id}",
//                LoaiSanDTO.class,
//                id
//        );
//        model.addAttribute("listLS", Arrays.asList(restTemplate.getForObject(
//                "http://localhost:8080/loai-san/hien-thi",
//                LoaiSanDTO[].class
//        )));
//
//
//        model.addAttribute("loaiSan", loaiSan);
//        return "list/update-loai-san";
//    }

    @GetMapping("/loaiSan/edit/{id}")
    @ResponseBody
    public LoaiSanDTO edit(@PathVariable("id") Integer id) {
        System.out.println("Haahaaa");
        LoaiSanDTO loaiSanDTO = restTemplate.getForObject(
                "http://localhost:8080/loai-san/{id}",
                LoaiSanDTO.class,
                id
        );

        return loaiSanDTO;
    }

    @PostMapping("/loaiSan/update")
    public String update(@ModelAttribute("loaiSan") LoaiSanDTO loaiSanDTO) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.put("http://localhost:8080/loai-san/{id}", loaiSanDTO, loaiSanDTO.getId());

        return "redirect:/listLoaiSan";
    }


}