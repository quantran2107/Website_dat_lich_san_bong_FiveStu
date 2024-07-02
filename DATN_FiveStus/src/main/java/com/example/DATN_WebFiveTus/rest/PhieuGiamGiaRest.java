package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.PhieuGiamGiaDTO;
import com.example.DATN_WebFiveTus.dto.SanBongDTO;
import com.example.DATN_WebFiveTus.service.PhieuGiamGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.data.web.SortDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@CrossOrigin()
@RestController
@RequestMapping(value = "/api-phieu-giam-gia/", produces = MediaType.APPLICATION_JSON_VALUE)
public class PhieuGiamGiaRest {


    private final PhieuGiamGiaService phieuGiamGiaService;
    private final PagedResourcesAssembler<PhieuGiamGiaDTO> pagedResourcesAssembler;

    @Autowired
    public PhieuGiamGiaRest(PhieuGiamGiaService phieuGiamGiaService,
                            PagedResourcesAssembler<PhieuGiamGiaDTO> pagedResourcesAssembler) {
        this.phieuGiamGiaService = phieuGiamGiaService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("/hien-thi")
    public ResponseEntity<List<PhieuGiamGiaDTO>> getAll() {
        List<PhieuGiamGiaDTO> phieuGiamGiaList = phieuGiamGiaService.getAll();
        return ResponseEntity.ok(phieuGiamGiaList);
    }

    @GetMapping("/phan-trang")
    public ResponseEntity<PagedModel<EntityModel<PhieuGiamGiaDTO>>> getAllPhieuGiamGia(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<PhieuGiamGiaDTO> phieuGiamGiaPage = phieuGiamGiaService.phanTrang(pageable);
        PagedModel<EntityModel<PhieuGiamGiaDTO>> pagedModel = pagedResourcesAssembler.toModel(phieuGiamGiaPage);
        return ResponseEntity.ok(pagedModel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        phieuGiamGiaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> getOne(@PathVariable("id") Integer id) {
        PhieuGiamGiaDTO phieuGiamGiaDTO = phieuGiamGiaService.getOne(id);
        return ResponseEntity.ok(phieuGiamGiaDTO);
    }

    @PostMapping("save")
    public ResponseEntity<PhieuGiamGiaDTO> save(@RequestBody PhieuGiamGiaDTO phieuGiamGiaDTO) {
        PhieuGiamGiaDTO phieuGiamGiaDTOSave = phieuGiamGiaService.save(phieuGiamGiaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(phieuGiamGiaDTOSave);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PhieuGiamGiaDTO> update(@PathVariable("id") Integer id, @RequestBody PhieuGiamGiaDTO phieuGiamGiaDTO) {
        PhieuGiamGiaDTO phieuGiamGiaDTODetail = phieuGiamGiaService.save(phieuGiamGiaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(phieuGiamGiaDTODetail);
    }

    @PutMapping("/toggle-status/{id}")
    public ResponseEntity<?> toggleStatus(@PathVariable Integer id, @RequestBody Map<String, Boolean> request) {
        boolean newStatus = request.get("trangThai");
        try {
            phieuGiamGiaService.updateStatus(id, newStatus);
            return ResponseEntity.ok().body("Status updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update status");
        }
    }

    @DeleteMapping("/delete-soft") // Endpoint đúng cho xóa mềm sử dụng phương thức DELETE
    public ResponseEntity<Void> deleteSoft(@RequestBody List<Integer> ids) {
        try {
            for (Integer id : ids) {
                PhieuGiamGiaDTO phieuGiamGia = phieuGiamGiaService.getOne(id);
                if (phieuGiamGia != null) {
                    phieuGiamGia.setDeletedAt(true); // Đặt cờ deletedAt thành true
                    phieuGiamGiaService.save(phieuGiamGia); // Lưu entity đã cập nhật
                } else {
                    // Xử lý trường hợp không tìm thấy entity với ID tương ứng
                }
            }
            return ResponseEntity.noContent().build(); // Trả về thành công
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<PhieuGiamGiaDTO>> search(@RequestParam String query) {
        List<PhieuGiamGiaDTO> results = phieuGiamGiaService.search(query);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PhieuGiamGiaDTO>> filter(@RequestParam String status) {
        return ResponseEntity.ok(phieuGiamGiaService.filter(status));
    }


}
