package com.example.msdiscountnum.service;

import java.util.List;
import com.example.msdiscountnum.entitie.Discountnum;
import com.example.msdiscountnum.repositorie.Discountnumrepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Discountnumservice {

    @Autowired Discountnumrepository discountnumrepository;

    public boolean save(Discountnum discountnum) {
        try {
            discountnumrepository.save(discountnum);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Double getDiscountByNumPersons(int numPersons) {
        Discountnum discountnum = discountnumrepository.findByNumPersons(numPersons);
        if (discountnum != null) {
            return discountnum.getDiscount();
        } else {
            throw new RuntimeException("Descuento no encontrado para el número de personas: " + numPersons);
        }
    }

    public List<Discountnum> getAllDiscounts() {
        return discountnumrepository.findAll();
    }

    public boolean deleteDiscount(String code) {
        try {
            discountnumrepository.deleteById(code);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el descuento: " + e.getMessage());
        }
    }
}