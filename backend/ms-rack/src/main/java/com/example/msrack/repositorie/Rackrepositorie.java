package com.example.msrack.repositorie;

import com.example.msrack.entitie.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface Rackrepositorie extends JpaRepository<Rack, Long>{

    @Query("SELECT r.hour FROM Rack r WHERE r.date = ?1")
    List<LocalTime> findByDate(LocalDate date);

    @Query("SELECT r FROM Rack r WHERE r.date = ?1 AND r.hour = ?2")
    Rack findByDateAndHour(LocalDate date, LocalTime hour);
}
