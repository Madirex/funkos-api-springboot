package com.madirex.funkosspringrest.rest.entities.category.repository;

import com.madirex.funkosspringrest.rest.entities.category.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * FunkoRepository
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>, JpaSpecificationExecutor<Category> {
    /**
     * Método que busca una categoría por su tipo
     *
     * @param type Tipo de categoría
     * @return Categoría
     */
    Optional<Category> findByType(String type);
}
