package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.rest.KhachHangRest;
import com.example.DATN_WebFiveTus.rest.PhieuGiamGiaRest;
import com.example.DATN_WebFiveTus.service.KhachHangService;
import com.example.DATN_WebFiveTus.service.PhieuGiamGiaService;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
public class PhieuGiamGiaController {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PhieuGiamGiaRest phieuGiamGiaRest;

    @Autowired
    private KhachHangRest khachHangRest;

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;


    @GetMapping("phieu-giam-gia")
    public String listPhieuGiamGia(@RequestParam(defaultValue = "0") int page, Model model) {
        String apiUrl = "http://localhost:8080/api-phieu-giam-gia/phan-trang";

        ResponseEntity<PagedModel<EntityModel<PhieuGiamGiaDTO>>> responseEntity = restTemplate.exchange(
                apiUrl + "?page={page}&size={size}",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PagedModel<EntityModel<PhieuGiamGiaDTO>>>() {
                },
                page, 10);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PagedModel<EntityModel<PhieuGiamGiaDTO>> phieuGiamGiaPage = responseEntity.getBody();
            int pageSize = (int) phieuGiamGiaPage.getMetadata().getSize();
            int number = pageSize * page;
            model.addAttribute(("number"), number);
            model.addAttribute("listPGG", phieuGiamGiaPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("pgg", new PhieuGiamGiaDTO());
            model.addAttribute("pggDetail", new PhieuGiamGiaDTO());
            // dieu kien su dung
            List<PhieuGiamGiaDTO> phieuGiamGiaDTOList = phieuGiamGiaRest.getAll().getBody();
            Set<String> uniqueDieuKienSuDung = new HashSet<>(phieuGiamGiaDTOList.stream()
                    .map(PhieuGiamGiaDTO::getDieuKienSuDung)
                    .collect(Collectors.toList()));
            model.addAttribute("listDKSD", uniqueDieuKienSuDung);
            //listFull
            model.addAttribute("listFull", phieuGiamGiaDTOList);
            //listKH
            List<KhachHangDTO> khachHangDTOSList = khachHangRest.GetAll2().getBody();
            model.addAttribute("listKH", khachHangDTOSList);
        } else {
            model.addAttribute("error", "Không thể lấy dữ liệu phân trang");
        }

        return "list/quan-ly-phieu-giam-gia";
    }

    @GetMapping("remove-pgg/{id}")
    public String removePGG(@PathVariable("id") Integer id) {
        String apiUrl = "http://localhost:8080/api-phieu-giam-gia/" + id;

        restTemplate.exchange(
                apiUrl,
                HttpMethod.DELETE,
                null,
                Void.class);
        return "redirect:/phieu-giam-gia";
    }

    @PostMapping("save-pgg")
    public String savePhieuGiamGia(@ModelAttribute("pgg") PhieuGiamGiaDTO pgg, Model model) {
        String apiUrl = "http://localhost:8080/api-phieu-giam-gia/save";
        pgg.setTrangThai(true); // Set trangThai = 1
        pgg.setDeletedAt(false); // Set deletedAt = false
        pgg.setMaPhieuGiamGia(generateMaPhieuGiamGia());
        try {
            // Call REST API to save PhieuGiamGiaDTO
            ResponseEntity<PhieuGiamGiaDTO> responseEntity = restTemplate.postForEntity(apiUrl, pgg, PhieuGiamGiaDTO.class);
            PhieuGiamGiaDTO savedPgg = responseEntity.getBody();
        } catch (RestClientException e) {
            // Handle exception if REST API call fails
            e.printStackTrace(); // Example: Log or handle differently
            return "error"; // Redirect to an error page or handle gracefully
        }
        return "redirect:/phieu-giam-gia";
    }

    private String generateMaPhieuGiamGia() {
        // Sử dụng UUID để tạo mã phiếu giảm giá duy nhất
        return "PGG-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    @GetMapping("/detail-phieu-giam-gia/{id}")
    public String viewUpdate(@RequestParam(defaultValue = "0") int page,
                             @PathVariable("id") Integer id, Model model) {
        String apiUrl = "http://localhost:8080/api-phieu-giam-gia/" + id;

        ResponseEntity<PhieuGiamGiaDTO> responseEntity = restTemplate.exchange(
                apiUrl,
                HttpMethod.GET,
                null,
                PhieuGiamGiaDTO.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            PhieuGiamGiaDTO pggDetail = responseEntity.getBody();
            if (pggDetail != null) {
                model.addAttribute("pggDetail", pggDetail);
            } else {
                model.addAttribute("error", "Không tìm thấy phiếu giảm giá với ID: " + id);
            }
        } else {
            model.addAttribute("error", "Không thể truy xuất phiếu giảm giá với ID: " + id);
        }
        return "list/quan-ly-phieu-giam-gia :: #detailModal" + id; // Trả về view để hiển thị modal
    }

    @PutMapping("/update-pgg/{id}")
    public String editPhieuGiamGia(@PathVariable("id") Integer id,
                                   @ModelAttribute("pggDetail") PhieuGiamGiaDTO pggDetail,
                                   Model model) {

        String apiUrl = "http://localhost:8080/api-phieu-giam-gia/" + id;

        try {
            // Call REST API to update PhieuGiamGiaDTO
            ResponseEntity<PhieuGiamGiaDTO> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.PUT,
                    new HttpEntity<>(pggDetail),
                    PhieuGiamGiaDTO.class);
        } catch (RestClientException e) {
            // Handle exception if REST API call fails
            e.printStackTrace(); // Example: Log or handle differently
            model.addAttribute("error", "Lỗi khi gửi yêu cầu cập nhật đến server");
        }

        return "redirect:/phieu-giam-gia";
    }

    public String toggleStatus(@PathVariable("id") Integer id, Model model) {

        String apiUrl = "http://localhost:8080/api-phieu-giam-gia/toggle-status/" + id;

        try {
            // Gửi yêu cầu PUT để cập nhật trạng thái
            ResponseEntity<Void> responseEntity = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.PUT,
                    null,
                    Void.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                model.addAttribute("success", "Đã cập nhật trạng thái thành công");
            } else {
                model.addAttribute("error", "Lỗi khi cập nhật trạng thái");
            }
        } catch (RestClientException e) {
            // Xử lý ngoại lệ nếu gửi yêu cầu API thất bại
            e.printStackTrace(); // Ví dụ: Log hoặc xử lý khác
            model.addAttribute("error", "Lỗi khi gửi yêu cầu cập nhật đến server");
        }

        return "redirect:/phieu-giam-gia";
    }
}


