package com.example.msspecialrate.repositorie;

import com.example.msspecialrate.entitie.Specialrates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpecialRatesRepository extends JpaRepository<Specialrates, Long> {

    @Query("SELECT s FROM Specialrates s WHERE s.month = :month AND s.day = :day")
    Specialrates findByMonthAndDay(@Param("month") int month, @Param("day") int day);
}
