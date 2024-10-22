package com.example.DATN_WebFiveTus.controller;

import com.example.DATN_WebFiveTus.dto.CaDTO;
import com.example.DATN_WebFiveTus.dto.LoaiSanDTO;
import com.example.DATN_WebFiveTus.dto.NgayTrongTuanDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.rest.SanCaRest;
import com.example.DATN_WebFiveTus.service.CaService;
import com.example.DATN_WebFiveTus.service.NgayTrongTuanService;
import com.example.DATN_WebFiveTus.service.SanBongService;
import com.example.DATN_WebFiveTus.service.SanCaService;
import jakarta.servlet.http.HttpServletResponse;
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

import java.io.IOException;
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

    private CaService caService;

    private NgayTrongTuanService ngayTrongTuanService;

    private SanBongService sanBongService;


    @Autowired
    public SanCaController(RestTemplate restTemplate, SanCaRest sanCaRest, SanCaService sanCaService,
                           CaService caService, NgayTrongTuanService ngayTrongTuanService, SanBongService sanBongService) {
        this.restTemplate = restTemplate;
        this.sanCaRest = sanCaRest;
        this.sanCaService = sanCaService;
        this.caService = caService;
        this.ngayTrongTuanService = ngayTrongTuanService;
        this.sanBongService = sanBongService;
    }

    @GetMapping("listSanCa")
    public String listSanCa(
            @RequestParam(name = "haha", required = false) Integer id,
            @RequestParam(name = "keyWords", required = false) String keyWords,
            @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(name = "sortDirection", defaultValue = "asc") String sortDirection,
            Model model) {

        List<SanCaDTO> listRest = sanCaRest.getAll2().getBody();
        Set<String> listTT = new HashSet<>(listRest.stream()
                .map(SanCaDTO::getTrangThai)
                .collect(Collectors.toList()));

        model.addAttribute("listTT", listTT);


        model.addAttribute("listTT", listTT);

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
            sanCaDTOList = sanCaService.listAllSortPage(pageNum, sortDirection, totalPageElement);
        }

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
        return sanCaDTO;
    }

    @PostMapping("/sanCa/update")
    public String update(@ModelAttribute("sanCa") SanCaDTO sanCaDTO) {
        System.out.println("hahaupdate");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.put("http://localhost:8080/san-ca/{id}", sanCaDTO, sanCaDTO.getId());
        System.out.println("Haha:"+sanCaDTO.getId());
        return "redirect:/listSanCa";
    }

//    @GetMapping("/export/excels")
//    public void exportExcel(HttpServletResponse httpServletResponse) throws IOException {
//        List<SanCaDTO> listExcel = sanCaService.getAllJoinFetch();
//        System.out.println("HAHA excel: "+listExcel);
//        ExcelExporter excelExporter = new ExcelExporter();
//        excelExporter.Export(listExcel, httpServletResponse);
//    }

//    @PostMapping("/sanCa/add")
//    public String addSanCa(@ModelAttribute("sanCa") SanCaDTO sanCaDTO,
//                           @ModelAttribute("ca") CaDTO caDTO,
//                           @ModelAttribute("sanBong") SanBongDTO sanBongDTO,
//                           @ModelAttribute("ngayTrongTuan") NgayTrongTuanDTO ngayTrongTuanDTO) {
//
//        if (caDTO.getId() != null) {
//            caService.save(caDTO);
//            System.out.println("Add1");
//        }
//
//        if (ngayTrongTuanDTO.getId() != null) {
//            ngayTrongTuanService.save(ngayTrongTuanDTO);
//            System.out.println("Add2");
//        }
//        System.out.println("Không add được add");
////        sanBongService.save(sanBongDTO);
////        caService.save(caDTO);
////        ngayTrongTuanService.save(ngayTrongTuanDTO);
//        // Lấy thông tin từ các DTO và đưa vào sanCaDTO
////        sanCaDTO.setIdSanBong(sanBongDTO.getId()); // ID của sân bóng
//        sanCaDTO.setIdNgayTrongTuan(ngayTrongTuanDTO.getId()); // ID của ngày trong tuần
//        sanCaDTO.setIdCa(caDTO.getId()); // ID của ca
//
//        // Lưu thông tin vào cơ sở dữ liệu
//        sanCaService.save(sanCaDTO);
//
//        return "redirect:/quan-ly-san-bong";
//    }


    // Lưu Ca
//    @PostMapping("/cas/add")
//    public String addCa(@ModelAttribute("ca") CaDTO caDTO, Model model) {
//        System.out.println("HahaCa");
//        if (caDTO.getId() != null) {
//            caService.save(caDTO);
//        }
//        model.addAttribute("ca", caDTO);
//        return "redirect:/form-page";
//    }
//
//    // Lưu Ngay Trong Tuan
//    @PostMapping("/ngayTrongTuans/add")
//    public String addNgayTrongTuan(@ModelAttribute("ngayTrongTuan") NgayTrongTuanDTO ngayTrongTuanDTO, Model model) {
//        // Lưu đối tượng ngayTrongTuan trước
//        NgayTrongTuanDTO savedNgayTrongTuan = ngayTrongTuanService.save(ngayTrongTuanDTO);
//
//        // In ra ID để kiểm tra
//        Integer ngayTrongTuanId = savedNgayTrongTuan.getId();
//        System.out.println("ID sau khi lưu: " + ngayTrongTuanId);
//
//        // Tạo đối tượng SanCaDTO mới
//        SanCaDTO sanCaDTO = new SanCaDTO();
//        // Gán ID của ngayTrongTuan vào sanCaDTO
//        sanCaDTO.setId(8);
//        sanCaDTO.setIdNgayTrongTuan(ngayTrongTuanId);
//
//        // Lưu đối tượng sanCa
//        SanCaDTO savedSanCa = sanCaService.save(sanCaDTO);
//        System.out.println("ID sau khi lưu SanCa: " + savedSanCa.getId());
//
//        // Thêm đối tượng đã lưu vào model để sử dụng trong view
//        model.addAttribute("ngayTrongTuan", ngayTrongTuanDTO);
//
//        return "redirect:/listSanBong";
//    }
//
//
//
//    // Lưu San Bong
//    @PostMapping("/sanBongs/add")
//    public String addSanBong(@ModelAttribute("sanBong") SanBongDTO sanBongDTO, Model model) {
//        if (sanBongDTO.getId() != null) {
//            System.out.println("HahaCa3");
//            sanBongService.save(sanBongDTO);
//        }
//        model.addAttribute("sanBong", sanBongDTO);
//        return "redirect:/form-page";
//    }

    // Lưu San Ca
//    @PostMapping("/sanCa/add")
//    public String addSanCa(@ModelAttribute("sanCa") SanCaDTO sanCaDTO,
//                           @ModelAttribute("ca") CaDTO caDTO,
//                           @ModelAttribute("sanBong") SanBongDTO sanBongDTO,
//                           @ModelAttribute("ngayTrongTuan") NgayTrongTuanDTO ngayTrongTuanDTO,
//                           Model model) {
//
//        // Kiểm tra các ID
//        if (caDTO.getId() != null && ngayTrongTuanDTO.getId() != null && sanBongDTO.getId() != null) {
//            // Gán các ID từ DTOs vào sanCaDTO
//            sanCaDTO.setIdNgayTrongTuan(ngayTrongTuanDTO.getId());
//            sanCaDTO.setIdCa(caDTO.getId());
//            sanCaDTO.setIdSanBong(sanBongDTO.getId());
//
//            // Lưu thông tin vào cơ sở dữ liệu
//            sanCaService.save(sanCaDTO);
//        } else {
//            System.out.println("Không add được sanCa vì thiếu ID");
//        }
//
//        return "redirect:/quan-ly-san-bong";
//    }

    @GetMapping("list-san-ca")
    public String listSanCa(){
        return "/list/chon-san-ca";
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
