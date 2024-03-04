package com.gdschongik.gdsc.global.util;

import static com.gdschongik.gdsc.global.common.constant.WorkbookConstant.*;

import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.Member;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelUtil {

    public static Workbook createMemberWorkbook() {
        return new HSSFWorkbook();
    }

    public static void createSheet(Workbook workbook, String sheetName, List<Member> content) {
        Sheet sheet = setUpSheet(workbook, sheetName);

        content.forEach(member -> {
            Row memberRow = sheet.createRow(sheet.getLastRowNum() + 1);
            memberRow.createCell(0).setCellValue(member.getCreatedAt().toString());
            memberRow.createCell(1).setCellValue(member.getName());
            memberRow.createCell(2).setCellValue(member.getStudentId());
            memberRow
                    .createCell(3)
                    .setCellValue(Optional.ofNullable(member.getDepartment())
                            .map(Department::getDepartmentName)
                            .orElse(""));
            memberRow.createCell(4).setCellValue(member.getPhone());
            memberRow.createCell(5).setCellValue(member.getEmail());
            memberRow.createCell(6).setCellValue(member.getDiscordUsername());
            memberRow.createCell(7).setCellValue(member.getNickname());
        });
    }

    private static Sheet setUpSheet(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);

        Row row = sheet.createRow(0);
        IntStream.range(0, MEMBER_SHEET_HEADER.length).forEach(i -> {
            Cell cell = row.createCell(i);
            cell.setCellValue(MEMBER_SHEET_HEADER[i]);
        });
        return sheet;
    }

    public static byte[] createByteArray(Workbook workbook) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
}
