package com.example.msreservation.repositorie;

import com.example.msreservation.entitie.Reservations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface Reservationrepositorie extends JpaRepository<Reservations, Long> {

    @Query("SELECT r.id FROM Reservations r " +
            "WHERE r.dateChoosen BETWEEN :fechaInicio AND :fechaFin " +
            "AND r.rateCode = :rateCode")
    List<Long> findReservationIdsByRateCodeAndDateRange(@Param("fechaInicio") LocalDate fechaInicio,
                                                        @Param("fechaFin") LocalDate fechaFin,
                                                        @Param("rateCode") String rateCode);

    @Query("SELECT r.id FROM Reservations r " +
            "WHERE r.dateChoosen BETWEEN :fechaInicio AND :fechaFin ")
    List<Long> findReservationIdsByDateRange(@Param("fechaInicio") LocalDate fechaInicio,
                                             @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT r FROM Reservations r WHERE r.dateChoosen = :fecha")
    List<Reservations> findReservationsByDate(@Param("fecha") LocalDate fecha);


    // find reservation by id
    @Query("SELECT r FROM Reservations r WHERE r.id = :id")
    Reservations findReservationById(@Param("id") Long id);
}


