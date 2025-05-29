package com.example.msreport.Dto;

import lombok.*;

import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Embeddable
public class MonthlyIncomePerGroup {
    private String month;
    private String groupCode; // d1, d3, d6, d11
    private Double cellValue; // Valor de la celda
}
