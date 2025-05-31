package com.example.mskart.service;

import com.example.mskart.entitie.Kart;
import com.example.mskart.repositorie.Kartrepositorie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Kartservice {

    @Autowired
    private Kartrepositorie kartrepositorie;

    public boolean saveKart(Kart kart) {
        kartrepositorie.save(kart);
        return true;
    }

    public List<Kart> getAllKarts(){ return kartrepositorie.findAll(); }

    public long countKartsDisponibles(String mantentionDay) {
        long enMantencion = kartrepositorie.countByMantentionDay(mantentionDay);
        long total = kartrepositorie.count();
        return total - enMantencion;
    }

    public List<Kart> getKartsDisponibles(String mantentionDay) {
        return kartrepositorie.findAll().stream()
                .filter(kart -> !kart.getMantentionDay().equalsIgnoreCase(mantentionDay))
                .collect(Collectors.toList());
    }

    public boolean deleteKart(String id) {
        try {
            kartrepositorie.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el kart: " + e.getMessage());
        }
    }

}
