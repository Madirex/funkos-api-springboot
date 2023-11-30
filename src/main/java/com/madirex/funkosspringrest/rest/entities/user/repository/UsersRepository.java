package com.madirex.funkosspringrest.rest.entities.user.repository;

import com.madirex.funkosspringrest.rest.entities.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interfaz UsersRepository
 */
@Repository
public interface UsersRepository extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User> {

    /**
     * Método que busca un usuario por nombre de usuario
     *
     * @param username Nombre de usuario
     * @return Usuario
     */
    Optional<User> findByUsername(String username);

    /**
     * Método que busca un usuario por email
     *
     * @param email Email
     * @return Usuario
     */
    Optional<User> findByEmailEqualsIgnoreCase(String email);

    /**
     * Método que busca un usuario por nombre de usuario
     *
     * @param username Nombre de usuario
     * @return Usuario
     */
    Optional<User> findByUsernameEqualsIgnoreCase(String username);

    /**
     * Método que busca un usuario por nombre de usuario o email
     *
     * @param username Nombre de usuario
     * @param email    Email
     * @return Usuario
     */
    Optional<User> findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(String username, String email);

    /**
     * Método que busca todos los usuarios cuyo nombre de usuario contenga el parámetro username
     *
     * @param username Nombre de usuario
     * @return Lista de usuarios
     */
    List<User> findAllByUsernameContainingIgnoreCase(String username);

    /**
     * Método que actualiza el campo isDeleted a true
     * Borrado lógico
     *
     * @param id ID del usuario
     */
    @Modifying
    @Query("UPDATE User p SET p.isDeleted = true WHERE p.id = :id")
    void updateIsDeletedToTrueById(UUID id);
}