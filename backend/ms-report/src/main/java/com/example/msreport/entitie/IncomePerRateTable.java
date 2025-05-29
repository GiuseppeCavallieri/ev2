package com.example.msreport.entitie;

import lombok.*;

import com.example.msreport.Dto.MonthlyIncomePerRate;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class IncomePerRateTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<MonthlyIncomePerRate> monthlyIncomeList;

    @ElementCollection
    private Map<String, Double> totalIncomePerRate;

    @ElementCollection
    private Map<String, Double> totalIncomePerMonth;

    private Double totalIncome;
}
