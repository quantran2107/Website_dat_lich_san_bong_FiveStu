package com.example.DATN_WebFiveTus.config;

import jakarta.servlet.http.HttpServletResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AbstractExporter {

    public void setResponseHeader(HttpServletResponse httpServletResponse, String contentType, String extension) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormat.format(new Date());
        String fileName = "mtam77_" + timestamp + extension; // Đặt dấu gạch dưới và định dạng file là .xlsx

        httpServletResponse.setContentType(contentType);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName; // Sửa lại phần header để đặt tên file đúng cách
        httpServletResponse.setHeader(headerKey, headerValue);
    }
}
