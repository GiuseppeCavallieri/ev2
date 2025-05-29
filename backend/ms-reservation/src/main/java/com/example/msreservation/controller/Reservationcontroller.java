package com.example.msreservation.controller;

import com.example.msreservation.entitie.Reservations;
import com.example.msreservation.service.Reservationservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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

    @GetMapping("/findHoursReserved/{date}")
    public ResponseEntity<List<LocalTime>> findHoursReserved(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<LocalTime> hours = reservationservice.findHoursReservedByDate(date);
            return ResponseEntity.ok(hours);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/findReservationIdsByRateCodeAndDateRange/{fechaInicio}/{fechaFin}/{rateCode}")
    public ResponseEntity<List<Long>> findReservationIdsByRateCodeAndDateRange(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @PathVariable String rateCode) {
        try {
            List<Long> reservationIds = reservationservice.findReservationIdsByRateCodeAndDateRange(fechaInicio, fechaFin, rateCode);
            return ResponseEntity.ok(reservationIds);
        } catch (RuntimeException e) {
            return ResponseEntity.ok(null);
        }
    }

    @GetMapping("/findReservationIdsByDateRange/{fechaInicio}/{fechaFin}")
    public ResponseEntity<List<Long>> findReservationIdsByDateRange(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<Long> reservationIds = reservationservice.findReservationIdsByDateRange(fechaInicio, fechaFin);
            return ResponseEntity.ok(reservationIds);
        } catch (RuntimeException e) {
            return ResponseEntity.ok(null);
        }
    }

    @GetMapping("/getTotalCostReservation/{reservationId}")
    public ResponseEntity<Double> getTotalCostReservation(@PathVariable Long reservationId) {
        try {
            Double totalCost = reservationservice.getTotalCostReservationById(reservationId);
            return ResponseEntity.ok(totalCost);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/findReservationById/{id}")
    public ResponseEntity<Reservations> findReservationById(@PathVariable Long id) {
        try {
            Reservations reservation = reservationservice.findReservationById(id);
            return ResponseEntity.ok(reservation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getReservationsIdsByGroupSize/{reservationsIds}/{groupSize}")
    public ResponseEntity<List<Long>> getReservationsIdsByGroupSize(
            @PathVariable List<Long> reservationsIds,
            @PathVariable String groupSize) {
        try {
            List<Long> filteredIds = reservationservice.getReservationsIdsByGroupSize(reservationsIds, groupSize);
            return ResponseEntity.ok(filteredIds);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}