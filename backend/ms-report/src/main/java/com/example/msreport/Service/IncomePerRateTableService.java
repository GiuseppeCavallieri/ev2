package com.example.msreport.Service;

import com.example.msreport.entitie.IncomePerRateTable;
import com.example.msreport.Repositorie.IncomePerRateTablerepositorie;
import com.example.msreport.Dto.Reservation;
import com.example.msreport.Dto.Rate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class IncomePerRateTableService {

    @Autowired
    private IncomePerRateTablerepositorie incomePerRateTablerepositorie;

    @Autowired
    RestTemplate restTemplate;

    public IncomePerRateTable getIncomePerRateTable(LocalDate startDate, LocalDate endDate){

        Rate[] ratesArray = restTemplate.getForObject("http://ms-rate/rate/getAll", Rate[].class);
        List<Rate> rates = Arrays.asList(ratesArray);
        List<String> rateCodes = rates.stream().map(Rate::getCode).collect(Collectors.toList()); // Extracting rate codes from the Rate objects

        Map <String, Map<String, Double>> cellIncome = new java.util.HashMap<>(); // {RateCode -> {Month -> Income}}
        for (String rateCode : rateCodes){

            Long[] idsArray = restTemplate.getForObject("http://ms-reservation/reservation/findReservationIdsByRateCodeAndDateRange/" + startDate + "/" + endDate + "/" + rateCode, Long[].class);
            List<Long> reservationIds = Arrays.asList(idsArray);
            for (Long reservationId : reservationIds) {

                Reservation reservation = restTemplate.getForObject("http://ms-reservation/reservation/findReservationById/" + reservationId, Reservation.class);
                LocalDate date = reservation.getDateChoosen();
                String month = date.getMonth().toString();
                Double totalCost = restTemplate.getForObject("http://ms-reservation/reservation/getTotalCostReservation/" + reservationId, Double.class);
                cellIncome.computeIfAbsent(rateCode, k -> new java.util.HashMap<>())
                        .merge(month, totalCost, Double::sum);
            }
        }
        Map<String, Double> totalPerRate = new java.util.HashMap<>();
        Map<String, Double> totalPerMonth = new java.util.HashMap<>();
        double totalIncome = 0.0;
        for (Map.Entry<String, Map<String, Double>> entry : cellIncome.entrySet()) {
            String rateCode = entry.getKey();
            Map<String, Double> monthIncome = entry.getValue();

            double totalRateIncome = monthIncome.values().stream().mapToDouble(Double::doubleValue).sum();
            totalPerRate.put(rateCode, totalRateIncome);
            totalIncome += totalRateIncome;

            for (Map.Entry<String, Double> monthEntry : monthIncome.entrySet()) {
                String month = monthEntry.getKey();
                double income = monthEntry.getValue();
                totalPerMonth.merge(month, income, Double::sum);
            }
        }

        IncomePerRateTable incomePerRateTable = new IncomePerRateTable();
        incomePerRateTable.setMonthlyIncomeList(new java.util.ArrayList<>());
        for (String rateCode : cellIncome.keySet()) {
            for (Map.Entry<String, Double> monthEntry : cellIncome.get(rateCode).entrySet()) {
                incomePerRateTable.getMonthlyIncomeList().add(new com.example.msreport.Dto.MonthlyIncomePerRate(monthEntry.getKey(), rateCode, monthEntry.getValue()));
            }
        }
        incomePerRateTable.setTotalIncomePerRate(totalPerRate);
        incomePerRateTable.setTotalIncomePerMonth(totalPerMonth);
        incomePerRateTable.setTotalIncome(totalIncome);
        return incomePerRateTable;
    }
}