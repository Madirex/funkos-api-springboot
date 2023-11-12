package com.madirex.funkosspringrest.repositories;

import com.madirex.funkosspringrest.models.Funko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Interface FunkoRepository
 */
@Repository
public interface FunkoRepository extends JpaRepository<Funko, UUID>, JpaSpecificationExecutor<Funko> {

}
