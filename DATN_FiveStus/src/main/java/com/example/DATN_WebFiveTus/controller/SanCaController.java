package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.dto.NgayTrongTuanDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.entity.SanCa;
import com.example.DATN_WebFiveTus.rest.SanCaRest;
import com.example.DATN_WebFiveTus.service.LoaiSanService;
import com.example.DATN_WebFiveTus.service.NgayTrongTuanService;
import com.example.DATN_WebFiveTus.service.SanCaService;
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
public class SanCaController {

    private RestTemplate restTemplate;

    private SanCaRest sanCaRest;

    private SanCaService sanCaService;

    @Autowired
    public SanCaController(RestTemplate restTemplate, SanCaRest sanCaRest, SanCaService sanCaService) {
        this.restTemplate = restTemplate;
        this.sanCaRest = sanCaRest;
        this.sanCaService = sanCaService;
    }

    @GetMapping("listSanCa")
    public String listSanCa(
            @RequestParam(name = "keyWords", required = false) Integer id,
            @RequestParam(name = "keyWords", required = false) String keyWords,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection,
            Model model) {

        List<SanCaDTO> listRest = sanCaRest.getAll2().getBody();
        Set<String> listTT = new HashSet<>(listRest.stream()
                .map(SanCaDTO::getTrangThai)
                .collect(Collectors.toList()));

        model.addAttribute("listTT",listTT);


        model.addAttribute("listTT",listTT);

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

        model.addAttribute("sanBong", new SanBongDTO());
        model.addAttribute("ca", new CaDTO());
        model.addAttribute("ngayTrongTuan", new NgayTrongTuanDTO());
        model.addAttribute("loaiSan", new LoaiSanDTO());
        model.addAttribute("sanCa", new SanCaDTO());

        int[] totalPageElement = new int[2]; // Array to store total pages and total elements

        List<SanCaDTO> sanCaDTOList;

        if (id != null || keyWords != null) {
            // Search by id if id is provided
            sanCaDTOList = sanCaService.searchKeyWords(pageNum, keyWords.trim(), sortDirection, totalPageElement, id);
        } else {
            // Otherwise, list all with pagination and sorting
            sanCaDTOList = sanCaService.listAll2(pageNum, sortDirection, totalPageElement);
        }

        // Add attributes to the model
        model.addAttribute("sanCaDTOList", sanCaDTOList);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("totalPages", totalPageElement[0]);
        model.addAttribute("totalElements", totalPageElement[1]);
        model.addAttribute("currentId", id);
        model.addAttribute("currentTrangThai", keyWords);

        return "/list/quan-ly-san-ca";
    }



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


        return "redirect:/listSanCa";
    }


}


//
//    @GetMapping("/listSanCa")
//    public String HienThi(Model model) {
//        List<SanCaDTO> listRest = sanCaRest.getAll2().getBody();
//        Set<String> listTT = new HashSet<>(listRest.stream()
//                .map(SanCaDTO::getTrangThai)
//                .collect(Collectors.toList()));
//
//        model.addAttribute("listTT",listTT);
//
//        model.addAttribute("listLS", Arrays.asList(restTemplate.getForObject(
//                "http://localhost:8080/loai-san/hien-thi",
//                LoaiSanDTO[].class
//        )));
//
//        model.addAttribute("listNTT", Arrays.asList(restTemplate.getForObject(
//                "http://localhost:8080/ngay-trong-tuan/hien-thi",
//                NgayTrongTuanDTO[].class
//        )));
//
//        model.addAttribute("listC", Arrays.asList(restTemplate.getForObject(
//                "http://localhost:8080/ca/hien-thi",
//                CaDTO[].class
//        )));
//
//        model.addAttribute("listSB", Arrays.asList(restTemplate.getForObject(
//                "http://localhost:8080/san-bong/hien-thi",
//                SanBongDTO[].class
//        )));
//
//        model.addAttribute("listSC", Arrays.asList(restTemplate.getForObject(
//                "http://localhost:8080/san-ca/hien-thi",
//                SanCaDTO[].class
//        )));
//
//        model.addAttribute("sanBong",new SanBongDTO());
//        model.addAttribute("ca",new CaDTO());
//        model.addAttribute("ngayTrongTuan",new NgayTrongTuanDTO());
//        model.addAttribute("loaiSan",new LoaiSanDTO());
//        model.addAttribute("sanCa",new SanCaDTO());
//        return "/list/quan-ly-san-ca";
//    }
