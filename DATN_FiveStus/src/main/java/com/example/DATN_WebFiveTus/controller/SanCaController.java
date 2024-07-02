package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.dto.NgayTrongTuanDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.entity.SanCa;
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
public class SanCaController {

    private RestTemplate restTemplate;

    @Autowired
    public SanCaController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/listSanCa")
    public String HienThi(Model model) {
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

        model.addAttribute("listSC", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/san-ca/hien-thi",
                SanCaDTO[].class
        )));

        model.addAttribute("sanBong",new SanBongDTO());
        model.addAttribute("ca",new CaDTO());
        model.addAttribute("ngayTrongTuan",new NgayTrongTuanDTO());
        model.addAttribute("loaiSan",new LoaiSanDTO());
        model.addAttribute("sanCa",new SanCaDTO());
        return "/list/quan-ly-san-ca";
    }

//    @GetMapping("/sanCa/edit/{id}")
//    public String edit(@PathVariable("id") Integer id, Model model) {
//        System.out.println("Haahaaa");
//        SanCa sanCa= restTemplate.getForObject(
//                "http://localhost:8080/san-ca/{id}",
//                SanCa.class,
//                id
//        );
//        model.addAttribute("listSC", Arrays.asList(restTemplate.getForObject(
//                "http://localhost:8080/san-ca/hien-thi",
//                CaDTO[].class
//        )));
//
//        model.addAttribute("sanCa", sanCa);
//        return "list/quan-ly-san-ca";
//    }

    @PostMapping("/sanCa/add")
    public String add(@ModelAttribute("sanCa") SanCaDTO sanCaDTO) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:8080/san-ca");

        restTemplate.postForObject(builder.toUriString(), sanCaDTO, Void.class);

        return "redirect:/listSanCa";
    }

    @GetMapping("/sanCa/edit/{id}")
    @ResponseBody
    public SanCaDTO edit(@PathVariable("id") Integer id) {
        System.out.println("Haahaaa");
        SanCaDTO sanCaDTO = restTemplate.getForObject(
                "http://localhost:8080/san-ca/{id}",
                SanCaDTO.class,
                id
        );
        System.out.println("Gia: " + sanCaDTO.getGia());
        return sanCaDTO;
    }

    @PostMapping("/sanCa/update")
    public String update(@ModelAttribute("sanCa") SanCaDTO sanCaDTO) {
        System.out.println("UpdATE");
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.put("http://localhost:8080/san-ca/{id}", sanCaDTO, sanCaDTO.getId());
        System.out.println("Giá: "+sanCaDTO.getGia());

        return "redirect:/listSanCa";
    }
}
