package com.example.ms_user.controller;

import com.example.ms_user.entitie.Users;
import com.example.ms_user.service.UserService;
import com.example.ms_user.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Users user) {
        try {
            userService.saveUser(user);
            return ResponseEntity.ok("Usuario registrado exitosamente.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        try {
            userService.login(loginRequest.name, loginRequest.pass);
            return ResponseEntity.ok("Inicio de sesión exitoso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/superuser/{name}")
    public ResponseEntity<String> checkSuperuser(@PathVariable String name) {
        try {
            userService.isSuperuser(name);
            return ResponseEntity.ok("Usuario tiene permisos de superusuario.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        try {
            Users user = userService.findUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{id}/birthday")
    public ResponseEntity<String> getBirthdayById(@PathVariable Long id) {
        try {
            String birthday = userService.findBirthday(id).toString();
            return ResponseEntity.ok(birthday);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/birthday/{clientId}/{date}")
    public ResponseEntity<Boolean> birthdayClient(@PathVariable Long clientId, @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            boolean isBirthday = userService.birthdayClient(clientId, date);
            return ResponseEntity.ok(isBirthday);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/name/{id}")
    public ResponseEntity<String> getNameById(@PathVariable Long id) {
        try {
            String name = userService.getNameById(id);
            return ResponseEntity.ok(name);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/email/{id}")
    public ResponseEntity<String> getEmailById(@PathVariable Long id) {
        try {
            String email = userService.getEmailById(id);
            return ResponseEntity.ok(email);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

