package com.madirex.funkosspringrest.rest.entities.auth.services.users;

import com.madirex.funkosspringrest.rest.entities.auth.repository.AuthUsersRepository;
import com.madirex.funkosspringrest.rest.entities.user.exceptions.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Implementación de AuthUsersService
 */
@Service("userDetailsService")
public class AuthUsersServiceImpl implements AuthUsersService {
    private final AuthUsersRepository authUsersRepository;

    /**
     * Constructor
     *
     * @param authUsersRepository repositorio de usuarios
     */
    @Autowired
    public AuthUsersServiceImpl(AuthUsersRepository authUsersRepository) {
        this.authUsersRepository = authUsersRepository;
    }

    /**
     * Carga un usuario por su nombre de usuario
     *
     * @param username Nombre de usuario
     * @return Usuario
     * @throws UserNotFound Usuario no encontrado
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UserNotFound {
        return authUsersRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFound("Usuario con username " + username + " no encontrado"));
    }
}