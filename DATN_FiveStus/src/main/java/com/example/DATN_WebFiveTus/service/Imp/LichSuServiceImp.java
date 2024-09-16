package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.HoaDonChiTietDTO;
import com.example.DATN_WebFiveTus.dto.LichSuDTO;
import com.example.DATN_WebFiveTus.entity.HoaDonChiTiet;
import com.example.DATN_WebFiveTus.entity.LichSu;
import com.example.DATN_WebFiveTus.repository.LichSuRepository;
import com.example.DATN_WebFiveTus.service.LichSuService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LichSuServiceImp implements LichSuService {

    private LichSuRepository lichSuRepository;

    private ModelMapper modelMapper;

    @Autowired
    public LichSuServiceImp(LichSuRepository lichSuRepository, ModelMapper modelMapper) {
        this.lichSuRepository = lichSuRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<LichSuDTO> getAll() {
        return lichSuRepository.findAll().stream().map((lichSu) ->modelMapper.map(lichSu,LichSuDTO.class)).collect(Collectors.toList());
    }

    @Override
    public LichSuDTO save(LichSuDTO lichSuDTO) {
        LichSu lichSu=modelMapper.map(lichSuDTO,LichSu.class);
        LichSu lichSuSave=lichSuRepository.save(lichSu);
        return modelMapper.map(lichSuSave,LichSuDTO.class);
    }

    @Override
    public LichSuDTO update(Integer id, LichSuDTO lichSuDTO) {
        return null;
    }

    @Override
    public void checkin(HoaDonChiTietDTO hoaDonChiTietDTO) {
        // Tạo đối tượng LichSu để lưu lịch sử check-in
        LichSuDTO lichSuCheckin = new LichSuDTO();
        lichSuCheckin.setIdHoaDonChiTiet(hoaDonChiTietDTO.getId());
        System.out.println("AHhihi: "+hoaDonChiTietDTO.getId() +"hihi"+hoaDonChiTietDTO.getHoVaTenKhachHang());
        // Lấy thời gian hiện tại và định dạng
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = now.format(formatter);
        lichSuCheckin.setThoiGianCheckin(now);

        lichSuCheckin.setMoTa("Khách hàng: "+hoaDonChiTietDTO.getHoVaTenKhachHang()
                +"có mã HDCT" + hoaDonChiTietDTO.getMaHoaDonChiTiet() + " đã check-in lúc " + formattedDateTime);

        // Lưu lịch sử check-in vào cơ sở dữ liệu
        LichSu lichSuSave=modelMapper.map(lichSuCheckin,LichSu.class);
        lichSuRepository.save(lichSuSave);
    }


//    @Override
//    public void checkin(HoaDonChiTietDTO hoaDonChiTietDTO) {
//        // Chuyển đổi HoaDonChiTietDTO sang HoaDonChiTiet bằng ModelMapper
//        HoaDonChiTiet hoaDonChiTiet = modelMapper.map(hoaDonChiTietDTO, HoaDonChiTiet.class);
//
//
//        // Tạo đối tượng LichSu để lưu lịch sử check-in
//        LichSu lichSuCheckin = new LichSu();
//        lichSuCheckin.setHoaDonChiTiet(hoaDonChiTiet);
//
//        // Lấy thời gian hiện tại và định dạng
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//        String formattedDateTime = now.format(formatter);
//
//        lichSuCheckin.setThoiGianCheckin(now);
//
//        // Tạo mô tả với thời gian đã định dạng
////        lichSuCheckin.setMoTa("Khách hàng: "+hoaDonChiTiet.getHoaDon().getKhachHang().getMaKhachHang()
////                +"-"+hoaDonChiTiet.getHoaDon().getKhachHang().getHoVaTen() +"có mã HDCT" + hoaDonChiTiet.getId() + " đã check-in lúc " + formattedDateTime);
//
//        lichSuCheckin.setMoTa("Khách hàng: "+hoaDonChiTiet.getHoaDon().getKhachHang().getHoVaTen()
//                +"có mã HDCT" + hoaDonChiTiet.getId() + " đã check-in lúc " + formattedDateTime);
//
////        lichSuCheckin.setMoTa("có mã HDCT" + hoaDonChiTiet.getId() + " đã check-in lúc " + formattedDateTime);
//
//        // Lưu lịch sử check-in vào cơ sở dữ liệu
//        lichSuRepository.save(lichSuCheckin);
//    }
}
