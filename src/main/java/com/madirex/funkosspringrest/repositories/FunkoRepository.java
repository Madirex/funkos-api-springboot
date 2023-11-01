package com.madirex.funkosspringrest.repositories;

import com.madirex.funkosspringrest.models.Funko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface FunkoRepository
 */
@Repository
public interface FunkoRepository extends JpaRepository<Funko, UUID> {

}
