package com.madirex.funkosspringrest.rest.entities.user.services;

import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import com.madirex.funkosspringrest.rest.entities.order.repository.OrderRepository;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserInfoResponse;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserRequest;
import com.madirex.funkosspringrest.rest.entities.user.dto.UserResponse;
import com.madirex.funkosspringrest.rest.entities.user.exceptions.UserNotFound;
import com.madirex.funkosspringrest.rest.entities.user.exceptions.UserNotValidUUIDException;
import com.madirex.funkosspringrest.rest.entities.user.exceptions.UsernameOrEmailExists;
import com.madirex.funkosspringrest.rest.entities.user.mappers.UsersMapper;
import com.madirex.funkosspringrest.rest.entities.user.models.User;
import com.madirex.funkosspringrest.rest.entities.user.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * Implementación de UsersService
 */
@Service
@Slf4j
@CacheConfig(cacheNames = {"users"})
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;
    private final OrderRepository orderRepository;
    private final UsersMapper usersMapper;

    /**
     * Constructor
     *
     * @param usersRepository repositorio de usuarios
     * @param orderRepository repositorio de pedidos
     * @param usersMapper     mapeador de usuarios
     */
    public UsersServiceImpl(UsersRepository usersRepository, OrderRepository orderRepository, UsersMapper usersMapper) {
        this.usersRepository = usersRepository;
        this.orderRepository = orderRepository;
        this.usersMapper = usersMapper;
    }

    /**
     * Método que busca entre todos los usuarios
     *
     * @param username  Nombre de usuario
     * @param email     Email
     * @param isDeleted Borrado
     * @param pageable  Paginación
     * @return Lista de usuarios
     */
    @Override
    public Page<UserResponse> findAll(Optional<String> username, Optional<String> email, Optional<Boolean> isDeleted, Pageable pageable) {
        log.info("Buscando todos los usuarios con username: " + username + " y borrados: " + isDeleted);
        Specification<User> specUsernameUser = (root, query, criteriaBuilder) ->
                username.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("username")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<User> specEmailUser = (root, query, criteriaBuilder) ->
                email.map(m -> criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), "%" + m.toLowerCase() + "%"))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<User> specIsDeleted = (root, query, criteriaBuilder) ->
                isDeleted.map(m -> criteriaBuilder.equal(root.get("isDeleted"), m))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<User> criterion = Specification.where(specUsernameUser)
                .and(specEmailUser)
                .and(specIsDeleted);

        return usersRepository.findAll(criterion, pageable).map(usersMapper::toUserResponse);
    }

    /**
     * Método que busca un usuario por su ID
     *
     * @param id ID del usuario
     * @return Usuario
     */
    @Override
    @Cacheable(key = "#id")
    public UserInfoResponse findById(String id) {
        log.info("Buscando usuario por ID: " + id);
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new UserNotValidUUIDException(id);
        }
        var user = usersRepository.findById(uuid).orElseThrow(() -> new UserNotFound(id));
        var orders = orderRepository.findOrdersByUserId(id).stream().map(Order::getId).toList();
        return usersMapper.toUserInfoResponse(user, orders);
    }

    /**
     * Método que guarda un usuario
     *
     * @param userRequest Nombre de usuario
     * @return Usuario
     */
    @Override
    @CachePut(key = "#result.id")
    public UserResponse save(UserRequest userRequest) {
        log.info("Guardando usuario: " + userRequest);
        usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(), userRequest.getEmail())
                .ifPresent(u -> {
                    throw new UsernameOrEmailExists("Ya existe un usuario con ese username o email");
                });
        return usersMapper.toUserResponse(usersRepository.save(usersMapper.toUser(userRequest)));
    }

    /**
     * Método que actualiza un usuario
     *
     * @param id          ID del usuario
     * @param userRequest Usuario
     * @return Usuario
     */
    @Override
    @CachePut(key = "#result.id")
    public UserResponse update(String id, UserRequest userRequest) {
        log.info("Actualizando usuario: " + userRequest);
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new UserNotValidUUIDException(id);
        }
        usersRepository.findById(uuid).orElseThrow(() -> new UserNotFound(id));
        usersRepository.findByUsernameEqualsIgnoreCaseOrEmailEqualsIgnoreCase(userRequest.getUsername(), userRequest.getEmail())
                .ifPresent(u -> {
                    if (!u.getId().toString().equals(id)) {
                        log.debug("usuario encontrado: " + u.getId() + " ID: " + id);
                        throw new UsernameOrEmailExists("Ya existe un usuario con ese username o email");
                    }
                });
        return usersMapper.toUserResponse(usersRepository.save(usersMapper.toUser(userRequest, uuid)));
    }

    /**
     * Método que borra un usuario dada su ID
     * Si el usuario tiene pedidos, se marca como borrado lógico. Si no, se borra físicamente
     *
     * @param id ID del usuario
     */
    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void deleteById(String id) {
        log.info("Borrando usuario por ID: " + id);
        UUID uuid;
        try {
            uuid = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new UserNotValidUUIDException(id);
        }
        User user = usersRepository.findById(uuid).orElseThrow(() -> new UserNotFound(id));
        if (orderRepository.existsByUserId(id)) {
            log.info("Borrado lógico de usuario por ID: " + id);
            usersRepository.updateIsDeletedToTrueById(uuid);
        } else {
            log.info("Borrado físico de usuario por ID: " + id);
            usersRepository.delete(user);
        }
    }
}