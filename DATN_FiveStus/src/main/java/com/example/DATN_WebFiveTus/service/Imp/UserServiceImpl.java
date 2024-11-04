package com.example.DATN_WebFiveTus.service.Imp;

import com.example.DATN_WebFiveTus.config.security.CheckRole;
import com.example.DATN_WebFiveTus.config.security.CookieUtils;
import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import com.example.DATN_WebFiveTus.dto.ApiResponseDto;
import com.example.DATN_WebFiveTus.dto.KhachHangDTO;
import com.example.DATN_WebFiveTus.dto.NhanVienDTO;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.entity.auth.ResponseStatus;
import com.example.DATN_WebFiveTus.entity.auth.User;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
import com.example.DATN_WebFiveTus.repository.UserRepository;
import com.example.DATN_WebFiveTus.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final JwtUtils jwtUtils;

    private final KhachHangRepository khachHangRepository;

    private final ModelMapper modelMapper;


    public UserServiceImpl(UserRepository userRepository, JwtUtils jwtUtils, KhachHangRepository khachHangRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.khachHangRepository = khachHangRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean existByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> getUserInRequest(HttpServletRequest request) {
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
            KhachHang khachHang = khachHangRepository.findKhachHangByEmail(user.getEmail());
            khachHang.setMatKhau(user.getPassword());
            return ResponseEntity.ok(ApiResponseDto.builder()
                    .status(String.valueOf(ResponseStatus.SUCCESS))
                    .response(modelMapper.map(khachHang,KhachHangDTO.class)).build());
        }
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(ResponseStatus.FAIL))
                .message("Customer doesn't login!")
                .response(null).build());
    }

}