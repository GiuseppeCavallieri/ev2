package com.example.msrack.repositorie;

import com.example.msrack.entitie.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface Rackrepositorie extends JpaRepository<Rack, Long>{

    // encuentra todos los racks con un date
    @Query("SELECT r.hour FROM Rack r WHERE r.date = ?1")
    Rack findByDate(LocalDate date);
}
