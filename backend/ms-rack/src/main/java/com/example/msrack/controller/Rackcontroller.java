package com.example.msrack.controller;

import com.example.msrack.service.Rackservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.example.msrack.service.Rackservice;

import java.time.LocalDate;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class Rackcontroller {

    @Autowired
    private Rackservice rackservice;

    @GetMapping("/saveRacks/{date}")
    public String saveRacks(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            rackservice.saveRacks(date);
            return "Racks guardados exitosamente.";
        } catch (RuntimeException e) {
            return "Error al guardar racks: " + e.getMessage();
        }
    }

    @GetMapping("/findByDate/{date}")
    public String findByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            return rackservice.findByDate(date).toString();
        } catch (RuntimeException e) {
            return "Error al encontrar racks por fecha: " + e.getMessage();
        }
    }
}
