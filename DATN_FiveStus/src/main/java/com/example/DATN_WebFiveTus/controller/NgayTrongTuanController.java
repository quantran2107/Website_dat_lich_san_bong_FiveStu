package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.dto.NgayTrongTuanDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.service.NgayTrongTuanService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class NgayTrongTuanController {

    private NgayTrongTuanService ngayTrongTuanService;

    private RestTemplate restTemplate;

    @Autowired
    public NgayTrongTuanController(NgayTrongTuanService ngayTrongTuanService, RestTemplate restTemplate) {
        this.ngayTrongTuanService = ngayTrongTuanService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/listNgayTrongTuan")
    public String HienThi(Model model) {

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

        model.addAttribute("listLS", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/loai-san/hien-thi",
                LoaiSanDTO[].class
        )));


        model.addAttribute("sanBong",new SanBongDTO());
        model.addAttribute("loaiSan",new LoaiSanDTO());
        model.addAttribute("ca",new CaDTO());
        model.addAttribute("ngayTrongTuan",new NgayTrongTuanDTO());
        return "/list/quan-ly-san-bong";
    }

//    @GetMapping("/ngayTrongTuan/edit/{id}")
//    public String edit(@PathVariable("id") Integer id, Model model) {
//
//        NgayTrongTuanDTO  ngayTrongTuanDTO= restTemplate.getForObject(
//                "http://localhost:8080/ngayTrongTuan/{id}",
//                NgayTrongTuanDTO.class,
//                id
//        );
//        model.addAttribute("listNTT", Arrays.asList(restTemplate.getForObject(
//                "http://localhost:8080/ngay-trong-tuan/hien-thi",
//                CaDTO[].class
//        )));
//
//        model.addAttribute("ngayTrongTuanDTO", ngayTrongTuanDTO);
//        return "list/update-ngay-trong-tuan";
//    }

    @PostMapping("/ngayTrongTuan/add")
    public String add(@ModelAttribute("ngayTrongTuan") NgayTrongTuanDTO ngayTrongTuanDTO) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:8080/ngay-trong-tuan");

        restTemplate.postForObject(builder.toUriString(), ngayTrongTuanDTO, Void.class);

        return "redirect:/listSanBong";
    }

    @GetMapping("/ngayTrongTuan/edit/{id}")
    @ResponseBody
    public NgayTrongTuanDTO edit(@PathVariable("id") Integer id) {
        NgayTrongTuanDTO ngayTrongTuanDTO = restTemplate.getForObject(
                "http://localhost:8080/ngay-trong-tuan/{id}",
                NgayTrongTuanDTO.class,
                id
        );
        System.out.println("HAHA321: "+ngayTrongTuanDTO.getId());
        return ngayTrongTuanDTO;
    }
    @PostMapping("/ngayTrongTuan/update")
    public String update(@ModelAttribute("ngayTrongTuan") NgayTrongTuanDTO ngayTrongTuanDTO) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put("http://localhost:8080/ngay-trong-tuan/{id}", ngayTrongTuanDTO, ngayTrongTuanDTO.getId());
        return "redirect:/listNgayTrongTuan";
    }
}
