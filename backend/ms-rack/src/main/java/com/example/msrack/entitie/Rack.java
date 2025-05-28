package com.example.msrack.entitie;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Rack")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Rack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    private LocalDate date; // fecha de donde obtendra las reservas
    private LocalTime hour; // hora de la reserva
}
