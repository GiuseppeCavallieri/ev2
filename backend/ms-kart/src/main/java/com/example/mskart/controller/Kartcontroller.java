package com.example.mskart.controller;

import com.example.mskart.entitie.Kart;
import com.example.mskart.service.Kartservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/kart")
@CrossOrigin("*")
public class Kartcontroller {

    @Autowired
    private Kartservice kartService;


    @PostMapping("/saveKart")
    public ResponseEntity<String> saveKart(@RequestBody Kart kart) {
        try {
            kartService.saveKart(kart);
            return ResponseEntity.ok("Kart guardado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Kart>> getAllKarts() {
        try {
            List<Kart> karts = kartService.getAllKarts();
            return ResponseEntity.ok(karts);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/countAvailableKarts/{mantentionDay}")
    public ResponseEntity<Long> countAvailableKarts(@PathVariable String mantentionDay) {
        try {
            long disponibles = kartService.countKartsDisponibles(mantentionDay);
            return ResponseEntity.ok(disponibles);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/getAvailableKarts/{mantentionDay}")
    public ResponseEntity<List<Kart>> getAvailableKarts(@PathVariable String mantentionDay) {
        try {
            List<Kart> disponibles = kartService.getKartsDisponibles(mantentionDay);
            return ResponseEntity.ok(disponibles);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
