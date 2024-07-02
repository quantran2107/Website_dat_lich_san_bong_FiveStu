package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.HoaDonDTO;
import com.example.DATN_WebFiveTus.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/hoa-don/" ,produces = MediaType.APPLICATION_JSON_VALUE)
public class HoaDonRest {

    private HoaDonService hoaDonService;
    private PagedResourcesAssembler<HoaDonDTO> pagedResourcesAssembler;

    @Autowired
    public HoaDonRest(HoaDonService hoaDonService, PagedResourcesAssembler<HoaDonDTO> pagedResourcesAssembler) {
        this.hoaDonService = hoaDonService;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll(){
        List<HoaDonDTO> listHoaDon = hoaDonService.getAllJoinFetch();
        return ResponseEntity.ok(listHoaDon);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HoaDonDTO> getOne(@PathVariable("id") Integer id){
        HoaDonDTO hoaDonDTODetail = hoaDonService.getOne(id);
        return ResponseEntity.ok(hoaDonDTODetail);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id){
        hoaDonService.deletedAt(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/phan-trang")
    public ResponseEntity<PagedModel<EntityModel<HoaDonDTO>>> getAllHoaDon(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<HoaDonDTO> hoaDonPage = hoaDonService.phanTrang(pageable);
        PagedModel<EntityModel<HoaDonDTO>> pagedModel = pagedResourcesAssembler.toModel(hoaDonPage);
        return ResponseEntity.ok(pagedModel);
    }
}
