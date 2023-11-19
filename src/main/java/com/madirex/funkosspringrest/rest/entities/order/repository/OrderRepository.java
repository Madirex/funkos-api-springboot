package com.madirex.funkosspringrest.rest.entities.order.repository;

import com.madirex.funkosspringrest.rest.entities.order.models.Order;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * OrderRepository
 */
@Repository
public interface OrderRepository extends MongoRepository<Order, ObjectId> {
}