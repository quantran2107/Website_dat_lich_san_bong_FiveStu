package com.example.DATN_WebFiveTus.service;


import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.exception.RoleNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface NhanVienService {
    List<NhanVienDTO> getAll();

    Boolean updateNew(NhanVienDTO nv);

    Object addNew(NhanVienDTO nv) throws RoleNotFoundException;

    List<NhanVienDTO> getActiveNV();

    List<NhanVienDTO> getInactiveNV();

    Boolean addMore(MultipartFile file);

    ResponseEntity<?> getOneNv(int id);

    ResponseEntity<?> getForCode(String maNV);

    NhanVienDTO getNV(HttpServletRequest request);
}
