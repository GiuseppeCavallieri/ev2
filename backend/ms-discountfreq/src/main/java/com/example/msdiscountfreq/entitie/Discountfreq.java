package com.example.msdiscountfreq.entitie;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "Discountfreq")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Discountfreq {

    @Id
    private String code; // codigo del descuento

    private int limInf; // limite inferior de visitas
    private int limSup; // limite superior de visitas
    private Double discount; // descuento en porcentaje
    private String description; // descripcion del descuento
}
