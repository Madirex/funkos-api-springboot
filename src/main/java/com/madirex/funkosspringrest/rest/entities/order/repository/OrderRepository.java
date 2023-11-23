package com.madirex.funkosspringrest.rest.entities.order.repository;

import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OrderRepository
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
    /**
     * Encuentra los pedidos dado un ID de usuario
     *
     * @param userId ID de usuario
     * @return Lista de pedidos
     */
    List<Order> findOrdersByUserId(String userId);

    /**
     * Existe un producto con el ID de usuario
     *
     * @param userId Id de usuario
     * @return ¿producto existe con el ID de usuario?
     */
    boolean existsByUserId(String userId);
}