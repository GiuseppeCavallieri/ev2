package com.example.ms_user.entitie;

import javax.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;
    private boolean superuser; // para funcionalidades de admin
    private String name;
    private String pass;
    private String email; // para enviar el comprobante
    private LocalDate birthday; // yyyy-MM-dd
}
