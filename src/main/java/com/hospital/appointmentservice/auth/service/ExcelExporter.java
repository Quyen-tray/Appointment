package com.hospital.appointmentservice.auth.service;

import com.hospital.appointmentservice.auth.model.Login_audit;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ExcelExporter {

    public static void exportLoginAudit(List<Login_audit> audits, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Login Audit");

        // Tạo header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Username", "Event", "Timestamp", "IP Address", "User Agent"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Định dạng thời gian
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Ghi dữ liệu
        int rowIdx = 1;
        for (Login_audit audit : audits) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(audit.getUsername());
            row.createCell(1).setCellValue(audit.getEvent());
            row.createCell(2).setCellValue(audit.getTimestamp().toString());
            row.createCell(3).setCellValue(audit.getIpAddress());
            row.createCell(4).setCellValue(audit.getUserAgent());
        }

        // Cấu hình response
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=login_audit.xlsx");

        workbook.write(response.getOutputStream());
        workbook.close();
    }
}

