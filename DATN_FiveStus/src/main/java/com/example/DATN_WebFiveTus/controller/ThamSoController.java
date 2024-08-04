package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.dto.ThamSoDTO;
import com.example.DATN_WebFiveTus.service.ThamSoService;
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
import java.util.List;

@Controller
public class ThamSoController {

    private RestTemplate restTemplate;

    private ThamSoService thamSoService;

    @Autowired
    public ThamSoController(RestTemplate restTemplate, ThamSoService thamSoService) {
        this.restTemplate = restTemplate;
        this.thamSoService = thamSoService;
    }

    @GetMapping("listThamSo")
    private String HienThi(@RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                           @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection
            , Model model) {
        model.addAttribute("listThamSo", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/tham-so/hien-thi",
                ThamSoDTO[].class
        )));

        int[] totalPageElement = new int[2]; // Array to store total pages and total elements

        List<ThamSoDTO> thamSoDTOList;

        // Otherwise, list all with pagination and sorting
        thamSoDTOList = thamSoService.listAllSortPage(pageNum, sortDirection, totalPageElement);


        model.addAttribute("thamSoDTOList", thamSoDTOList);
        model.addAttribute("currentPage", pageNum);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("totalPages", totalPageElement[0]);
        model.addAttribute("totalElements", totalPageElement[1]);
        model.addAttribute("thamSo", new ThamSoDTO());
        return "list/quan-ly-tham-so";
    }

//    @GetMapping("listThamSo")
//    private String HienThi(Model model){
//        model.addAttribute("listThamSo", Arrays.asList(restTemplate.getForObject(
//                "http://localhost:8080/tham-so/hien-thi",
//                ThamSoDTO[].class
//        )));
//
//        model.addAttribute("thamSo", new ThamSoDTO());
//        return "list/quan-ly-tham-so";
//    }

    @PostMapping("/thamSo/add")
    public String add(@ModelAttribute("thamSo") ThamSoDTO thamSoDTO) {
        System.out.println("ADD HAHA321");
        RestTemplate restTemplate = new RestTemplate();

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("http://localhost:8080/tham-so");

        restTemplate.postForObject(builder.toUriString(), thamSoDTO, Void.class);

        return "redirect:/listThamSo";
    }

    @GetMapping("/thamSo/edit/{id}")
    @ResponseBody
    public ThamSoDTO edit(@PathVariable("id") Integer id) {
        ThamSoDTO thamSoDTO = restTemplate.getForObject(
                "http://localhost:8080/tham-so/{id}",
                ThamSoDTO.class,
                id
        );
        return thamSoDTO;
    }

    @PostMapping("/thamSo/update")
    public String update(@ModelAttribute("thamSo") ThamSoDTO thamSoDTO) {

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put("http://localhost:8080/tham-so/{id}", thamSoDTO, thamSoDTO.getId());
        return "redirect:/listThamSo";
    }

}
