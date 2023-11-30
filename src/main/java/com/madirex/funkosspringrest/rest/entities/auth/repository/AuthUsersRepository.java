package com.madirex.funkosspringrest.rest.entities.auth.repository;

import com.madirex.funkosspringrest.rest.entities.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio de usuarios
 */
@Repository
public interface AuthUsersRepository extends JpaRepository<User, UUID> {

    /**
     * Encuentra un usuario por su nombre de usuario
     *
     * @param username Nombre de usuario
     * @return Usuario
     */
    Optional<User> findByUsername(String username);
}