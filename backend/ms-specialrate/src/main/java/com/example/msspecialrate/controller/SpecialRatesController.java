package com.example.msspecialrate.controller;

import java.util.List;
import com.example.msspecialrate.entitie.Specialrates;
import com.example.msspecialrate.service.SpecialRatesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/specialrate")
@CrossOrigin("*")
public class SpecialRatesController {

    @Autowired
    private SpecialRatesService specialRatesService;

    @PostMapping("/save")
    public ResponseEntity<Specialrates> saveSpecialRate(@RequestBody Specialrates specialRate) {
        Specialrates savedRate = specialRatesService.saveSpecialRate(specialRate);
        return ResponseEntity.ok(savedRate);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSpecialRate(@PathVariable Long id) {
        specialRatesService.deleteSpecialRate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/discount/{month}/{day}")
    public ResponseEntity<Double> getDiscountByMonthAndDay(@PathVariable int month, @PathVariable int day) {
        Double discount = specialRatesService.getDiscountByMonthAndDay(month, day);
        return ResponseEntity.ok(discount);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Specialrates>> getAllSpecialRates() {
        List<Specialrates> specialRates = specialRatesService.getAllSpecialRates();
        return ResponseEntity.ok(specialRates);
    }
}
