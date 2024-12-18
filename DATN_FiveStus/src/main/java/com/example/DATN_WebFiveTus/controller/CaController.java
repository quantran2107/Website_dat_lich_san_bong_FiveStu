package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.dto.NgayTrongTuanDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.entity.Ca;
import com.example.DATN_WebFiveTus.rest.CaRest;
import com.example.DATN_WebFiveTus.service.CaService;
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
public class CaController {

    private CaService caService;

    private RestTemplate restTemplate;

    private CaRest caRest;

    @Autowired
    public CaController(CaService caService, RestTemplate restTemplate, CaRest caRest) {
        this.caService = caService;
        this.restTemplate = restTemplate;
        this.caRest = caRest;
    }

    @GetMapping("/listCa")
    public String HienThi(@RequestParam(name = "haha", required = false) Integer id,
                          @RequestParam(name = "keyWords", required = false) String keyWords,
                          @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                          @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection,
                          Model model) {
        List<CaDTO> listRest = caRest.getAll().getBody();
        Set<String> listTT = new HashSet<>(listRest.stream()
                .map(CaDTO::getTrangThai)
                .collect(Collectors.toList()));

        model.addAttribute("listTT",listTT);

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



        int[] totalPageElement = new int[2]; // Array to store total pages and total elements

        List<CaDTO> caDTOList;

        if (id != null || keyWords != null) {
            // Search by id if id is provided
            caDTOList = caService.searchKeyWords(pageNum, keyWords.trim(), sortDirection, totalPageElement, id);
        } else {
            // Otherwise, list all with pagination and sorting
            caDTOList = caService.listAllSortPage(pageNum, sortDirection, totalPageElement);
        }

        model.addAttribute("caDTOList", caDTOList);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("totalPages", totalPageElement[0]);
        model.addAttribute("totalElements", totalPageElement[1]);
        model.addAttribute("currentId", id);
        model.addAttribute("currentTrangThai", keyWords);


        return "/list/quan-ly-ca";
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
