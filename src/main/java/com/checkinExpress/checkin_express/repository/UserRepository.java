package com.checkinExpress.checkin_express.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.checkinExpress.checkin_express.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username); // Busca o usuário pelo nome de usuário
}