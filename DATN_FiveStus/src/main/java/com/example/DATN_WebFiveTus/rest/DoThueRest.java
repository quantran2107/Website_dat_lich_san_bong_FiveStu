package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.DoThueDTO;
import com.example.DATN_WebFiveTus.entity.DoThue;
import com.example.DATN_WebFiveTus.repository.DoThueRepository;
import com.example.DATN_WebFiveTus.service.DoThueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("do_thue")
public class DoThueRest {
    private DoThueRepository doThueRepository;
    private DoThueService doThueService;

    public DoThueRest(DoThueService doThueService) {
        this.doThueService = doThueService;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List<DoThueDTO>> getAll2() {
        List<DoThueDTO> thueDTOList = doThueService.getAll();
        return ResponseEntity.ok(thueDTOList);
    }

    @GetMapping("/phan-trang")
    public ResponseEntity<Page<DoThueDTO>> layPhanTrang(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DoThueDTO> pageResult = doThueService.phanTrang(pageable);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/search-and-filter")
    public ResponseEntity<Page<DoThueDTO>> searchAndFilter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) Float donGiaMin,
            @RequestParam(required = false) Float donGiaMax,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        // Cập nhật pageable để sắp xếp theo ID giảm dần
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));

        Page<DoThueDTO> doThueDTOPage = doThueService.searchDoThue(
                keyword, trangThai, donGiaMin, donGiaMax, sortedPageable);

        return ResponseEntity.ok(doThueDTOPage);
    }

    @PostMapping("save")
    public ResponseEntity<DoThueDTO> save(@ModelAttribute DoThueDTO doThueDTO,
                                          @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        // Xử lý hình ảnh
        if (imageFile != null && !imageFile.isEmpty()) {
            String fileName = imageFile.getOriginalFilename();
            doThueDTO.setImageData(fileName);
        }

        // Lưu đối tượng DoThueDTO
        DoThueDTO savedDoThue = doThueService.save(doThueDTO);

        return ResponseEntity.ok(savedDoThue);
    }

//    @PostMapping("save")
//    public ResponseEntity<DoThueDTO> save(@ModelAttribute DoThueDTO doThueDTO,
//                                          @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
//        // Xử lý hình ảnh
//        if (imageFile != null && !imageFile.isEmpty()) {
//            byte[] imageData = imageFile.getBytes();
//            doThueDTO.setImageData(imageData);
//        }
//
//        // Lưu đối tượng DoThueDTO
//        DoThueDTO savedDoThue = doThueService.save(doThueDTO);
//
//        return ResponseEntity.ok(savedDoThue);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<DoThueDTO> getOne(@PathVariable("id") Integer id) {
        DoThueDTO doThueDTO = doThueService.getOne(id);
        return ResponseEntity.ok(doThueDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoThueDTO> update(@PathVariable("id") Integer id,@RequestBody DoThueDTO doThueDTO){
        return ResponseEntity.ok(doThueService.update(id, doThueDTO));
//
//    public ResponseEntity<DoThueDTO> updateDoThue(
//            @PathVariable("id") Integer id,
//            @RequestParam("tenDoThue") String tenDoThue,
//            @RequestParam("soLuong") int soLuong,
//            @RequestParam("donGia") float donGia,
//            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
//
//        // Lấy đối tượng hiện tại
//        DoThueDTO existingDoThueDTO = doThueService.getOne(id);
//
//        // Cập nhật các giá trị mới
//        existingDoThueDTO.setTenDoThue(tenDoThue);
//        existingDoThueDTO.setSoLuong(soLuong);
//        existingDoThueDTO.setDonGia(donGia);
//
//        // Cập nhật ảnh nếu có
//        if (imageFile != null && !imageFile.isEmpty()) {
//            existingDoThueDTO.setImageData(imageFile.getBytes());
//        }
//
//        // Cập nhật đối tượng
//        DoThueDTO updatedDoThueDTO = doThueService.update(id, existingDoThueDTO);
//
//        return ResponseEntity.ok(updatedDoThueDTO);

    }

//    @PutMapping("/{id}")
//    public ResponseEntity<DoThueDTO> update(
//            @PathVariable("id") Integer id,
//            @RequestParam("doThueDTO") String doThueDTOJson,
//            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
//
//        // Chuyển đổi JSON thành đối tượng DoThueDTO
//        ObjectMapper objectMapper = new ObjectMapper();
//        DoThueDTO doThueDTO = objectMapper.readValue(doThueDTOJson, DoThueDTO.class);
//
//        // Lấy đối tượng DoThueDTO hiện tại từ dịch vụ để giữ lại ảnh cũ nếu không có file mới
//        DoThueDTO existingDoThueDTO = doThueService.getOne(id);
//
//        // Nếu không có file mới, giữ lại hình ảnh cũ
//        if (imageFile == null || imageFile.isEmpty()) {
//            doThueDTO.setImageData(existingDoThueDTO.getImageData());
//        } else {
//            // Nếu có file mới, chuyển đổi MultipartFile thành byte[]
//            byte[] imageBytes = imageFile.getBytes();
//            doThueDTO.setImageData(imageBytes);
//        }
//
//        // Đặt ID cho đối tượng DoThueDTO để cập nhật
//        doThueDTO.setId(id);
//
//        // Xử lý đối tượng DoThueDTO và lưu vào dịch vụ
//        DoThueDTO updatedDoThueDTO = doThueService.save(doThueDTO);
//
//        return ResponseEntity.ok(updatedDoThueDTO);
//    }

    @PutMapping("/delete-soft/{id}")
    public ResponseEntity<Void> deleteSoft(@PathVariable Integer id, @RequestBody(required = false) Map<String, Boolean> requestBody) {
        if (requestBody == null || !requestBody.containsKey("deletedAt")) {
            return ResponseEntity.badRequest().build(); // Trả về lỗi 400 nếu body thiếu
        }

        Boolean deletedAt = requestBody.get("deletedAt");
        if (deletedAt != null && deletedAt) {
            doThueService.delete(id);
        }
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/check-id-do-thue")
    public ResponseEntity<Boolean> checkIdDichVuDoThue(@RequestParam("id") Integer id, @RequestParam("idHoaDonChiTiet") Integer idHoaDonChiTiet) {
        Boolean exists = doThueService.checkIdDichVuDoThue(id, idHoaDonChiTiet);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("getIdDoThue")
    public ResponseEntity<Integer> getIdDoThues(@RequestParam Integer idDoThue,
                                                @RequestParam Integer idHoaDonChiTiet){
        return ResponseEntity.ok(doThueService.getIdDoThue(idDoThue,idHoaDonChiTiet));
    }

    @GetMapping("searchTenDoThue")
    public ResponseEntity<List<DoThueDTO>> searchTenDoThues(@RequestParam("tenDoThue") String tenDoThue){
        return ResponseEntity.ok(doThueService.searchTenDoThue(tenDoThue));
    }

}
