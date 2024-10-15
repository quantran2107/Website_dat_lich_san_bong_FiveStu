//package com.example.DATN_WebFiveTus.security;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.sql.DataSource;
//@Configuration
//public class UserConfig {
//    @Bean
//    @Autowired
//    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//        jdbcUserDetailsManager.setUsersByUsernameQuery("SELECT ma,passwords,enableds FROM Account where ma=?");
//        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("SELECT ma,authority FROM Roles where ma=?");
//        return jdbcUserDetailsManager;
//    }
//
////    @Bean
////    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
////        httpSecurity.authorizeHttpRequests(
////                authorize -> authorize
////                        .requestMatchers("/admin/**").hasRole("ADMIN")
////                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
////                        .requestMatchers("/member/**").hasAnyRole("MANAGER", "MEMBER")
////                        .requestMatchers("/public/**").permitAll()
////                        .anyRequest().permitAll()
////
////        ).formLogin(
////                form -> form.loginPage("/showLoginPage").loginProcessingUrl("/authenticateTheUser").permitAll()
////        )
////                .logout(
////                        logout -> logout.permitAll()
////                );
////        httpSecurity.httpBasic(Customizer.withDefaults());
////        httpSecurity.csrf(csrf -> csrf.disable());
////        // csrf - cross site request forgery
////
////        return httpSecurity.build();
////    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
//                        .requestMatchers("/member/**").hasAnyRole("MANAGER", "MEMBER")
////                        .requestMatchers("/").permitAll()
//                        .anyRequest().permitAll()// Đảm bảo tất cả các yêu cầu khác cần phải được xác thực
//                )
//                .formLogin(form -> form
//                        .loginPage("/loginPages") // Trang đăng nhập tùy chỉnh
//                        .loginProcessingUrl("/authenticateTheUser") // URL xử lý đăng nhập
//                        .defaultSuccessUrl("/listSanBong", true) // Chuyển hướng đến trang chính sau khi đăng nhập thành công
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/logout") // URL xử lý đăng xuất
//                        .logoutSuccessUrl("/login?logout") // Chuyển hướng đến trang đăng nhập sau khi đăng xuất
//                        .permitAll()
//                )
//                .httpBasic()
//                .and()
//                .csrf(csrf -> csrf.disable()); // Tắt CSRF nếu không sử dụng cho API
//        return http.build();
//    }
//}
