package com.madirex.funkosspringrest.rest.entities.users;

import com.madirex.funkosspringrest.rest.entities.user.dto.UserRequest;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserUpdate;
import com.madirex.funkosspringrest.rest.entities.user.mappers.UsersMapper;
import com.madirex.funkosspringrest.rest.entities.user.models.Role;
import com.madirex.funkosspringrest.rest.entities.user.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Clase UserMapperImplTest
 */
class UserMapperImplTest {
    private UsersMapper userMapperImpl;

    /**
     * Método setUp para inicializar los objetos
     */
    @BeforeEach
    void setUp() {
        userMapperImpl = new UsersMapper();
    }

    /**
     * Test para comprobar que el mapeo de UserUpdate a User es correcto
     */
    @Test
    void testUserUpdateToUser() {
        var userUpdate = UserUpdate.builder()
                .name("nombre")
                .surname("apellido")
                .email("usuario@madirex.com")
                .password("password")
                .roles(Set.of(Role.USER))
                .isDeleted(false)
                .build();
        var mapped = userMapperImpl.toUser(userUpdate, UUID.randomUUID(), "username");
        assertAll("User properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(userUpdate.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(userUpdate.getSurname(), mapped.getSurname(), "El apellido debe coincidir"),
                () -> assertEquals(userUpdate.getEmail(), mapped.getEmail(), "El email debe coincidir"),
                () -> assertEquals(userUpdate.getPassword(), mapped.getPassword(), "La contraseña debe coincidir"),
                () -> assertEquals(userUpdate.getRoles(), mapped.getRoles(), "Los roles deben coincidir"),
                () -> assertEquals(userUpdate.getIsDeleted(), mapped.getIsDeleted(), "El estado de borrado debe coincidir")
        );
    }

    /**
     * Test para comprobar que el mapeo de UserRequest a User es correcto
     */
    @Test
    void testUserRequestToUser() {
        var userRequest = UserRequest.builder()
                .name("nombre")
                .surname("apellido")
                .email("usuario@madirex.com")
                .password("password")
                .roles(Set.of(Role.USER))
                .isDeleted(false)
                .build();
        var mapped = userMapperImpl.toUser(userRequest);
        assertAll("User properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(userRequest.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(userRequest.getSurname(), mapped.getSurname(), "El apellido debe coincidir"),
                () -> assertEquals(userRequest.getEmail(), mapped.getEmail(), "El email debe coincidir"),
                () -> assertEquals(userRequest.getPassword(), mapped.getPassword(), "La contraseña debe coincidir"),
                () -> assertEquals(userRequest.getRoles(), mapped.getRoles(), "Los roles deben coincidir"),
                () -> assertEquals(userRequest.getIsDeleted(), mapped.getIsDeleted(), "El estado de borrado debe coincidir")
        );
    }

    /**
     * Test para comprobar que el mapeo de User a UserResponse es correcto
     */
    @Test
    void testUserToUserResponse() {
        var user = User.builder()
                .id(UUID.randomUUID())
                .name("nombre")
                .surname("apellido")
                .username("username")
                .email("usuario@madirex.com")
                .password("password")
                .roles(Set.of(Role.USER))
                .isDeleted(false)
                .build();
        var mapped = userMapperImpl.toUserResponse(user);
        assertAll("User properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(user.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(user.getSurname(), mapped.getSurname(), "El apellido debe coincidir"),
                () -> assertEquals(user.getUsername(), mapped.getUsername(), "El username debe coincidir"),
                () -> assertEquals(user.getEmail(), mapped.getEmail(), "El email debe coincidir"),
                () -> assertEquals(user.getRoles(), mapped.getRoles(), "Los roles deben coincidir"),
                () -> assertEquals(user.getIsDeleted(), mapped.getIsDeleted(), "El estado de borrado debe coincidir")
        );
    }

    /**
     * Test para comprobar que el mapeo de User a UserInfoResponse es correcto
     */
    @Test
    void testUserToUserInfoResponse() {
        var user = User.builder()
                .id(UUID.randomUUID())
                .name("nombre")
                .surname("apellido")
                .username("username")
                .email("usuario@madirex.com")
                .password("password")
                .roles(Set.of(Role.USER))
                .isDeleted(false)
                .build();
        var orders = new ArrayList<>(
                Set.of(
                        "Pedido 1",
                        "Pedido 2",
                        "Pedido 3"
                )
        );
        var mapped = userMapperImpl.toUserInfoResponse(user, orders);
        assertAll("User properties",
                () -> assertNotNull(mapped.getId(), "El ID no debe ser nulo"),
                () -> assertEquals(user.getName(), mapped.getName(), "El nombre debe coincidir"),
                () -> assertEquals(user.getSurname(), mapped.getSurname(), "El apellido debe coincidir"),
                () -> assertEquals(user.getUsername(), mapped.getUsername(), "El username debe coincidir"),
                () -> assertEquals(user.getEmail(), mapped.getEmail(), "El email debe coincidir"),
                () -> assertEquals(user.getRoles(), mapped.getRoles(), "Los roles deben coincidir"),
                () -> assertEquals(user.getIsDeleted(), mapped.getIsDeleted(), "El estado de borrado debe coincidir"),
                () -> assertEquals(orders, mapped.getOrders(), "Los pedidos deben coincidir")
        );
    }
}
