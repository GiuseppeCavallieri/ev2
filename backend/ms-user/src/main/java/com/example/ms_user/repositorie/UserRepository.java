package com.example.ms_user.repositorie;

import com.example.ms_user.entitie.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<Users, Long> {
    boolean existsByNameAndPass(String name, String pass);
    boolean existsByNameAndSuperuser(String name, boolean superuser);
    boolean existsByName(String name);
    @Query("SELECT u.birthday FROM Users u WHERE u.id = :id")
    LocalDate getBirthdayById(@Param("id") Long id);
}
