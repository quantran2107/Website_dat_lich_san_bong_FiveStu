package com.example.DATN_WebFiveTus.config;

import com.example.DATN_WebFiveTus.filter.JwtAuthenticationEntrypoint;
import com.example.DATN_WebFiveTus.filter.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableMethodSecurity
@AllArgsConstructor
public class SpringSecurityConfig {

    private UserDetailsService userDetailsService;

    private JwtAuthenticationEntrypoint authenticationEntryPoint;

    private JwtAuthenticationFilter authenticationFilter;

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable() // Vô hiệu hóa CSRF, có thể cần cho các API.
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/loginPages", "/api/auth/login").permitAll() // Cho phép truy cập không cần xác thực
                        .requestMatchers("/api/auth/test").hasRole("ADMIN") // Chỉ cho phép ADMIN truy cập
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN") // Chỉ cho phép ADMIN truy cập
                        .anyRequest().permitAll()// Tất cả các yêu cầu khác cần xác thực
                )
                .cors() // Thêm CORS vào cấu hình
                .and()
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)); // Xử lý yêu cầu không được phép truy cập

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class); // Thêm filter cho xác thực JWT

        return http.build();
    }

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Thay đổi tùy thuộc vào domain của bạn
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
