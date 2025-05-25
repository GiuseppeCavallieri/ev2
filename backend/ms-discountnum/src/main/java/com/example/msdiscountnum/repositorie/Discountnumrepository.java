package com.example.msdiscountnum.repositorie;

import com.example.msdiscountnum.entitie.Discountnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface Discountnumrepository extends JpaRepository<Discountnum, String> {

    @Query("SELECT d FROM Discountnum d WHERE :numPersons BETWEEN d.limInf AND d.limSup")
    Discountnum findByNumPersons(@Param("numPersons") int numPersons);
}
