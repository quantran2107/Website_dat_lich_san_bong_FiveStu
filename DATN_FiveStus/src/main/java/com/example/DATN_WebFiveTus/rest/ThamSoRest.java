package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.ThamSoDTO;
import com.example.DATN_WebFiveTus.entity.ThamSo;
import com.example.DATN_WebFiveTus.service.ThamSoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("tham-so")
public class ThamSoRest {

    private ThamSoService thamSoService;

    @Autowired
    public ThamSoRest(ThamSoService thamSoService) {
        this.thamSoService = thamSoService;
    }

    @GetMapping("hien-thi")
    private ResponseEntity<List> HienThi(){
        return ResponseEntity.ok(thamSoService.getAll());
    }

    @GetMapping("hien-thi2")
    private ResponseEntity<List> HienThi2(){
        return ResponseEntity.ok(thamSoService.getAll2());
    }

    @GetMapping("/{id}")
    private ResponseEntity<ThamSoDTO> getOne(@PathVariable("id") Integer id){
        return ResponseEntity.ok(thamSoService.getOne(id));
    }

    @PostMapping
    private ResponseEntity<ThamSoDTO> save(@RequestBody ThamSoDTO thamSoDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(thamSoService.save(thamSoDTO));
    }

    @PutMapping("/{id}")
    private ResponseEntity<ThamSoDTO> update(@RequestBody ThamSoDTO thamSoDTO,@PathVariable("id") Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(thamSoService.update(thamSoDTO,id));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ThamSoDTO>> search(
            @RequestParam("keyword") String keyword,
            @RequestParam(name = "pageNo", defaultValue = "0") int page,
            @RequestParam(name = "pageSize", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ThamSoDTO> resultPage = thamSoService.searchThamSo(keyword, pageable);

        return ResponseEntity.ok(resultPage);
    }

    @GetMapping("searchs")
    public Page<ThamSoDTO> searchThamSos(
            @RequestParam(required = false, defaultValue = "") String ma,
            @RequestParam(required = false, defaultValue = "") String ten,
            @RequestParam(required = false, defaultValue = "") String typeGiaTri,
            @RequestParam(required = false) Boolean trangThai, // Thay đổi từ `boolean` thành `Boolean`
            Pageable pageable) {
        return thamSoService.searchThamSoss(ma, ten, typeGiaTri, trangThai, pageable);
    }

    @GetMapping("searchMaFake/{maThamSo}")
    public ResponseEntity<ThamSoDTO> getThamSoByMa(@PathVariable String maThamSo) {
        ThamSoDTO thamSoDTO = thamSoService.findByMaThamSo(maThamSo);
        return new ResponseEntity<>(thamSoDTO, HttpStatus.OK);
    }

    @PostMapping("/fake")
    public ResponseEntity<ThamSoDTO> saveFake(@RequestBody ThamSoDTO thamSoDTO) {
        ThamSoDTO savedThamSo = thamSoService.saveFake(thamSoDTO);
        return new ResponseEntity<>(savedThamSo, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        thamSoService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/updateTrangThai/{id}")
    public ResponseEntity<String> updateTrangThai(@PathVariable("id") Integer id,
                                                  @RequestParam("status") boolean status) {
        try {
            thamSoService.updateTrangThai(id, status);
            return ResponseEntity.ok("Trạng thái đã được cập nhật thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi khi cập nhật trạng thái: " + e.getMessage());
        }
    }

}
