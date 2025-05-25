package com.example.msdiscountfreq.repositorie;

import com.example.msdiscountfreq.entitie.Discountfreq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Discountfreqrepository extends JpaRepository<Discountfreq, String> {

    @Query("SELECT d FROM Discountfreq d WHERE :numVisits BETWEEN d.limInf AND d.limSup")
    Discountfreq findByNumVisits(@Param("numVisits") int numVisits);
}
