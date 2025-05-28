package com.example.msrack.service;

import com.example.msrack.entitie.Rack;
import com.example.msrack.repositorie.Rackrepositorie;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Rackservice {

    @Autowired
    private Rackrepositorie rackrepositorie;

    @Autowired
    RestTemplate restTemplate;

    public boolean saveRack(Rack rack) {
        Rack existingRack = rackrepositorie.findByDateAndHour(rack.getDate(), rack.getHour());

        if (existingRack != null) {
            return true;
        }

        rackrepositorie.save(rack);
        return true;
    }

    public boolean saveRacks(LocalDate date) {

        List<String> reservedHoursRaw = restTemplate.getForObject("http://ms-reservation/reservation/findHoursReserved/" + date, List.class);

        // conversion de cada hora a LocalTime
        List<LocalTime> reservedHours = reservedHoursRaw.stream()
                .map(LocalTime::parse)
                .collect(Collectors.toList());

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
        List<LocalTime> rack = rackrepositorie.findByDate(date);
        if (rack != null) {
            return rack;
        } else {
            throw new RuntimeException("No se encontraron racks para la fecha: " + date);
        }
    }
}