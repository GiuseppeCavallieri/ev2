package com.example.msrack.service;

import com.example.msrack.entitie.Rack;
import com.example.msrack.repositorie.Rackrepositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class Rackservice {

    @Autowired
    private Rackrepositorie rackrepositorie;

    @Autowired
    RestTemplate restTemplate;

    public boolean saveRack(Rack rack) {
        rackrepositorie.save(rack);
        return true;
    }

    public boolean saveRacks(LocalDate date) {
        List<LocalTime> reservedHours = restTemplate.getForObject("http://ms-reservation/reservation/findHoursReserved/" + date, List.class);
        if (reservedHours != null) {
            for (LocalTime hour : reservedHours) {
                Rack rack = new Rack();
                rack.setDate(date);
                rack.setHour(hour);
                saveRack(rack);
            }
            return true;
        } else {
            throw new RuntimeException("No se encontraron horas reservadas para la fecha: " + date);
        }
    }

    public List<LocalTime> findByDate(LocalDate date) {
        Rack rack = rackrepositorie.findByDate(date);
        if (rack != null) {
            return List.of(rack.getHour());
        } else {
            throw new RuntimeException("No se encontraron racks para la fecha: " + date);
        }
    }
}