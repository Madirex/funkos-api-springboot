package com.madirex.funkosspringrest.rest.entities.funko.repository;

import com.madirex.funkosspringrest.rest.entities.funko.models.Funko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * FunkoRepository
 */
@Repository
public interface FunkoRepository extends JpaRepository<Funko, UUID>, JpaSpecificationExecutor<Funko> {

}
