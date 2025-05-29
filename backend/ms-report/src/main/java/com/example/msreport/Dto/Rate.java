package com.example.msreport.Dto;
import lombok.Getter;

@Getter
public class Rate {
    private String code; // codigo de la tarifa
    private Long price; // precio de la tarifa
    private Long duration; // duracion del servicio contratado correspondiente a la tarifa
    private String description; // descripcion de la tarifa
}
