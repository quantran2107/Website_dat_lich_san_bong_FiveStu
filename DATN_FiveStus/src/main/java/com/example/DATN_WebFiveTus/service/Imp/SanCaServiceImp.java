package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.dto.SanCaDTO;
import com.example.DATN_WebFiveTus.entity.Ca;
import com.example.DATN_WebFiveTus.entity.LoaiSan;
import com.example.DATN_WebFiveTus.entity.NgayTrongTuan;
import com.example.DATN_WebFiveTus.entity.SanBong;
import com.example.DATN_WebFiveTus.entity.SanCa;
import com.example.DATN_WebFiveTus.exception.ResourceNotfound;
import com.example.DATN_WebFiveTus.repository.CaRepository;
import com.example.DATN_WebFiveTus.repository.LoaiSanRepository;
import com.example.DATN_WebFiveTus.repository.NgayTrongTuanRepository;
import com.example.DATN_WebFiveTus.repository.SanBongRepository;
import com.example.DATN_WebFiveTus.repository.SanCaRepository;
import com.example.DATN_WebFiveTus.service.SanCaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SanCaServiceImp implements SanCaService {

    private SanCaRepository sanCaRepository;

    private NgayTrongTuanRepository ngayTrongTuanRepository;

    private SanBongRepository sanBongRepository;


    private CaRepository caRepository;

    private LoaiSanRepository loaiSanRepository;

    private ModelMapper modelMapper;

    @Autowired
    public SanCaServiceImp(SanCaRepository sanCaRepository, NgayTrongTuanRepository ngayTrongTuanRepository
            , SanBongRepository sanBongRepository, CaRepository caRepository, LoaiSanRepository loaiSanRepository, ModelMapper modelMapper) {
        this.sanCaRepository = sanCaRepository;
        this.ngayTrongTuanRepository = ngayTrongTuanRepository;
        this.sanBongRepository = sanBongRepository;
        this.caRepository = caRepository;
        this.loaiSanRepository = loaiSanRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<SanCaDTO> getAll() {
        return sanCaRepository.findAll().stream()
                .map((sanCa) -> modelMapper.map(sanCa,SanCaDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<SanCaDTO> getAllJoinFetch() {
        return sanCaRepository.getAllJoinFetch().stream()
                .map((sanCa) -> modelMapper.map(sanCa,SanCaDTO.class)).collect(Collectors.toList());
    }

    @Override
    public SanCaDTO getOne(Integer id) {
        return modelMapper.map(sanCaRepository.findById(id).orElseThrow(()->new ResourceNotfound("Không tồn tại id: "+id)),SanCaDTO.class);
    }

    @Override
    public SanCaDTO save(SanCaDTO sanCaDTO) {
        Ca ca=caRepository.findById(sanCaDTO.getIdCa()).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại id ca: "+sanCaDTO.getIdCa()));

        SanBong sanBong=sanBongRepository.findById(sanCaDTO.getIdSanBong()).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại id sân bóng: "+sanCaDTO.getIdSanBong()));

        NgayTrongTuan ngayTrongTuan=ngayTrongTuanRepository.findById(sanCaDTO.getIdNgayTrongTuan()).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại id ngày trong tuần: "+sanCaDTO.getIdNgayTrongTuan()));


        SanCa sanCa=modelMapper.map(sanCaDTO,SanCa.class);
        sanCa.setTrangThai("Đã đặt nhé");
        sanCa.setCa(ca);
        sanCa.setSanBong(sanBong);
        sanCa.setNgayTrongTuan(ngayTrongTuan);
        SanCa sanCaSave=sanCaRepository.save(sanCa);
        return modelMapper.map(sanCaSave,SanCaDTO.class);
    }

    @Override
    public SanCaDTO update(Integer id, SanCaDTO sanCaDTO) {
        SanCa sanCa=sanCaRepository.findById(id).orElseThrow(()-> new ResourceNotfound("Không tồn tại update id: "+id));
        Ca ca=caRepository.findById(sanCaDTO.getIdCa()).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại id ca: "+sanCaDTO.getIdCa()));

        SanBong sanBong=sanBongRepository.findById(sanCaDTO.getIdSanBong()).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại id sân bóng: "+sanCaDTO.getIdSanBong()));

        NgayTrongTuan ngayTrongTuan=ngayTrongTuanRepository.findById(sanCaDTO.getIdNgayTrongTuan()).orElseThrow(()
                ->new ResourceNotfound("Không tồn tại id ngày trong tuần: "+sanCaDTO.getIdNgayTrongTuan()));
        sanCa.setCa(ca);
        sanCa.setSanBong(sanBong);
        sanCa.setNgayTrongTuan(ngayTrongTuan);
        sanCa.setTrangThai(sanCaDTO.getTrangThai());
        SanCa sanCaUpdate=sanCaRepository.save(sanCa);
        return modelMapper.map(sanCaUpdate,SanCaDTO.class);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void deletedAt(Integer id) {
        SanCa sanCa=sanCaRepository.findById(id).orElseThrow(()-> new ResourceNotfound("Không tồn tại xoá id: "+id));
        sanCaRepository.deletedAt(id);
    }

    @Override
    public List<SanCaDTO> listAllSortPage(Integer pageNum, String sortDirection, int[] totalPageElement) {
        Sort sortS = Sort.by("sanBong.tenSanBong").and(Sort.by("ca.tenCa"));;
        if (sortDirection.equalsIgnoreCase("asc")) {
            sortS = sortS.ascending();
        } else if (sortDirection.equalsIgnoreCase("desc")) {
            sortS = sortS.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, 5, sortS);
        Page<SanCa> sanCaPage = sanCaRepository.findBySanCaPage(pageable);

        totalPageElement[0] = sanCaPage.getTotalPages();
        totalPageElement[1] = (int) sanCaPage.getTotalElements();

        return sanCaPage.getContent().stream()
                .map(sanCa -> modelMapper.map(sanCa, SanCaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SanCaDTO> searchKeyWords(Integer pageNum, String keyWords, String sortDirection, int[] totalPageElement, Integer id) {
        Sort sortS = Sort.by("sanBong.tenSanBong").and(Sort.by("ca.tenCa"));;
        if (sortDirection.equalsIgnoreCase("asc")) {
            sortS = sortS.ascending();
        } else if (sortDirection.equalsIgnoreCase("desc")) {
            sortS = sortS.descending();
        }

        Pageable pageable = PageRequest.of(pageNum - 1, 5, sortS);
        Page<SanCa> sanCaPage = sanCaRepository.search(id,keyWords.trim(),pageable);

        totalPageElement[0] = sanCaPage.getTotalPages();
        totalPageElement[1] = (int) sanCaPage.getTotalElements();

        return sanCaPage.getContent().stream()
                .map(sanCa -> modelMapper.map(sanCa, SanCaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<SanCaDTO> findByTrangThai(Integer idCa, String thuTrongTuan, String trangThai) {
        List<SanCa> list = sanCaRepository.sanHopLe(idCa,thuTrongTuan,trangThai);
        return list.stream().map(sanCa -> modelMapper.map(sanCa,SanCaDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<SanCaDTO> hienThiSanTrong(Integer idLoaiSan, List<String> thuTrongTuanList, java.util.Date startDate, Date endDate) {
        List<SanCa> list = sanCaRepository.hienThiSanTrong(idLoaiSan, thuTrongTuanList, startDate, endDate);
        return list.stream()
                .map(sanCa -> modelMapper.map(sanCa, SanCaDTO.class))
                .collect(Collectors.toList());
    }


//    public List<SanCaDTO> findSanCaAndNgayDenSanByHoaDonChiTietId(Integer id) {
//        return sanCaRepository.findSanCaAndNgayDenSanByHoaDonChiTietId(id);
//    }

    @Override
    public List<SanCaDTO> findSanCaBySan(Integer idSanBong, Integer idNgayTrongTuan) {
        return sanCaRepository.findSanCaBySan(idSanBong,idNgayTrongTuan).stream()
                .map((sanCa) -> modelMapper.map(sanCa,SanCaDTO.class)).collect(Collectors.toList());
    }

    @Override
    public SanCaDTO getOneSanCaByAll(Integer idSanBong, Integer idNgayTrongTuan, Integer idCa) {
        return modelMapper.map(sanCaRepository.getOneSanCaByAll
                (idSanBong,idNgayTrongTuan, idCa),SanCaDTO.class);
    }

    //Ly them
    @Override
    public List<SanCaDTO> findSanCaByNhieuNgay(Integer idSanBong, List<Integer> listIdNgayTrongTuan) {
        return sanCaRepository.findSanCaByNhieuNgay(idSanBong,listIdNgayTrongTuan).stream()
                .map((sanCa) -> modelMapper.map(sanCa,SanCaDTO.class)).collect(Collectors.toList());
    }

    @Override
    public void addSanCaForSanBongWithIdLoaiSan2(int idLoaiSan, SanCaDTO sanCaDTO) {
        // Lấy danh sách sân bóng theo idLoaiSan
        List<SanBong> sanBongs = sanBongRepository.findByLoaiSanId(idLoaiSan);
        if (sanBongs.isEmpty()) {
            throw new ResourceNotfound("Không tìm thấy sân bóng nào với idLoaiSan: " + idLoaiSan);
        }

        LoaiSan loaiSan = loaiSanRepository.findById(idLoaiSan).get();
//        if (sanBongs.isEmpty()) {
//            throw new ResourceNotfound("Không tìm thấy sân bóng nào với idLoaiSan: " + idLoaiSan);
//        }

        // Kiểm tra tồn tại các ca
        List<Ca> cas = caRepository.findAllById(sanCaDTO.getIdCaList());
        if (cas.size() != sanCaDTO.getIdCaList().size()) {
            throw new ResourceNotfound("Một hoặc nhiều ca không tồn tại.");
        }

        // Kiểm tra tồn tại các ngày trong tuần
        List<NgayTrongTuan> ngayTrongTuans = ngayTrongTuanRepository.findAllById(sanCaDTO.getIdNgayTrongTuanList());
        if (ngayTrongTuans.size() != sanCaDTO.getIdNgayTrongTuanList().size()) {
            throw new ResourceNotfound("Một hoặc nhiều ngày trong tuần không tồn tại.");
        }

        // Lưu dữ liệu
        for (SanBong sanBong : sanBongs) {
            for (NgayTrongTuan ngayTrongTuan : ngayTrongTuans) {
                for (Ca ca : cas) {
//                     Kiểm tra xem SanCa đã tồn tại chưa
                    List<SanCaDTO> existingSanCaList = getListSanCaExits(sanBong.getLoaiSan().getId(),List.of(sanBong.getId()), List.of(ngayTrongTuan.getId()), List.of(ca.getId()));

                    if (!existingSanCaList.isEmpty()) {
                        System.out.println("Trống nha");
                        // Nếu đã tồn tại, cập nhật giá cho tất cả các bản ghi
                        for (SanCaDTO existingSanCa : existingSanCaList) {
                            SanCa sanCaToUpdate = modelMapper.map(existingSanCa, SanCa.class);
                            sanCaToUpdate.setGia(sanCaDTO.getGia());
                            sanCaToUpdate.setTrangThai("Hoạt động");
                            sanCaToUpdate.setUpdatedAt(LocalDateTime.now());
                            sanCaRepository.save(sanCaToUpdate);
                        }
                    } else {
                        System.out.println("Mới nha");
//                         Nếu chưa tồn tại, tạo mới
                        SanCa newSanCa = new SanCa();
                        newSanCa.setSanBong(sanBong);
                        newSanCa.setNgayTrongTuan(ngayTrongTuan);
                        newSanCa.setCa(ca);
                        newSanCa.setGia(sanCaDTO.getGia());
                        newSanCa.setTrangThai("Hoạt động");
                        newSanCa.setDeletedAt(false);

                        // Lưu sanCa vào cơ sở dữ liệu
                        sanCaRepository.save(newSanCa);
                    }
                }
            }
        }
    }

    @Override
    public List<SanCaDTO> getListSanCaExits(Integer idLoaiSan,List<Integer>  idSanBong, List<Integer> idNgayTrongTuan, List<Integer> idCa) {
        return sanCaRepository.getListSanCaExits(idLoaiSan, idSanBong, idNgayTrongTuan, idCa).stream()
                .map(sanCa -> modelMapper.map(sanCa, SanCaDTO.class)).collect(Collectors.toList());
    }



}
