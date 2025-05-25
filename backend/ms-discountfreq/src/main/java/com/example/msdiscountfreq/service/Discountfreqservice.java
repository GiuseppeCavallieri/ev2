package com.example.msdiscountfreq.service;

import java.util.List;
import com.example.msdiscountfreq.entitie.Discountfreq;
import com.example.msdiscountfreq.repositorie.Discountfreqrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Discountfreqservice {

    @Autowired
    Discountfreqrepository discountfreqrepository;

    public boolean save(Discountfreq discountfreq) {
        try {
            discountfreqrepository.save(discountfreq);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Double getDiscountByNumVisits(int numVisits) {
        Discountfreq discountfreq = discountfreqrepository.findByNumVisits(numVisits);
        if (discountfreq != null) {
            return discountfreq.getDiscount();
        } else {
            throw new RuntimeException("Descuento no encontrado para el número de visitas: " + numVisits);
        }
    }

    public List<Discountfreq> getAllDiscounts() {
        return discountfreqrepository.findAll();
    }

    public boolean deleteDiscount(String code) {
        try {
            discountfreqrepository.deleteById(code);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el descuento: " + e.getMessage());
        }
    }
}
