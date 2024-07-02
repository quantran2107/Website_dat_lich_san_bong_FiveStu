package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.dto.NgayTrongTuanDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.service.CaService;
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
public class CaController {

    private CaService caService;

    private RestTemplate restTemplate;

    @Autowired
    public CaController(CaService caService, RestTemplate restTemplate) {
        this.caService = caService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/listCa")
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

    @PostMapping("/ca/add")
    public String add(@ModelAttribute("ca") CaDTO caDTO) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:8080/ca");

        restTemplate.postForObject(builder.toUriString(), caDTO, Void.class);

        return "redirect:/listCa";
    }


    @GetMapping("/ca/edit/{id}")
    @ResponseBody
    public CaDTO edit(@PathVariable("id") Integer id) {
        System.out.println("Haahaaa");
        CaDTO caDTO = restTemplate.getForObject(
                "http://localhost:8080/ca/{id}",
                CaDTO.class,
                id
        );
        return caDTO;
    }



    @PostMapping("/ca/update")
    public String update(@ModelAttribute("ca") CaDTO caDTO) {
        System.out.println("HAHHA CA");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put("http://localhost:8080/ca/{id}", caDTO, caDTO.getId());
        return "redirect:/listCa";
    }

}
