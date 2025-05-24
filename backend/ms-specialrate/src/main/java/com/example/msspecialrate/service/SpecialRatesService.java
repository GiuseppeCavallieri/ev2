package com.example.msspecialrate.service;

import java.time.LocalDate;
import java.util.List;
import com.example.msspecialrate.entitie.Specialrates;
import com.example.msspecialrate.repositorie.SpecialRatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecialRatesService {

    @Autowired
    private SpecialRatesRepository specialRatesRepository;

    public boolean saveSpecialRate(Specialrates specialRate) {
        specialRatesRepository.save(specialRate);
        return true;
    }

    public boolean deleteSpecialRate(Long id) {
        Specialrates specialRate = specialRatesRepository.findById(id).orElse(null);
        if (specialRate != null) {
            specialRatesRepository.delete(specialRate);
            return true;
        } else {
            throw new RuntimeException("Tarifa especial no encontrada.");
        }
    }

    public Double getDiscountByMonthAndDay(LocalDate date) {

        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        Specialrates specialRate = specialRatesRepository.findByMonthAndDay(month, day);
        if (specialRate != null) {
            return specialRate.getDiscount();
        }
        else {
            throw new RuntimeException("Tarifa no encontrada.");
        }
    }

    public List<Specialrates> getAllSpecialRates() {
        return specialRatesRepository.findAll();
    }
}
