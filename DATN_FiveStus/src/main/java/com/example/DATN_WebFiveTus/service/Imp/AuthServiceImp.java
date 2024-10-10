//package com.example.DATN_WebFiveTus.service.Imp;
//
//import com.example.DATN_WebFiveTus.dto.JwtAuthResponse;
//import com.example.DATN_WebFiveTus.dto.LoginDTO;
//import com.example.DATN_WebFiveTus.entity.Account;
//import com.example.DATN_WebFiveTus.entity.Roles;
//import com.example.DATN_WebFiveTus.filter.JwtTokenProvider;
//import com.example.DATN_WebFiveTus.repository.AccountRepository;
//import com.example.DATN_WebFiveTus.repository.RolesRepository;
//import com.example.DATN_WebFiveTus.service.AuthService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class AuthServiceImp implements AuthService {
//
//    private AccountRepository accountRepository;
//
//    private RolesRepository rolesRepository;
//
//    private PasswordEncoder passwordEncoder;
//
//    private AuthenticationManager authenticationManager;
//
//    private JwtTokenProvider jwtTokenProvider;
//
//    @Autowired
//    public AuthServiceImp(AccountRepository accountRepository, RolesRepository rolesRepository,
//                          PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
//                          JwtTokenProvider jwtTokenProvider) {
//        this.accountRepository = accountRepository;
//        this.rolesRepository = rolesRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.authenticationManager = authenticationManager;
//        this.jwtTokenProvider = jwtTokenProvider;
//    }
//
//
//    @Override
//    public JwtAuthResponse login(LoginDTO loginDto) {
//
//        // Xác thực thông tin đăng nhập
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        loginDto.getUsername(),
//                        loginDto.getPassword()
//                )
//        );
//
//        // Đặt thông tin xác thực vào SecurityContextHolder
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Tạo token JWT
//        String token = jwtTokenProvider.generateToken(authentication);
//
//        // Tìm kiếm tài khoản dựa trên username hoặc email
//        Optional<Account> users = accountRepository.findByUsernameOrEmail(
//                loginDto.getUsername(), loginDto.getUsername()
//        );
//
//        // Biến để lưu danh sách vai trò
//        String roles = null;
//
//        if (users.isPresent()) {
//            Account userLoggin = users.get();
//
//            // Lấy tất cả các vai trò của người dùng và ghép chúng lại
//            roles = userLoggin.getRoles().stream()
//                    .map(Roles::getAuthority)
//                    .collect(Collectors.joining(","));
//        } else {
//            // Xử lý khi không tìm thấy tài khoản
//            throw new UsernameNotFoundException("User not found with username or email: " + loginDto.getUsername());
//        }
//
//        // Tạo phản hồi cho người dùng với token và danh sách vai trò
//        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
//        jwtAuthResponse.setRoles(roles);
//        jwtAuthResponse.setAccessToken(token);
//        return jwtAuthResponse;
//    }
//
//
//
//}