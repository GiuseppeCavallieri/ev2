package com.example.msdiscountnum.entitie;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "Discountnum")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Discountnum {

    @Id
    private String code; // codigo del descuento

    private int limInf; // limite inferior de personas
    private int limSup; // limite superior de personas
    private Double discount; // descuento en porcentaje
    private String description; // descripcion del descuento
}
