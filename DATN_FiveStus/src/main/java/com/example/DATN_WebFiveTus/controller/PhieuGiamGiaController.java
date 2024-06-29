package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.rest.PhieuGiamGiaRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
//@RequestMapping("/web/")
public class PhieuGiamGiaController {
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping("/list")
    public String listPhieuGiamGia(Model model, @RequestParam(defaultValue = "0") int page) {
        String apiUrl = "http://localhost:8080/api/phan-trang";

        ResponseEntity<PagedModel<EntityModel<PhieuGiamGiaDTO>>> responseEntity = restTemplate.exchange(
                apiUrl + "?page={page}&size={size}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<EntityModel<PhieuGiamGiaDTO>>>() {
                },
                page, 4);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PagedModel<EntityModel<PhieuGiamGiaDTO>> phieuGiamGiaPage = responseEntity.getBody();
            int pageSize = (int) phieuGiamGiaPage.getMetadata().getSize();
            int number = pageSize * page;
            model.addAttribute(("number"),number);
            model.addAttribute("listPGG", phieuGiamGiaPage);
            model.addAttribute("currentPage", page);
        } else {
            model.addAttribute("error", "Không thể lấy dữ liệu phân trang");
        }
        return "list/quan-ly-phieu-giam-gia";
    }

}


