package com.example.msreservation.entitie;

import javax.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "Reservations")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Reservations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private Long clientId;

    @ElementCollection
    @CollectionTable(name = "Reservation_Companions", joinColumns = @JoinColumn(name = "reservation_id"))
    @Column(name = "companion_id")
    private List<Long> companionsId; // ids de los acompañantes
    private String rateCode; // codigo de la tarifa
    private LocalTime hourChoosen;
    private LocalDate dateChoosen;
}
