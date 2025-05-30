package com.example.msreport.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.example.msreport.Service.IncomePerRateTableService;
import com.example.msreport.entitie.IncomePerRateTable;

import java.time.LocalDate;

@RestController
@RequestMapping("/report/iRate")
public class IncomePerRateController {

    @Autowired
    private IncomePerRateTableService incomePerRateTableService;

    @GetMapping("/getIncomePerRateTable/{startDate}/{endDate}")
    public IncomePerRateTable getIncomePerRateTable(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return incomePerRateTableService.getIncomePerRateTable(startDate, endDate);
    }
}
