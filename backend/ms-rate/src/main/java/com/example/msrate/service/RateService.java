package com.example.msrate.service;

import java.util.List;
import com.example.msrate.entitie.Rates;
import com.example.msrate.repositorie.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RateService {

    @Autowired
    private RateRepository rateRepository;

    public boolean saveRate(Rates rate) {
        rateRepository.save(rate);
        return true;
    }

    public Long getPriceByCode(String code) {
        Rates rate = rateRepository.findById(code).orElse(null);
        if (rate != null) {
            return rate.getPrice();
        } else {
            throw new RuntimeException("Tarifa no encontrada.");
        }
    }

    public List<Rates> getAllRates() {
        return rateRepository.findAll();
    }

    public boolean deleteRate(String code) {
        Rates rate = rateRepository.findById(code).orElse(null);
        if (rate != null) {
            rateRepository.delete(rate);
            return true;
        } else {
            throw new RuntimeException("Tarifa no encontrada.");
        }
    }
}
