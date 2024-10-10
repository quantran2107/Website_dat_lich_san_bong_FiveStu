//package com.example.DATN_WebFiveTus.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Tất cả các route
//                .allowedOrigins("http://localhost:8080/admin/page") // Thay bằng URL của client
//                .allowedHeaders("Authorization", "application/json") // Thêm Authorization
//                .allowedMethods("GET", "POST", "PUT", "DELETE"); // Các phương thức cho phép
//    }
//}