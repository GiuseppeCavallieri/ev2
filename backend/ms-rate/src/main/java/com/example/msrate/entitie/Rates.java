package com.example.msrate.entitie;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "Rates")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Rates {

    @Id
    private String code; // codigo de la tarifa
    private Long price; // precio de la tarifa
    private Long duration; // duracion del servicio contratado correspondiente a la tarifa
    private String description; // descripcion de la tarifa
}
