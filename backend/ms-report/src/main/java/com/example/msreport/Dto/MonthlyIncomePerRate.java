package com.example.msreport.Dto;

import lombok.*;

import javax.persistence.Embeddable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Embeddable
public class MonthlyIncomePerRate {
    private String month; // Enero, Febrero, etc.
    private String rateCode; // OP10, OP15, OP20, etc.
    private Double cellValue; // Valor de la celda
}
