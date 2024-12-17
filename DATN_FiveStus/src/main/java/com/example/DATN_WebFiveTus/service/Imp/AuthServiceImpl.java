package com.example.DATN_WebFiveTus.service.Imp;


import com.example.DATN_WebFiveTus.config.RoleFactory;
import com.example.DATN_WebFiveTus.config.security.CheckRole;
import com.example.DATN_WebFiveTus.config.security.CookieUtils;
import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import com.example.DATN_WebFiveTus.dto.ApiResponseDto;
import com.example.DATN_WebFiveTus.dto.request.ChangePassRequest;
import com.example.DATN_WebFiveTus.dto.request.OtpRequest;
import com.example.DATN_WebFiveTus.dto.request.SignInRequestDto;
import com.example.DATN_WebFiveTus.dto.request.SignUpRequestDto;
import com.example.DATN_WebFiveTus.dto.response.SignInResponseDto;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.entity.NhanVien;
import com.example.DATN_WebFiveTus.entity.auth.ResponseStatus;
import com.example.DATN_WebFiveTus.entity.auth.Role;
import com.example.DATN_WebFiveTus.entity.auth.User;
import com.example.DATN_WebFiveTus.exception.RoleNotFoundException;
import com.example.DATN_WebFiveTus.exception.UserAlreadyExistsException;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
import com.example.DATN_WebFiveTus.repository.NhanVienReposity;
import com.example.DATN_WebFiveTus.repository.UserRepository;
import com.example.DATN_WebFiveTus.service.AuthService;
import com.example.DATN_WebFiveTus.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleFactory roleFactory;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CheckRole checkRole;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private NhanVienReposity nhanVienReposity;

    @Override
    public ResponseEntity<ApiResponseDto<?>> signUp(SignUpRequestDto signUpRequestDto)
            throws UserAlreadyExistsException, RoleNotFoundException {
        if (userService.existByEmail(signUpRequestDto.getEmail())) {
            throw new UserAlreadyExistsException("Đăng ký không thành công: Email này đã tồn tại trong hệ thống.");
        }
        if (userService.existByUsername(signUpRequestDto.getUsername())) {
            throw new UserAlreadyExistsException("Đăng ký không thành công: Tên người dùng đã tồn tại trong hệ thống.");
        }
        KhachHang kh = khachHangRepository.findKhachHangBySoDienThoai(signUpRequestDto.getPhoneNumber());
        if (kh!=null) {
            throw new UserAlreadyExistsException("Đăng ký không thành công: Số điện thoại đã tồn tại trong hệ thống.");
        }

        User user = createUser(signUpRequestDto);
        userService.save(user);
        KhachHang khachHang = new KhachHang();
        khachHang.setEmail(signUpRequestDto.getEmail());
        khachHang.setMaKhachHang(signUpRequestDto.getEmail().substring(0, signUpRequestDto.getEmail().indexOf("@")));
        khachHang.setMatKhau(passwordEncoder.encode(signUpRequestDto.getPassword()));
        khachHang.setSoDienThoai(signUpRequestDto.getPhoneNumber());
        khachHang.setTrangThai("active");
        khachHang.setHoVaTen(signUpRequestDto.getName());
        khachHangRepository.save(khachHang);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponseDto.builder()
                        .status(String.valueOf(ResponseStatus.SUCCESS))
                        .message("User account has been successfully created!")
                        .build()
        );
    }

    @Override
    public ResponseEntity<ApiResponseDto<?>> signIn(SignInRequestDto signInRequestDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequestDto.getUsername(), signInRequestDto.getPassword()));
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        if (roles.contains("ROLE_EMPLOYEE")) {
            NhanVien nv = nhanVienReposity.findByUsername(signInRequestDto.getUsername());
            if (nv != null && nv.getTrangThai().equals("inactive")) {
                return ResponseEntity.ok(
                        ApiResponseDto.builder()
                                .status(String.valueOf(ResponseStatus.FAIL))
                                .message("Tài khoản đã dừng hoạt động!")
                                .response(null)
                                .build()
                );
            }
        }
        if (roles.contains("ROLE_USER")) {
            KhachHang kh = khachHangRepository.findKhachHangByEmail1(signInRequestDto.getUsername()).orElse(null);
            if (kh != null && kh.getTrangThai().equals("inactive")) {
                return ResponseEntity.ok(
                        ApiResponseDto.builder()
                                .status(String.valueOf(ResponseStatus.FAIL))
                                .message("Tài khoản đã dừng hoạt động!")
                                .response(null)
                                .build()
                );
            }
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        SignInResponseDto signInResponseDto = SignInResponseDto.builder()
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .id(userDetails.getId())
                .token(jwt)
                .type("Bearer")
                .roles(roles)
                .build();

        return ResponseEntity.ok(
                ApiResponseDto.builder()
                        .status(String.valueOf(ResponseStatus.SUCCESS))
                        .message("Sign in successfull! Role")
                        .response(signInResponseDto)
                        .build()
        );
    }

    @Override
    public ResponseEntity<?> logout(String userName) {
        if (jwtUtils.deleteToken(userName)){
            return ResponseEntity.ok(
                    ApiResponseDto.builder()
                            .status(String.valueOf(ResponseStatus.SUCCESS))
                            .message("Log out successfull!")
                            .response("")
                            .build()
            );
        };
        return ResponseEntity.ok(
                ApiResponseDto.builder()
                        .status(String.valueOf(ResponseStatus.FAIL))
                        .message("Log out fail!")
                        .response("")
                        .build()
        );

    }

    @Override
    public ResponseEntity<?> getRole(HttpServletRequest request) {
        return ResponseEntity.ok(checkRole.getListRole(request));
    }

    @Override
    public ResponseEntity<?> getOtp(String email) throws UserAlreadyExistsException {
        if (!userService.existByEmail(email)) {
            throw new UserAlreadyExistsException("Email này không tồn tại trong hệ thống.");
        }
        boolean mail = mailFunction(email,"otp","otp","Thông báo về mã của bạn",jwtUtils.generateOtp(email));
        if (!mail) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(true);
    }

    @Override
    public ResponseEntity<?> checkOtp(OtpRequest request) {
        if (jwtUtils.fillOtp(request.getOtp(), request.getEmail())){
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + request.getEmail()));
            String password = generateMK(10);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            jwtUtils.removeOtp(request.getEmail());
            boolean mail = mailFunction(request.getEmail(),"pass","password","Thông báo về mật khẩu của bạn",password);
            if (!mail) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }

    @Override
    public ResponseEntity<?> changePass(HttpServletRequest request, ChangePassRequest changePassRequest) {
        String token = CookieUtils.getCookie(request, "authToken");
        if (token != null && jwtUtils.validateJwtToken(token) && jwtUtils.checkBlackList(token)) {
            String username = jwtUtils.getUserNameFromJwtToken(token);
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
            user.setPassword(passwordEncoder.encode(changePassRequest.getPassword()));
            userService.save(user);
            return ResponseEntity.ok(ApiResponseDto.builder()
                    .status(String.valueOf(ResponseStatus.SUCCESS))
                    .response(true).build());
        }
        return ResponseEntity.ok(ApiResponseDto.builder()
                .status(String.valueOf(ResponseStatus.FAIL))
                .message("Thay đổi ko thành công")
                .response(false).build());

    }


    private User createUser(SignUpRequestDto signUpRequestDto) throws RoleNotFoundException {
        return User.builder()
                .email(signUpRequestDto.getEmail())
                .username(signUpRequestDto.getUsername())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .enabled(true)
                .roles(determineRoles(signUpRequestDto.getRoles()))
                .build();
    }

    private Set<Role> determineRoles(Set<String> strRoles) throws RoleNotFoundException {
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            roles.add(roleFactory.getInstance("user"));
        } else {
            for (String role : strRoles) {
                roles.add(roleFactory.getInstance(role));
            }
        }
        return roles;
    }

    public Boolean mailFunction(String email,String variable,String template,String subject,String code) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            Context context = new Context();
            context.setVariable(variable, code);

            String html = springTemplateEngine.process(template, context);

            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(html, true);

            javaMailSender.send(mimeMessage);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String generateMK(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom RANDOM = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}