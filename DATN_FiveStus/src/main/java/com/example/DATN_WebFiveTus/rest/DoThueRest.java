package com.example.DATN_WebFiveTus.rest;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
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
    private final Cloudinary cloudinary;
    private DoThueRepository doThueRepository;
    private DoThueService doThueService;

    public DoThueRest(DoThueService doThueService, Cloudinary cloudinary) {
        this.doThueService = doThueService;
        this.cloudinary = cloudinary;
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
        if (imageFile != null && !imageFile.isEmpty()) {
            // Upload ảnh lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            String imageUrl = uploadResult.get("secure_url").toString();
            doThueDTO.setImageData(imageUrl);
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
    public ResponseEntity<DoThueDTO> update(
            @PathVariable("id") Integer id,
            @ModelAttribute DoThueDTO doThueDTO, // Không ánh xạ imageData từ request
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        // Xử lý ảnh nếu có tệp ảnh được tải lên
        if (imageFile != null && !imageFile.isEmpty()) {
            // Upload ảnh lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            String imageUrl = uploadResult.get("secure_url").toString();

            // Set URL ảnh vào doThueDTO
            doThueDTO.setImageData(imageUrl);  // Cập nhật URL của ảnh sau khi upload
        }

        // Gọi service để xử lý cập nhật
        DoThueDTO updatedDoThue = doThueService.update(id, doThueDTO);
        return ResponseEntity.ok(updatedDoThue);
    }



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

    @PutMapping("updateSoLuong/{id}")
    public ResponseEntity<?> updateSoLuong(@PathVariable Integer id, @RequestBody DoThueDTO doThueDTO){
        return ResponseEntity.ok(doThueService.updateSoLuong(id, doThueDTO));
    }

}
