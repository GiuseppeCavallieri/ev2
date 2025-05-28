package com.example.mskart.entitie;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "Karts")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Kart {

    @Id
    private String name;
    private String mantentionDay; // Uno de los días de la semana ej: Lunes, Martes, etc
}
