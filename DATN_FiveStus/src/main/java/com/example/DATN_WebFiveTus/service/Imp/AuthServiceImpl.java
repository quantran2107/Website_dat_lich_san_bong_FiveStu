package com.example.DATN_WebFiveTus.service.Imp;


import com.example.DATN_WebFiveTus.config.RoleFactory;
import com.example.DATN_WebFiveTus.config.security.CheckRole;
import com.example.DATN_WebFiveTus.config.security.CookieUtils;
import com.example.DATN_WebFiveTus.config.security.jwt.JwtUtils;
import com.example.DATN_WebFiveTus.dto.ApiResponseDto;
import com.example.DATN_WebFiveTus.dto.request.ChangePassRequest;
import com.example.DATN_WebFiveTus.dto.request.SignInRequestDto;
import com.example.DATN_WebFiveTus.dto.request.SignUpRequestDto;
import com.example.DATN_WebFiveTus.dto.response.SignInResponseDto;
import com.example.DATN_WebFiveTus.entity.KhachHang;
import com.example.DATN_WebFiveTus.entity.auth.ResponseStatus;
import com.example.DATN_WebFiveTus.entity.auth.Role;
import com.example.DATN_WebFiveTus.entity.auth.User;
import com.example.DATN_WebFiveTus.exception.RoleNotFoundException;
import com.example.DATN_WebFiveTus.exception.UserAlreadyExistsException;
import com.example.DATN_WebFiveTus.repository.KhachHangRepository;
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


    @Override
    public ResponseEntity<ApiResponseDto<?>> signUp(SignUpRequestDto signUpRequestDto)
            throws UserAlreadyExistsException, RoleNotFoundException {
        if (userService.existByEmail(signUpRequestDto.getEmail())) {
            throw new UserAlreadyExistsException("Registration Failed: Provided email already exists. Try sign in or provide another email.");
        }
        if (userService.existByUsername(signUpRequestDto.getUsername())) {
            throw new UserAlreadyExistsException("Registration Failed: Provided username already exists. Try sign in or provide another username.");
        }

        User user = createUser(signUpRequestDto);
        userService.save(user);
        KhachHang khachHang = new KhachHang();
        khachHang.setEmail(signUpRequestDto.getEmail());
        khachHang.setMaKhachHang(signUpRequestDto.getEmail().substring(0, signUpRequestDto.getEmail().indexOf("@")));
        khachHang.setMatKhau(passwordEncoder.encode(signUpRequestDto.getPassword()));
        khachHangRepository.save(khachHang);
        boolean mail = mailFunction(signUpRequestDto);
        if (!mail) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
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

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

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

    public Boolean mailFunction(SignUpRequestDto dto) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            Context context = new Context();
            context.setVariable("username", dto.getEmail());
            context.setVariable("password", dto.getPassword());

            String html = springTemplateEngine.process("userNV", context);

            helper.setTo(dto.getEmail());
            helper.setSubject("Thông báo tài khoản và mật khẩu");
            helper.setText(html, true);

            javaMailSender.send(mimeMessage);

            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
