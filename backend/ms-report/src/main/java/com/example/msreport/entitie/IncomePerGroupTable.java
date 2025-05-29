package com.example.msreport.entitie;

import lombok.*;

import com.example.msreport.Dto.MonthlyIncomePerGroup;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class IncomePerGroupTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    private List<MonthlyIncomePerGroup> monthlyIncomeList;

    @ElementCollection
    private Map<String, Double> totalIncomePerGroup;

    @ElementCollection
    private Map<String, Double> totalIncomePerMonth;

    private Double totalIncome;
}
