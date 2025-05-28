package com.example.msreservation.service;

import com.example.msreservation.repositorie.Reservationrepositorie;
import com.example.msreservation.entitie.Reservations;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class Reservationservice {

    @Autowired
    private Reservationrepositorie reservationrepositorie;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JavaMailSender mailSender;

    public void sendReservationPdfEmail(String toEmail, ByteArrayOutputStream pdf, String subject) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(toEmail);
        helper.setSubject(subject);
        helper.setText("Adjunto encontrarás el comprobante de tu reserva.");

        ByteArrayDataSource dataSource = new ByteArrayDataSource(pdf.toByteArray(), "application/pdf");
        helper.addAttachment("comprobante_reserva.pdf", dataSource);

        mailSender.send(message);
    }

    public boolean saveReservation(Reservations reservation){
        Reservations savedReservation = reservationrepositorie.save(reservation);
        try {
            ByteArrayOutputStream pdfStream = generateTicketPdf(savedReservation, savedReservation.getId());
            String toEmail = restTemplate.getForObject("http://ms-user/user/email/" + savedReservation.getClientId(), String.class);
            sendReservationPdfEmail(toEmail, pdfStream, "Comprobante de reserva");
            System.out.println("PDF generado y correo enviado correctamente.");

        } catch (Exception e) {
            System.err.println("Error al generar el PDF o enviar el correo:");
            e.printStackTrace();
            return false;
        }
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

    public ByteArrayOutputStream generateTicketPdf(Reservations reservation, Long reservationId) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);

        String nameClient = restTemplate.getForObject("http://ms-user/user/name/" + reservation.getClientId(), String.class);
        String rateDesc = restTemplate.getForObject("http://ms-rate/rate/getDesc/" + reservation.getRateCode(), String.class);

        document.open();
        document.add(new Paragraph("Comprobante de Reserva"));
        document.add(new Paragraph("ID de la reserva: " + reservationId));
        document.add(new Paragraph("Fecha elegida: " + reservation.getDateChoosen()));
        document.add(new Paragraph("Descripcion de la tarifa: " + rateDesc));
        document.add(new Paragraph("Cantidad de personas: " + (reservation.getCompanionsId().size() + 1)));
        document.add(new Paragraph("Reservado por: " + nameClient));
        document.add(new Paragraph(" ")); // espacio

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.addCell("Nombre");
        table.addCell("Tarifa base");
        table.addCell("Descuento grupo");
        table.addCell("Descuento especial");
        table.addCell("Subtotal");
        table.addCell("Total c/IVA");
        
        List<Long> allIds = new ArrayList<>(reservation.getCompanionsId());
        allIds.add(reservation.getClientId()); // incluye al cliente que reservó

        int groupSize = reservation.getCompanionsId().size() + 1;
        Long discountsAvailableLong = (groupSize <= 2) ? 0L : (groupSize <= 5) ? 1L : 2L;
        for (Long id : allIds) {

            String name = restTemplate.getForObject("http://ms-user/user/name/" + id, String.class);
            Long baseRate = restTemplate.getForObject("http://ms-rate/rate/getPrice/" + reservation.getRateCode(), Long.class);

            double discount; // Variable para almacenar el descuento
            try {
                discount = restTemplate.getForObject(
                        "http://ms-specialrate/specialrate/discount/" + reservation.getDateChoosen(),
                        Double.class
                );
            } catch (Exception e) {
                discount = 0.0; // Valor por defecto si hay error (400, 500, timeout, etc.)
            }

            if (discount != 0.0) {
                baseRate = Math.round(baseRate * discount);
            }

            double groupDiscount = restTemplate.getForObject("http://ms-discountnum/discountnum/getDiscount/" + groupSize, Double.class);

            double specialDiscount = greaterDiscountOffAClient(id, reservation.getDateChoosen(), discountsAvailableLong);
            // se verifica si se aplico descuento por cumpleañero
            if (restTemplate.getForObject("http://ms-user/user/birthday/" + id + "/" + reservation.getDateChoosen(), Boolean.class)) {
                discountsAvailableLong--;
            }

            double discountToApply = Math.max(groupDiscount, specialDiscount);
            double subtotal = baseRate - (baseRate * discountToApply);
            double totalIVA = subtotal * 1.19;

            table.addCell(name);
            table.addCell(String.valueOf(baseRate));
            table.addCell(groupDiscount + "");
            table.addCell(specialDiscount + "");
            table.addCell(String.format("%.2f", subtotal));
            table.addCell(String.format("%.2f", totalIVA));
        }

        document.add(table);
        document.close();

        return out;
    }

    // retorna una lista de horas, segun las reservas de un dia
    public List<LocalTime> findHoursReservedByDate(LocalDate fecha) {
        List<Reservations> reservations = reservationrepositorie.findReservationsByDate(fecha);
        List<LocalTime> hoursReserved = new ArrayList<>();

        for (Reservations reservation : reservations) {
            hoursReserved.add(reservation.getHourChoosen());
        }

        return hoursReserved;
    }

    public List<Long> findReservationIdsByRateCodeAndDateRange(LocalDate fechaInicio, LocalDate fechaFin, String rateCode) {
        return reservationrepositorie.findReservationIdsByRateCodeAndDateRange(fechaInicio, fechaFin, rateCode);
    }

    public List<Long> findReservationIdsByDateRange(LocalDate fechaInicio, LocalDate fechaFin) {
        return reservationrepositorie.findReservationIdsByDateRange(fechaInicio, fechaFin);
    }

    public double getTotalCostReservationById(Long reservationId) {
        Reservations reservation = reservationrepositorie.findById(reservationId).orElseThrow(() -> new RuntimeException("Reserva no encontrada"));
        double totalCost = 0.0;
        List<Long> allIds = new ArrayList<>(reservation.getCompanionsId());
        allIds.add(reservation.getClientId()); // incluye al cliente que reservó

        int groupSize = reservation.getCompanionsId().size() + 1;
        Long discountsAvailableLong = (groupSize <= 2) ? 0L : (groupSize <= 5) ? 1L : 2L;

        for (Long id : allIds){
            Long baseRate = restTemplate.getForObject("http://ms-rate/rate/getPrice/" + reservation.getRateCode(), Long.class);

            // Descuento al precio base por fecha especial
            double discount;
            try {
                discount = restTemplate.getForObject(
                        "http://ms-specialrate/specialrate/discount/" + reservation.getDateChoosen(),
                        Double.class
                );
            } catch (Exception e) {
                discount = 0.0; // Valor por defecto si hay error (400, 500, timeout, etc.)
            }
            if (discount != 0.0) {
                baseRate = Math.round(baseRate * discount);
            }

            // Descuento por grupo
            double groupDiscount = restTemplate.getForObject("http://ms-discountnum/discountnum/getDiscount/" + groupSize, Double.class);

            double specialDiscount = greaterDiscountOffAClient(id, reservation.getDateChoosen(), discountsAvailableLong);
            // se verifica si se aplico descuento por cumpleañero
            if (restTemplate.getForObject("http://ms-user/user/birthday/" + id + "/" + reservation.getDateChoosen(), Boolean.class)) {
                discountsAvailableLong--;
            }

            double discountToApply = Math.max(groupDiscount, specialDiscount);
            double subtotal = baseRate - (baseRate * discountToApply);
            double totalIVA = subtotal * 1.19;
            totalCost += totalIVA;
        }
        return totalCost;
    }

    public Reservations findReservationById(Long id) {
        return reservationrepositorie.findReservationById(id);
    }
}

/*

 */
