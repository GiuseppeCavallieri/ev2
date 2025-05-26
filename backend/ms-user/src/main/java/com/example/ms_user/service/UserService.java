package com.example.ms_user.service;

import com.example.ms_user.entitie.Users;
import com.example.ms_user.repositorie.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean saveUser(Users user) {
        if(userRepository.existsByName(user.getName())) {
            throw new RuntimeException("El nombre de usuario ya existe.");
        }
        userRepository.save(user);
        return true;
    }

    public boolean login(String name, String pass) {
        boolean isValid = userRepository.existsByNameAndPass(name, pass);
        if (!isValid) {
            throw new RuntimeException("Nombre o contraseña incorrectos.");
        }
        return true;
    }

    public boolean isSuperuser(String name) {
        boolean isSuperuser = userRepository.existsByNameAndSuperuser(name, true);
        if (!isSuperuser) {
            throw new RuntimeException("No tienes permisos de superusuario.");
        }
        return true;
    }

    public Users findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado."));
    }

    public LocalDate findBirthday(Long id) { return userRepository.getBirthdayById(id); }

    public boolean birthdayClient(Long clientId, LocalDate date) {
        Users client = findUserById(clientId); // Encuentra al cliente
        LocalDate birthday = findBirthday(client.getId()); // Compara la fecha de cumpleaños con la fecha actual
        return birthday.getMonth() == date.getMonth() && birthday.getDayOfMonth() == date.getDayOfMonth();
    }

    public String getNameById(Long id) {
        Users user = findUserById(id);
        return user.getName();
    }

    public String getEmailById(Long id) {
        Users user = findUserById(id);
        return user.getEmail();
    }
}


