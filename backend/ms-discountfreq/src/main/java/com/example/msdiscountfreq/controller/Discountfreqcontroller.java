package com.example.msdiscountfreq.controller;

import java.util.List;
import com.example.msdiscountfreq.entitie.Discountfreq;
import com.example.msdiscountfreq.service.Discountfreqservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discountfreq")
@CrossOrigin("*")
public class Discountfreqcontroller {

    @Autowired
    private Discountfreqservice discountfreqservice;

    @PostMapping("/save")
    public ResponseEntity<String> saveDiscount(@RequestBody Discountfreq discountfreq) {
        try {
            discountfreqservice.save(discountfreq);
            return ResponseEntity.ok("Descuento guardado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getDiscount/{numVisits}")
    public ResponseEntity<Double> getDiscountByNumVisits(@PathVariable int numVisits) {
        try {
            Double discount = discountfreqservice.getDiscountByNumVisits(numVisits);
            return ResponseEntity.ok(discount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getAllDiscounts")
    public ResponseEntity<List<Discountfreq>> getAllDiscounts() {
        try {
            List<Discountfreq> discounts = discountfreqservice.getAllDiscounts();
            return ResponseEntity.ok(discounts);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<String> deleteDiscount(@PathVariable String code) {
        try {
            discountfreqservice.deleteDiscount(code);
            return ResponseEntity.ok("Descuento eliminado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
