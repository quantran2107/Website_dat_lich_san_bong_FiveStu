package com.example.DATN_WebFiveTus.service;


import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NhanVienService {
    List<NhanVienDTO> getAll();

    Boolean updateNew(NhanVienDTO nv);

    Boolean addNew(NhanVienDTO nv);

    List<NhanVienDTO> getActiveNV();

    List<NhanVienDTO> getInactiveNV();

    Boolean addMore(MultipartFile file);
}
