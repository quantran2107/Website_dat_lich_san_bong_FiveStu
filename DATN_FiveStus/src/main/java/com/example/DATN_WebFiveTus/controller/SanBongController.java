package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.dto.NgayTrongTuanDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.service.SanBongService;
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
public class SanBongController {

    private SanBongService sanBongService;

    private RestTemplate restTemplate;

    @Autowired
    public SanBongController(SanBongService sanBongService, RestTemplate restTemplate) {
        this.sanBongService = sanBongService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/listSanBong")
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

        model.addAttribute("sanBong",new SanBongDTO());
        model.addAttribute("ca",new CaDTO());
        model.addAttribute("ngayTrongTuan",new NgayTrongTuanDTO());
        model.addAttribute("loaiSan",new LoaiSanDTO());
        return "/list/quan-ly-san-bong";
    }

//    @GetMapping("/sanBong/edit/{id}")
//    public String edit(@PathVariable("id") Integer id, Model model) {
//
//        SanBongDTO  sanBong= restTemplate.getForObject(
//                "http://localhost:8080/san-bong/{id}",
//                SanBongDTO.class,
//                id
//        );
//        model.addAttribute("listSB", Arrays.asList(restTemplate.getForObject(
//                "http://localhost:8080/san-bong/hien-thi",
//                SanBongDTO[].class
//        )));
//
//        model.addAttribute("sanBong", sanBong);
//        return "list/update-san-bong";
//    }

    @PostMapping("/sanBong/add")
    public String add(@ModelAttribute("sanBong") SanBongDTO sanBongDTO) {
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:8080/san-bong");

        restTemplate.postForObject(builder.toUriString(), sanBongDTO, Void.class);

        return "redirect:/listSanBong";
    }

    @GetMapping("/sanBong/edit/{id}")
    @ResponseBody
    public SanBongDTO edit(@PathVariable("id") Integer id) {
        System.out.println("Haahaaa");
        SanBongDTO sanBongDTO = restTemplate.getForObject(
                "http://localhost:8080/san-bong/{id}",
                SanBongDTO.class,
                id
        );
        System.out.println("ID của toi: "+sanBongDTO.getId());
        return sanBongDTO;
    }


    @PostMapping("/sanBong/update")
    public String update(@ModelAttribute("sanBong") SanBongDTO sanBongDTO) {
        System.out.println("hahaupdate");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put("http://localhost:8080/san-bong/{id}", sanBongDTO, sanBongDTO.getId());
        System.out.println("Haha:"+sanBongDTO.getId());
        return "redirect:/listSanBong";
    }
}
