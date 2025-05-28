package com.example.mskart.repositorie;

import com.example.mskart.entitie.Kart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface Kartrepositorie extends JpaRepository<Kart, String> {

    @Query("SELECT COUNT(k) FROM Kart k WHERE k.mantentionDay = :mDay")
    long countByMantentionDay(@Param("mDay") String mantentionDay);
}
