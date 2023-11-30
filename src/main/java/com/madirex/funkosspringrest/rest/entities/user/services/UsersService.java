package com.madirex.funkosspringrest.rest.entities.user.services;

import com.madirex.funkosspringrest.rest.entities.user.dto.UserInfoResponse;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserRequest;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserResponse;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Interfaz UsersService
 */
public interface UsersService {

    /**
     * Método que busca entre todos los usuarios
     *
     * @param username  Nombre de usuario
     * @param email     Email
     * @param isDeleted Borrado
     * @param pageable  Paginación
     * @return Lista de usuarios
     */
    Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable);

    /**
     * Método que busca un usuario por su ID
     *
     * @param id ID del usuario
     * @return Usuario
     */
    UserInfoResponse findById(String id);

    /**
     * Método que busca un usuario por su nombre de usuario
     *
     * @param userRequest Nombre de usuario
     * @return Usuario
     */
    UserResponse save(UserRequest userRequest);

    /**
     * Método que actualiza un usuario
     *
     * @param id          ID del usuario
     * @param userUpdate Usuario a actualizar
     * @return Usuario
     */
    UserResponse update(String id, UserUpdate userUpdate);

    /**
     * Método que borra un usuario
     *
     * @param id ID del usuario
     */
    void deleteById(String id);
}