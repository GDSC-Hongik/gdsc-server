package com.gdschongik.gdsc.global.util;

import static com.gdschongik.gdsc.global.common.constant.WorkbookConstant.*;

import com.gdschongik.gdsc.domain.member.dao.MemberRepository;
import com.gdschongik.gdsc.domain.member.domain.Department;
import com.gdschongik.gdsc.domain.member.domain.MemberRole;
import jakarta.annotation.Nullable;
import java.util.Optional;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelUtil {

    public Workbook createMemberWorkbook() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        createSheet(workbook, ALL_MEMBER_SHEET_NAME, null);
        createSheet(workbook, GRANTED_MEMBER_SHEET_NAME, MemberRole.USER);
        return workbook;
    }

    private void createSheet(Workbook workbook, String sheetName, @Nullable MemberRole role) {
        Sheet sheet = setUpSheet(workbook, sheetName);

        memberRepository.findAllByRole(role).forEach(member -> {
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

    private Sheet setUpSheet(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.createSheet(sheetName);
        Row row = sheet.createRow(0);
        IntStream.range(0, MEMBER_SHEET_HEADER.length)
                .forEach(i -> row.createCell(i).setCellValue(MEMBER_SHEET_HEADER[i]));
        return sheet;
    }
}
