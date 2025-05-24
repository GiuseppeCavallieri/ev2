package com.example.msrate.controller;

import java.util.List;
import com.example.msrate.entitie.Rates;
import com.example.msrate.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rate")
@CrossOrigin("*")
public class RateController {
    @Autowired
    private RateService rateService;

    @PostMapping("/save")
    public ResponseEntity<String> saveRate(@RequestBody Rates rate) {
        try {
            rateService.saveRate(rate);
            return ResponseEntity.ok("Tarifa guardada exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getPrice/{code}")
    public ResponseEntity<Long> getPriceByCode(@PathVariable String code) {
        try {
            Long price = rateService.getPriceByCode(code);
            return ResponseEntity.ok(price);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Rates>> getAllRates() {
        try {
            List<Rates> rates = rateService.getAllRates();
            return ResponseEntity.ok(rates);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<String> deleteRate(@PathVariable String code) {
        try {
            boolean deleted = rateService.deleteRate(code);
            if (deleted) {
                return ResponseEntity.ok("Tarifa eliminada exitosamente.");
            } else {
                return ResponseEntity.badRequest().body("Tarifa no encontrada.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}