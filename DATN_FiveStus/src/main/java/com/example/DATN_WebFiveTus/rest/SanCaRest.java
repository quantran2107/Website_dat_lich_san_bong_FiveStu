package com.example.DATN_WebFiveTus.rest;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.service.SanCaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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


    @GetMapping("/hien-thi-san-trong")
    public ResponseEntity<?> hienThiSanTrong(
            @RequestParam(value = "startDate", required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(value = "endDate", required = true)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(value = "idLoaiSan", required = true) Integer idLoaiSan) {

        // Chuyển đổi startDate và endDate thành danh sách các thứ trong tuần
        List<String> thuTrongTuanList = convertDateToThuTrongTuan(startDate, endDate);

        // Gọi service với danh sách thuTrongTuan và idLoaiSan
        List<SanCaDTO> list = sanCaService.hienThiSanTrong(idLoaiSan, thuTrongTuanList, startDate, endDate);

        // Trả về danh sách dưới dạng ResponseEntity
        return ResponseEntity.ok(list);
    }

    // Hàm chuyển đổi Date thành danh sách các thứ trong tuần
    private List<String> convertDateToThuTrongTuan(Date startDate, Date endDate) {
        List<String> thuTrongTuanList = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (!calendar.getTime().after(endDate)) {
            // Lấy thứ trong tuần từ Calendar (1 = Chủ nhật, 2 = Thứ hai, ...)
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            // Chuyển đổi thành chuỗi thứ trong tuần (Ví dụ: "Thứ Hai", "Thứ Ba", ...)
            thuTrongTuanList.add(convertDayOfWeekToThuTrongTuan(dayOfWeek));

            // Tăng ngày lên 1
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return thuTrongTuanList;
    }

    // Chuyển đổi dayOfWeek (Calendar) sang định dạng thứ trong tuần (Ví dụ: "Thứ Hai")
    private String convertDayOfWeekToThuTrongTuan(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.MONDAY: return "Thứ Hai";
            case Calendar.TUESDAY: return "Thứ Ba";
            case Calendar.WEDNESDAY: return "Thứ Tư";
            case Calendar.THURSDAY: return "Thứ Năm";
            case Calendar.FRIDAY: return "Thứ Sáu";
            case Calendar.SATURDAY: return "Thứ Bảy";
            case Calendar.SUNDAY: return "Chủ Nhật";
            default: throw new IllegalArgumentException("Invalid day of week: " + dayOfWeek);
        }
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



    @GetMapping("/danh-sach-san-ca/{idSanBong}/{idNgayTrongTuan}")
    public ResponseEntity<List> danhSachSanCa(@PathVariable("idSanBong") Integer idSanBong,
                                              @PathVariable("idNgayTrongTuan") Integer idNgayTrongTuan){
        List<SanCaDTO> listSanCa = sanCaService.findSanCaBySan(idSanBong,idNgayTrongTuan);
        return ResponseEntity.ok(listSanCa);
    }

    @GetMapping("/danh-sach-san-ca/{idSanBong}/{idNgayTrongTuan}/{idCa}")
    public ResponseEntity<SanCaDTO> getOneSanCaByAll(@PathVariable("idSanBong") Integer idSanBong,
                                              @PathVariable("idNgayTrongTuan") Integer idNgayTrongTuan,
                                                 @PathVariable("idCa") Integer idCa){
        SanCaDTO sanCaDTO = sanCaService.getOneSanCaByAll(idSanBong,idNgayTrongTuan,idCa);
        return ResponseEntity.ok(sanCaDTO);
    }

    //Ly them
    @GetMapping("/danh-sach-nhieu-ngay/{idSanBong}")
    public ResponseEntity<List<SanCaDTO>> danhSachSanCa(
            @PathVariable("idSanBong") Integer idSanBong,
            @RequestParam("listIdNgayTrongTuan") List<Integer> listIdNgayTrongTuan) {

        List<SanCaDTO> listSanCa = sanCaService.findSanCaByNhieuNgay(idSanBong, listIdNgayTrongTuan);
        return ResponseEntity.ok(listSanCa);
    }


}
