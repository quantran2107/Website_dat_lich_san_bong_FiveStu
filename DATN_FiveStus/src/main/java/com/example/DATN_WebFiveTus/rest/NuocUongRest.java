package com.example.DATN_WebFiveTus.rest;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.DATN_WebFiveTus.dto.DoThueDTO;
import com.example.DATN_WebFiveTus.dto.NuocUongDTO;
import com.example.DATN_WebFiveTus.service.Imp.DoThueServiceImp;
import com.example.DATN_WebFiveTus.service.NuocUongService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

@RestController
@RequestMapping("nuoc_uong")
public class NuocUongRest {
    private final Cloudinary cloudinary;
    private final DoThueServiceImp doThueServiceImp;
    private NuocUongService nuocUongService;

    public NuocUongRest(NuocUongService nuocUongService, Cloudinary cloudinary, DoThueServiceImp doThueServiceImp) {
        this.nuocUongService = nuocUongService;
        this.cloudinary = cloudinary;
        this.doThueServiceImp = doThueServiceImp;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> GetAll2() {
        List<NuocUongDTO> nuocUongDTOList = nuocUongService.getAllJoinFetch2();
        return ResponseEntity.ok(nuocUongDTOList);
    }


    @GetMapping("/phan-trang")
    public ResponseEntity<Page<NuocUongDTO>> layPhanTrang(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<NuocUongDTO> pageResult = nuocUongService.phanTrang(pageable);
        return ResponseEntity.ok(pageResult);
    }

    @GetMapping("/search-and-filter")
    public ResponseEntity<Page<NuocUongDTO>> searchAndFilter(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String trangThai,
            @RequestParam(required = false) Float donGiaMin,
            @RequestParam(required = false) Float donGiaMax,
            @PageableDefault(page = 0, size = 10) Pageable pageable) {

        // Cập nhật pageable để sắp xếp theo ID giảm dần
        Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Order.desc("createdAt")));

        Page<NuocUongDTO> nuocUongDTOPage = nuocUongService.searchNuocUong(
                keyword, trangThai, donGiaMin, donGiaMax, sortedPageable);

        return ResponseEntity.ok(nuocUongDTOPage);
    }

    @PostMapping("save")
    public ResponseEntity<NuocUongDTO> save(@ModelAttribute NuocUongDTO nuocUongDTO,
                                            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {

        if (imageFile != null && !imageFile.isEmpty()) {
            // Upload ảnh lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            String imageUrl = uploadResult.get("secure_url").toString();
            nuocUongDTO.setImageData(imageUrl);
        }

        // Lưu đối tượng DoThueDTO
        NuocUongDTO saveNuocUong = nuocUongService.save(nuocUongDTO);

        return ResponseEntity.ok(saveNuocUong);
    }

//    @PostMapping("save")
//    public ResponseEntity<NuocUongDTO> save(@ModelAttribute NuocUongDTO nuocUongDTO,
//                                            @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
//
//        // Xử lý hình ảnh
//        if (imageFile != null && !imageFile.isEmpty()) {
//            byte[] imageData = imageFile.getBytes();
//            nuocUongDTO.setImageData(imageData);
//        }
//
//        // Lưu đối tượng NuocUongDTO
//        NuocUongDTO savedNuocUong = nuocUongService.save(nuocUongDTO);
//
//        return ResponseEntity.ok(savedNuocUong);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<NuocUongDTO> getOne(@PathVariable("id") Integer id) {
        NuocUongDTO nuocUongDTO = nuocUongService.getOne(id);
        return ResponseEntity.ok(nuocUongDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NuocUongDTO> update(
            @PathVariable("id") Integer id,
            @ModelAttribute NuocUongDTO nuocUongDTO, // Không ánh xạ imageData từ request
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {

        // Xử lý ảnh nếu có tệp ảnh được tải lên
        if (imageFile != null && !imageFile.isEmpty()) {
            // Upload ảnh lên Cloudinary
            Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(),
                    ObjectUtils.asMap("resource_type", "auto"));
            String imageUrl = uploadResult.get("secure_url").toString();

            // Set URL ảnh vào doThueDTO
            nuocUongDTO.setImageData(imageUrl);  // Cập nhật URL của ảnh sau khi upload
        }

        // Gọi service để xử lý cập nhật
        NuocUongDTO updatedNuocUong = nuocUongService.update(id, nuocUongDTO);
        return ResponseEntity.ok(updatedNuocUong);
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<NuocUongDTO> update(
//            @PathVariable("id") Integer id,
//            @RequestParam("NuocUongDTO") String nuocUongDTOJson,
//            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) throws IOException {
//
//        // Chuyển đổi JSON thành đối tượng NuocUongDTO
//        ObjectMapper objectMapper = new ObjectMapper();
//        NuocUongDTO nuocUongDTO = objectMapper.readValue(nuocUongDTOJson, NuocUongDTO.class);
//
//        // Lấy đối tượng NuocUongDTO hiện tại từ dịch vụ để giữ lại ảnh cũ nếu không có file mới
//        NuocUongDTO existingNuocUongDTO = nuocUongService.getOne(id);
//
//        // Nếu không có file mới, giữ lại hình ảnh cũ
//        if (imageFile == null || imageFile.isEmpty()) {
//            nuocUongDTO.setImageData(existingNuocUongDTO.getImageData());
//        } else {
//            // Nếu có file mới, chuyển đổi MultipartFile thành byte[]
//            byte[] imageBytes = imageFile.getBytes();
//            nuocUongDTO.setImageData(imageBytes);
//        }
//
//        // Đặt ID cho đối tượng NuocUongDTO để cập nhật
//        nuocUongDTO.setId(id);
//
//        // Xử lý đối tượng NuocUongDTO và lưu vào dịch vụ
//        NuocUongDTO updatedNuocUongDTO = nuocUongService.save(nuocUongDTO);
//
//        return ResponseEntity.ok(updatedNuocUongDTO);
//    }

    @PutMapping("/delete-soft/{id}")
    public ResponseEntity<Void> deleteSoft(@PathVariable Integer id, @RequestBody(required = false) Map<String, Boolean> requestBody) {
        if (requestBody == null || !requestBody.containsKey("deletedAt")) {
            return ResponseEntity.badRequest().build(); // Trả về lỗi 400 nếu body thiếu
        }

        Boolean deletedAt = requestBody.get("deletedAt");
        if (deletedAt != null && deletedAt) {
            nuocUongService.delete(id);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/check-id-nuoc-uong")
    public ResponseEntity<Boolean> checkIdDichVuDoThue(@RequestParam("id") Integer id, @RequestParam("idHoaDonChiTiet") Integer idHoaDonChiTiet) {
        Boolean exists = nuocUongService.checkIdDichVuNuocUong(id, idHoaDonChiTiet);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("getIdNuocUong")
    public ResponseEntity<Integer> getIdNuocUongs(@RequestParam Integer idNuocUong,
                                                @RequestParam Integer idHoaDonChiTiet){
        return ResponseEntity.ok(nuocUongService.getIdNuocUong(idNuocUong,idHoaDonChiTiet));
    }

    @GetMapping("searchTenNuocUong")
    public ResponseEntity<List> searchTenNuocUongs(@RequestParam("tenNuocUong") String tenNuocUong){
        return ResponseEntity.ok(nuocUongService.searchTenNuocUong(tenNuocUong));
    }

    @PutMapping("updateSoLuong/{id}")
    public ResponseEntity<?> updateSoLuong(@PathVariable Integer id, @RequestBody NuocUongDTO nuocUongDTO) {
        return ResponseEntity.ok(nuocUongService.updateSoLuong(id, nuocUongDTO));
    }

}
