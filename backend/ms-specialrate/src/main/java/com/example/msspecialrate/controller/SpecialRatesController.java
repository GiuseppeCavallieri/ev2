package com.example.msspecialrate.controller;

import java.time.LocalDate;
import java.util.List;
import com.example.msspecialrate.entitie.Specialrates;
import com.example.msspecialrate.service.SpecialRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/specialrate")
public class SpecialRatesController {

    @Autowired
    private SpecialRatesService specialRatesService;

    @PostMapping("/save")
    public ResponseEntity<String> saveSpecialRate(@RequestBody Specialrates specialRate) {

        try{
            specialRatesService.saveSpecialRate(specialRate);
            return ResponseEntity.ok("Tarifa especial guardada exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteSpecialRate(@PathVariable Long id) {
        try {
            boolean deleted = specialRatesService.deleteSpecialRate(id);
            if (deleted) {
                return ResponseEntity.ok("Tarifa eliminada exitosamente.");
            } else {
                return ResponseEntity.badRequest().body("Tarifa no encontrada.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/discount/{date}")
    public ResponseEntity<Double> getDiscountByMonthAndDay(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            Double discount = specialRatesService.getDiscountByMonthAndDay(date);
            return ResponseEntity.ok(discount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Specialrates>> getAllSpecialRates() {
        try {
            List<Specialrates> specialRates = specialRatesService.getAllSpecialRates();
            return ResponseEntity.ok(specialRates);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
