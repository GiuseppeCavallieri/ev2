package com.example.msrate.repositorie;

import com.example.msrate.entitie.Rates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateRepository extends JpaRepository<Rates, String> {
}