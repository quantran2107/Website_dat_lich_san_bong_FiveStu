package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.service.SanCaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("san-ca")
public class SanCaRest {

    private SanCaService sanCaService;

    @Autowired
    public SanCaRest(SanCaService sanCaService) {
        this.sanCaService = sanCaService;
    }


    @GetMapping("hien-thi")
    public ResponseEntity<List> getAll2() {
        List<SanCaDTO> listSanCa = sanCaService.getAllJoinFetch();
        return ResponseEntity.ok(listSanCa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SanCaDTO> getOne(@PathVariable("id") Integer id) {
        SanCaDTO sanCaDTODetail = sanCaService.getOne(id);
        return ResponseEntity.ok(sanCaDTODetail);
    }

    @GetMapping("/san-bong-hop-le")
    public ResponseEntity<?> findByTrangThai(
            @RequestParam(value = "idCa", required = false) Integer idCa,
            @RequestParam(value = "thuTrongTuan", defaultValue = "") String thuTrongTuan,
            @RequestParam(value = "trangThai", defaultValue = "") String trangThai) {

        List<SanCaDTO> list = sanCaService.findByTrangThai(idCa, thuTrongTuan, trangThai);
        return ResponseEntity.ok(list);
    }


    @PostMapping("")
    public ResponseEntity<SanCaDTO> save(@RequestBody SanCaDTO sanCaDTO) {
        SanCaDTO sanCaDTOSave = sanCaService.save(sanCaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(sanCaDTOSave);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SanCaDTO> update(@PathVariable("id") Integer id, @RequestBody SanCaDTO sanCaDTO) {
        SanCaDTO sanCaDTOSave = sanCaService.update(id, sanCaDTO);
        return ResponseEntity.status(HttpStatus.OK).body(sanCaDTOSave);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        sanCaService.deletedAt(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/sort")
    public Map<String, Object> getSanCa(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        int[] totalPageElement = new int[2];
        List<SanCaDTO> sanCaDTOList = sanCaService.listAllSortPage(pageNum, sortDirection, totalPageElement);

        Map<String, Object> response = new HashMap<>();
        response.put("totalPages", totalPageElement[0]);
        response.put("totalElements", totalPageElement[1]);
        response.put("sanCaList", sanCaDTOList);

        return response;
    }

//    @GetMapping("/search")
//    public Map<String, Object> searchById(
//            @RequestParam(value = "id", defaultValue = "1") Integer id,
//            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
//            @RequestParam(value = "trangThai", defaultValue = "inactive") String trangThai,
//            @RequestParam(value = "sortDirection", defaultValue = "asc") String sortDirection
//    ) {
//        int[] totalPageElement = new int[2];
//        List<SanCaDTO> sanCaDTOList = sanCaService.searchId(pageNum,trangThai, sortDirection, totalPageElement, id);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("totalPages", totalPageElement[0]);
//        response.put("totalElements", totalPageElement[1]);
//        response.put("sanCaList", sanCaDTOList);
//
//        return response;
//    }


//
//    @GetMapping("/sanCaList")
//    public String getSanCaList(
//            @RequestParam(defaultValue = "1") Integer pageNum,
//            @RequestParam(defaultValue = "asc") String sortDirection,
//            Model model) {
//        List<SanCaDTO> sanCaDTOList = sanCaService.listAll2(pageNum, sortDirection);
//
//        model.addAttribute("sanCaList", sanCaDTOList);
//        model.addAttribute("currentPage", pageNum);
//        model.addAttribute("sortDirection", sortDirection);
//
//        return "sanCaList"; // Trả về tên của view template (sanCaList.html trong trường hợp này)
//    }
}
