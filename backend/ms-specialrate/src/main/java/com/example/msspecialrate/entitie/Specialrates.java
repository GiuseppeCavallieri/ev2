package com.example.msspecialrate.entitie;

import javax.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "Specialrates")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Specialrates {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    // mes y dia de la tarifa especial
    private int month;
    private int day;

    private Double discount; // descuento acorde al dia festivo
    private String description; // descripcion de la tarifa especial
}
