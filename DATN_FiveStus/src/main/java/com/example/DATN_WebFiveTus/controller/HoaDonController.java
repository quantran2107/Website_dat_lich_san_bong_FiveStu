package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
public class HoaDonController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/web-hoa-don")
    public String listHoaDon(Model model, @RequestParam(defaultValue = "0") int page) {
        String apiUrl = "http://localhost:8080/hoa-don/phan-trang";

        ResponseEntity<PagedModel<EntityModel<HoaDonDTO>>> responseEntity = restTemplate.exchange(
                apiUrl + "?page={page}&size={size}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<EntityModel<HoaDonDTO>>>() {
                },
                page, 10);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PagedModel<EntityModel<HoaDonDTO>> hoaDonPage = responseEntity.getBody();
            int pageSize = (int) hoaDonPage.getMetadata().getSize();
            int number = pageSize * page;
            model.addAttribute(("number"),number);
            model.addAttribute("listHD", hoaDonPage);
            model.addAttribute("currentPage", page);
        } else {
            model.addAttribute("error", "Không thể lấy dữ liệu phân trang");
        }
        return "/list/quan-ly-hoa-don";
    }

    @GetMapping("/web-hoa-don/{id}")
    public String getHoaDonDetail(@PathVariable Integer id, Model model) {
        String apiUrl = "http://localhost:8080/hoa-don/{id}";

        ResponseEntity<HoaDonDTO> responseEntity = restTemplate.getForEntity(
                apiUrl,
                HoaDonDTO.class,
                id);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            HoaDonDTO hoaDon = responseEntity.getBody();
            model.addAttribute("hoaDon", hoaDon);
        } else {
            model.addAttribute("error", "Không thể lấy thông tin chi tiết hóa đơn");
        }

        return "modal/hien-thi-hoa-don"; // Thay thế bằng tên file HTML của bạn cho modal
    }
}
