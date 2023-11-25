package com.madirex.funkosspringrest.rest.entities.user.mappers;

import com.madirex.funkosspringrest.rest.entities.user.dto.UserInfoResponse;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserRequest;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserResponse;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserUpdate;
import com.madirex.funkosspringrest.rest.entities.user.models.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Clase UsersMapper
 */
@Component
public class UsersMapper {
    /**
     * Método que convierte un objeto UserRequest en un objeto User
     *
     * @param request UserRequest
     * @return User
     */
    public User toUser(UserRequest request) {
        return User.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .isDeleted(request.getIsDeleted())
                .build();
    }

    /**
     * Método que convierte un objeto UserRequest y un UUID en un objeto User
     *
     * @param request UserRequest
     * @param id      ID del usuario
     * @return User
     */
    public User toUser(UserRequest request, UUID id) {
        return User.builder()
                .id(id)
                .name(request.getName())
                .surname(request.getSurname())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(request.getPassword())
                .roles(request.getRoles())
                .isDeleted(request.getIsDeleted())
                .build();
    }

    /**
     * Método que convierte un objeto User en un objeto UserResponse
     *
     * @param user usuario
     * @return UserResponse
     */
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isDeleted(user.getIsDeleted())
                .build();
    }

    /**
     * Método que convierte un objeto User y una lista de pedidos en un objeto UserInfoResponse
     *
     * @param user   usuario
     * @param orders pedidos
     * @return UserInfoResponse
     */
    public UserInfoResponse toUserInfoResponse(User user, List<String> orders) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(user.getRoles())
                .isDeleted(user.getIsDeleted())
                .orders(orders)
                .build();
    }

    /**
     * Método que convierte un objeto UserUpdate en un objeto User
     *
     * @param userUpdate UserUpdate
     * @param uuid       UUID
     * @param username   Nombre de usuario
     * @return User
     */
    public User toUser(UserUpdate userUpdate, UUID uuid, String username) {
        return User.builder()
                .id(uuid)
                .name(userUpdate.getName())
                .surname(userUpdate.getSurname())
                .username(username)
                .email(userUpdate.getEmail())
                .password(userUpdate.getPassword())
                .roles(userUpdate.getRoles())
                .isDeleted(userUpdate.getIsDeleted())
                .build();
    }
}