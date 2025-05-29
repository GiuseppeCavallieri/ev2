package com.example.msreport.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import com.example.msreport.Dto.Reservation;
import com.example.msreport.entitie.IncomePerGroupTable;
import com.example.msreport.Dto.MonthlyIncomePerGroup;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IncomePerGroupTableService {

    @Autowired
    RestTemplate restTemplate;

    public IncomePerGroupTable getIncomePerGroupTable(LocalDate startDate, LocalDate endDate) {
        String[] groupSizesArray = restTemplate.getForObject("http://ms-discountnum/discountnum/getAllCodes", String[].class);
        List<String> groupSizes = Arrays.asList(groupSizesArray);
        groupSizes = groupSizes.stream()
                .filter(code -> code.startsWith("d"))
                .collect(Collectors.toList());
        Map<String, Map<String, Double>> cellIncome = new java.util.HashMap<>(); // {GroupSize -> {Month -> Income}}

        for (String groupSize : groupSizes) {
            Long[] reservationIdsArray = restTemplate.getForObject("http://ms-reservation/reservation/findReservationIdsByDateRange/" + startDate + "/" + endDate, Long[].class);
            List<Long> reservationIds = Arrays.asList(reservationIdsArray);

            Long[] filteredIdsArray = restTemplate.getForObject("http://ms-reservation/reservation/getReservationsIdsByGroupSize/" + String.join(",", reservationIds.stream().map(String::valueOf).collect(Collectors.toList())) + "/" + groupSize, Long[].class);
            List<Long> filteredIds = Arrays.asList(filteredIdsArray);

            for (Long reservationId : filteredIds) {
                Reservation reservation = restTemplate.getForObject("http://ms-reservation/reservation/findReservationById/" + reservationId, Reservation.class);
                LocalDate date = reservation.getDateChoosen();
                String month = date.getMonth().toString();
                Double totalCost = restTemplate.getForObject("http://ms-reservation/reservation/getTotalCostReservation/" + reservationId, Double.class);
                cellIncome.computeIfAbsent(groupSize, k -> new java.util.HashMap<>())
                        .merge(month, totalCost, Double::sum);
            }
        }
        Map<String, Double> totalPerGroupSize = new java.util.HashMap<>();
        Map<String, Double> totalPerMonth = new java.util.HashMap<>();
        double totalIncome = 0.0;
        for (Map.Entry<String, Map<String, Double>> entry : cellIncome.entrySet()) {
            String groupSize = entry.getKey();
            Map<String, Double> monthIncome = entry.getValue();

            double totalGroupSizeIncome = monthIncome.values().stream().mapToDouble(Double::doubleValue).sum();
            totalPerGroupSize.put(groupSize, totalGroupSizeIncome);
            totalIncome += totalGroupSizeIncome;

            for (Map.Entry<String, Double> monthEntry : monthIncome.entrySet()) {
                String month = monthEntry.getKey();
                double income = monthEntry.getValue();
                totalPerMonth.merge(month, income, Double::sum);
            }
        }
        IncomePerGroupTable incomePerGroupTable = new IncomePerGroupTable();
        incomePerGroupTable.setMonthlyIncomeList(new java.util.ArrayList<>());
        for (String groupSize : cellIncome.keySet()) {
            for (Map.Entry<String, Double> monthEntry : cellIncome.get(groupSize).entrySet()) {
                incomePerGroupTable.getMonthlyIncomeList().add(new MonthlyIncomePerGroup(monthEntry.getKey(), groupSize,
                        monthEntry.getValue()));
            }
        }
        incomePerGroupTable.setTotalIncomePerGroup(totalPerGroupSize);
        incomePerGroupTable.setTotalIncomePerMonth(totalPerMonth);
        incomePerGroupTable.setTotalIncome(totalIncome);
        return incomePerGroupTable;
    }
}

/*
 public IncomePerGroupTable getIncomePerGroupTable(LocalDate startDate, LocalDate endDate) {
        List<String> groupSizes = List.of("d1", "d3", "d6", "d11");
        Map<String, Map<String, Double>> cellIncome = new java.util.HashMap<>(); // {GroupSize -> {Month -> Income}}

        for (String groupSize : groupSizes) {
            List<Long> reservationIds = reservationRepository.findReservationIdsByDateRange(startDate, endDate);
            List<Long> filteredIds = getReservationIdsByGroupSize(reservationIds, groupSize);
            for (Long reservationId : filteredIds) {
                Reservations reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
                LocalDate date = reservation.getDateChoosen();
                String month = date.getMonth().toString();
                double totalCost = getTotalCostReservationById(reservationId);

                cellIncome.computeIfAbsent(groupSize, k -> new java.util.HashMap<>()).merge(month, totalCost, Double::sum);
            }
        }

        Map<String, Double> totalPerGroupSize = new java.util.HashMap<>();
        Map<String, Double> totalPerMonth = new java.util.HashMap<>();
        double totalIncome = 0.0;
        for (Map.Entry<String, Map<String, Double>> entry : cellIncome.entrySet()) {
            String groupSize = entry.getKey();
            Map<String, Double> monthIncome = entry.getValue();

            double totalGroupSizeIncome = monthIncome.values().stream().mapToDouble(Double::doubleValue).sum();
            totalPerGroupSize.put(groupSize, totalGroupSizeIncome);
            totalIncome += totalGroupSizeIncome;

            for (Map.Entry<String, Double> monthEntry : monthIncome.entrySet()) {
                String month = monthEntry.getKey();
                double income = monthEntry.getValue();
                totalPerMonth.merge(month, income, Double::sum);
            }
        }

        IncomePerGroupTable incomePerGroupTable = new IncomePerGroupTable();
        incomePerGroupTable.setMonthlyIncomeList(new ArrayList<>());
        for (String groupSize : cellIncome.keySet()) {
            for (Map.Entry<String, Double> monthEntry : cellIncome.get(groupSize).entrySet()) {
                incomePerGroupTable.getMonthlyIncomeList().add(new MonthlyIncomePerGroup(monthEntry.getKey(), groupSize,
                        monthEntry.getValue()));
            }
        }

        incomePerGroupTable.setTotalIncomePerGroup(totalPerGroupSize);
        incomePerGroupTable.setTotalIncomePerMonth(totalPerMonth);
        incomePerGroupTable.setTotalIncome(totalIncome);
        return incomePerGroupTable;
    }
}
 */
