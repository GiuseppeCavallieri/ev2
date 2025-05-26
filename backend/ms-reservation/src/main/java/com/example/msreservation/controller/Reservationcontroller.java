package com.example.msreservation.controller;

import com.example.msreservation.entitie.Reservations;
import com.example.msreservation.service.Reservationservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@RestController
@RequestMapping("/reservation")
@CrossOrigin("*")
public class Reservationcontroller {

    @Autowired
    private Reservationservice reservationservice;

    @PostMapping("/save")
    public ResponseEntity<Boolean> saveReservation(@RequestBody Reservations reservation) {
        try {
            Boolean result = reservationservice.saveReservation(reservation);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/count/{clientId}/{reservationDate}")
    public ResponseEntity<Integer> countReservationsOfAClientAMonth(
            @PathVariable Long clientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate) {
        try {
            int count = reservationservice.countReservationsOfAClientAMonth(clientId, reservationDate);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/greaterDiscount/{clientId}/{reservationDate}/{bhDiscountAvailable}")
    public ResponseEntity<Double> greaterDiscountOffAClient(
            @PathVariable Long clientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate,
            @PathVariable Long bhDiscountAvailable) {
        try {
            Double discount = reservationservice.greaterDiscountOffAClient(clientId, reservationDate, bhDiscountAvailable);
            return ResponseEntity.ok(discount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // controlador auxiliar para analizar el funcionamiento de funciones
    @GetMapping("/birthday/{clientId}/{reservationDate}")
    public ResponseEntity<Boolean> birthdayClient(
            @PathVariable long clientId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate reservationDate) {
        try {
            Boolean isBirthday = reservationservice.birthdayClient(clientId, reservationDate);
            return ResponseEntity.ok(isBirthday);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}