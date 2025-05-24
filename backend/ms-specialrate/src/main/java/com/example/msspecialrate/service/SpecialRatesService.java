package com.example.msspecialrate.service;

import java.util.List;
import com.example.msspecialrate.entitie.Specialrates;
import com.example.msspecialrate.repositorie.SpecialRatesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SpecialRatesService {

    @Autowired
    private SpecialRatesRepository specialRatesRepository;

    public Specialrates saveSpecialRate(Specialrates specialRate) {
        return specialRatesRepository.save(specialRate);
    }

    public void deleteSpecialRate(Long id) {
        specialRatesRepository.deleteById(id);
    }

    public Double getDiscountByMonthAndDay(int month, int day) {
        Specialrates specialRate = specialRatesRepository.findByMonthAndDay(month, day);
        if (specialRate != null) {
            return specialRate.getDiscount();
        }
        return 0.0; // si no existe tarifa especial, retornar 0
    }

    public List<Specialrates> getAllSpecialRates() {
        return specialRatesRepository.findAll();
    }
}
