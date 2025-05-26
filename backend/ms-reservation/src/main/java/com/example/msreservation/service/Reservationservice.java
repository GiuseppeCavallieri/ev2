package com.example.msreservation.service;

import com.example.msreservation.repositorie.Reservationrepositorie;
import com.example.msreservation.entitie.Reservations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;


@Service
public class Reservationservice {

    @Autowired
    private Reservationrepositorie reservationrepositorie;

    @Autowired
    RestTemplate restTemplate;

    public boolean saveReservation(Reservations reservation){
        Reservations savedReservation = reservationrepositorie.save(reservation);
        /*
        lógica de envio de correo, más adelante
         */
        return true;
    }

    public int countReservationsOfAClientAMonth(Long clientId, LocalDate reservationDate){
        int count = 0;
        for(Reservations reservation : reservationrepositorie.findAll()){
            // verifica que la reserva hecha por el cliente sea en el mes actual (se busca cliente frecuente en mes actual)
            if(reservation.getDateChoosen().getMonth() == reservationDate.getMonth()){
                if(reservation.getClientId().equals(clientId)){
                    count++;
                }

                if(reservation.getCompanionsId().contains(clientId)){
                    count++;
                }
            }
        }
        return count;
    }

    public Double greaterDiscountOffAClient(Long clientId, LocalDate reservationDate, Long bhDiscountAvailable) {
        Double GreaterDiscount = 0.0;
        boolean isBirthday = restTemplate.getForObject("http://ms-user/user/birthday/" + clientId + "/" + reservationDate, Boolean.class);

        // determina si quedan cupos para el descuento de cumpleaños, comprueba si esta de cumpleaños
        if (bhDiscountAvailable > 0 && isBirthday) {
            GreaterDiscount = restTemplate.getForObject("http://ms-discountnum/discountnum/getDiscountByCode/birthday", Double.class);
        }

        int freq = countReservationsOfAClientAMonth(clientId, reservationDate);
        Double frequentDiscount = restTemplate.getForObject("http://ms-discountfreq/discountfreq/getDiscount/" + freq, Double.class);

        if (GreaterDiscount > frequentDiscount) {
            return GreaterDiscount;
        } else {
            return frequentDiscount;
        }
    }
}

/*

 */
