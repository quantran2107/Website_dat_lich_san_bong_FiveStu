package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.rest.PhieuGiamGiaRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
//@RequestMapping("/web/")
public class PhieuGiamGiaController {

    @Autowired
    private RestTemplate restTemplate;

//    @GetMapping("/web-phieu-giam-gia")
//    public String hienThi(Model model) {
//        model.addAttribute("listPGG", Arrays.asList(restTemplate.getForObject(
//                "http://localhost:8080/api/hien-thi",
//                PhieuGiamGiaDTO[].class
//        )));
//        return "/list/quan-ly-phieu-giam-gia";
//    }


    @GetMapping("/list")
    public String listPhieuGiamGia(Model model, @RequestParam(defaultValue = "0") int page) {
        // URL của API
        String apiUrl = "http://localhost:8080/api/phan-trang";

        // Thực hiện request API để lấy Page dữ liệu PhieuGiamGiaDTO
        ResponseEntity<Page<PhieuGiamGiaDTO>> responseEntity = restTemplate.exchange(
                apiUrl + "?page={page}&size={size}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Page<PhieuGiamGiaDTO>>() {},
                page, 10); // page và size là các tham số truyền vào API

        // Kiểm tra nếu request thành công
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            Page<PhieuGiamGiaDTO> phieuGiamGiaPage = responseEntity.getBody();

            // Lấy danh sách PhieuGiamGiaDTO từ phần content của Page
            List<PhieuGiamGiaDTO> phieuGiamGiaList = phieuGiamGiaPage.getContent();

            // Đưa danh sách này vào model để truyền cho View
            model.addAttribute("phieuGiamGiaList", phieuGiamGiaList);
        } else {
            // Xử lý trường hợp lỗi nếu cần
            // Ví dụ: model.addAttribute("error", "Không thể lấy dữ liệu phân trang");
        }

        return "/index"; // Trả về tên của template Thymeleaf
    }
}
