package com.example.msdiscountnum.controller;

import java.util.List;
import com.example.msdiscountnum.entitie.Discountnum;
import com.example.msdiscountnum.service.Discountnumservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/discountnum")
@CrossOrigin("*")
public class Discountnumcontroller {

    @Autowired
    private Discountnumservice discountnumservice;

    @PostMapping("/save")
    public ResponseEntity<String> saveDiscount(@RequestBody Discountnum discountnum) {
        try {
            discountnumservice.save(discountnum);
            return ResponseEntity.ok("Descuento guardado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getDiscount/{numPersons}")
    public ResponseEntity<Double> getDiscountByNumPersons(@PathVariable int numPersons) {
        try {
            Double discount = discountnumservice.getDiscountByNumPersons(numPersons);
            return ResponseEntity.ok(discount);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getAllDiscounts")
    public ResponseEntity<List<Discountnum>> getAllDiscounts() {
        try {
            List<Discountnum> discounts = discountnumservice.getAllDiscounts();
            return ResponseEntity.ok(discounts);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/delete/{code}")
    public ResponseEntity<String> deleteDiscount(@PathVariable String code) {
        try {
            discountnumservice.deleteDiscount(code);
            return ResponseEntity.ok("Descuento eliminado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getDiscountByCode/{code}")
    public ResponseEntity<Double> getDiscountByCode(@PathVariable String code) {
        try {
            Double discountnum = discountnumservice.getDiscountByCode(code);
            return ResponseEntity.ok(discountnum);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getCodeByNumPersons/{numPersons}")
    public ResponseEntity<String> getCodeByNumPersons(@PathVariable int numPersons) {
        try {
            String code = discountnumservice.getCodeByNumPersons(numPersons);
            return ResponseEntity.ok(code);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllCodes")
    public ResponseEntity<List<String>> getAllCodes() {
        try {
            List<String> codes = discountnumservice.getAllCodes();
            return ResponseEntity.ok(codes);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}