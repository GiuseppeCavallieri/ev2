package com.example.msrate.repositorie;

import com.example.msrate.entitie.Rates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RateRepository extends JpaRepository<Rates, String> {

    @Query("SELECT r.code FROM Rates r")
    List<String> findAllRateCodes();
}