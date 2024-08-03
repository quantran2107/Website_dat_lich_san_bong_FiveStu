package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.dto.ThamSoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Controller
public class ThamSoController {

    private RestTemplate restTemplate;

    @Autowired
    public ThamSoController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("listThamSo")
    private String HienThi(Model model){
        model.addAttribute("listThamSo", Arrays.asList(restTemplate.getForObject(
                "http://localhost:8080/tham-so/hien-thi",
                ThamSoDTO[].class
        )));

        model.addAttribute("thamSo", new ThamSoDTO());
        return "list/quan-ly-tham-so";
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
