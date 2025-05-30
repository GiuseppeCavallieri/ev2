package com.example.msreport.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.example.msreport.Service.IncomePerGroupTableService;
import com.example.msreport.entitie.IncomePerGroupTable;

import java.time.LocalDate;

@RestController
@RequestMapping("/report/iGroup")
public class IncomePerGroupController {

    @Autowired
    private IncomePerGroupTableService incomePerGroupTableService;

    @GetMapping("/getIncomePerGroupTable/{startDate}/{endDate}")
    public IncomePerGroupTable getIncomePerGroupTable(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return incomePerGroupTableService.getIncomePerGroupTable(startDate, endDate);
    }
}
