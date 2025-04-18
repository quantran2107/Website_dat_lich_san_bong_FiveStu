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
import com.example.DATN_WebFiveTus.service.SanBongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class SanBongController {

    private RestTemplate restTemplate;

    private SanBongRest sanBongRest;

    private LoaiSanRest loaiSanRest;

    private CaRest caRest;

    private NgayTrongTuanRest ngayTrongTuanRest;

    private SanBongService sanBongService;

    @Autowired
    public SanBongController(RestTemplate restTemplate, SanBongRest sanBongRest, LoaiSanRest loaiSanRest, CaRest caRest,
                             NgayTrongTuanRest ngayTrongTuanRest, SanBongService sanBongService) {
        this.restTemplate = restTemplate;
        this.sanBongRest = sanBongRest;
        this.loaiSanRest = loaiSanRest;
        this.caRest = caRest;
        this.ngayTrongTuanRest = ngayTrongTuanRest;
        this.sanBongService = sanBongService;
    }

    @GetMapping("/timTenSanBongTheoIdLoaiSan")
    public String getSanBongsByLoaiSanIds(@RequestParam("idLoaiSan") Integer idLoaiSan, Model model) {
        System.out.println("Mtam77");
        List<SanBongDTO> sanBongs = sanBongService.getSanBongsByLoaiSanId(idLoaiSan);

        model.addAttribute("listSB", sanBongs);
        System.out.println("Haha: "+sanBongs);
        return "quan-ly-san-bong"; // Thay thế "list-san-bong" bằng tên view của bạn
    }


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

    @GetMapping("/listSanBong")
    public String HienThi(Model model) {

        List<LoaiSanDTO> listRest1 = loaiSanRest.getAll().getBody();
        Set<String> listTTLS = new HashSet<>(listRest1.stream()
                .map(LoaiSanDTO::getTrangThai)
                .collect(Collectors.toList()));

        model.addAttribute("listTTLS",listTTLS);

        List<SanBongDTO> listRest2 = sanBongRest.getAll().getBody();
        Set<String> listTTSB = new HashSet<>(listRest2.stream()
                .map(SanBongDTO::getTrangThai)
                .collect(Collectors.toList()));

        model.addAttribute("listTTSB",listTTSB);

        List<CaDTO> listRest3 = caRest.getAll().getBody();
        Set<String> listTTC = new HashSet<>(listRest3.stream()
                .map(CaDTO::getTrangThai)
                .collect(Collectors.toList()));

        model.addAttribute("listTTC",listTTC);

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

        model.addAttribute("listSC", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/san-ca/hien-thi",
                SanCaDTO[].class
        )));

        model.addAttribute("sanCa",new SanCaDTO());
        model.addAttribute("sanBong",new SanBongDTO());
        model.addAttribute("ca",new CaDTO());
        model.addAttribute("ngayTrongTuan",new NgayTrongTuanDTO());
        model.addAttribute("loaiSan",new LoaiSanDTO());
        model.addAttribute("sanCa", new SanCaDTO());
        return "/list/quan-ly-san-bong3";
    }

}

