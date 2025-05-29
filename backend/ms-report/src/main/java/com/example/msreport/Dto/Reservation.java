package com.example.msreport.Dto;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class Reservation {
    private Long id;
    private Long clientId;
    private List<Long> companionsId; // ids de los acompañantes
    private String rateCode; // codigo de la tarifa
    private LocalTime hourChoosen;
    private LocalDate dateChoosen;
}
