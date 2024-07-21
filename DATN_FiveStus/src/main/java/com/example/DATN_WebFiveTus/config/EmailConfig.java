package com.example.DATN_WebFiveTus.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

    @Value("${mailServe.host}")
    private String host;

    @Value("${mailServe.port}")
    private int port;

    @Value("${mailServe.email}")
    private String email;

    @Value("${mailServe.password}")
    private String password;

    @Value("${mailServe.protocol}")
    private String protocol;

    @Value("${mailServe.isSSL}")
    private boolean isSSL;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(email);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");

        // Cấu hình properties của mail sender
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.enable", isSSL);
        props.put("mail.smtp.from", email);
        props.put("mail.smtp.connectiontimeout", "5000"); // 3 giây
        props.put("mail.smtp.timeout", "5000"); // 3 giây
        props.put("mail.smtp.writetimeout", "3000"); // 3 giây
        props.put("mail.smtp.maxtotalconnections", "50"); // Tăng số kết nối song song
        props.put("mail.smtp.poolsize", "50"); // Tăng kích thước pool kết nối
        props.put("mail.debug", "false"); // Có thể bỏ đi khi đã vào production

        return mailSender;
    }
}

