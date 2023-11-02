package com.madirex.funkosspringrest.services.category;

import com.madirex.funkosspringrest.dto.category.CreateCategoryDTO;
import com.madirex.funkosspringrest.dto.category.PatchCategoryDTO;
import com.madirex.funkosspringrest.dto.category.UpdateCategoryDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidIDException;
import com.madirex.funkosspringrest.exceptions.category.DeleteCategoryException;
import com.madirex.funkosspringrest.mappers.category.CategoryMapperImpl;
import com.madirex.funkosspringrest.models.Category;
import com.madirex.funkosspringrest.repositories.CategoryRepository;
import com.madirex.funkosspringrest.utils.Util;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase CategoryServiceImpl
 */
@Service
@CacheConfig(cacheNames = "category")
public class CategoryServiceImpl implements CategoryService {

    public static final String FUNKO_NOT_FOUND_MSG = "No se ha encontrado el Category con el ID indicado";
    public static final String NOT_VALID_FORMAT_ID_MSG = "El ID no tiene un formato válido";

    private final CategoryRepository categoryRepository;
    private final CategoryMapperImpl categoryMapperImpl;

    /**
     * Constructor CategoryServiceImpl
     *
     * @param categoryRepository CategoryRepositoryImpl
     * @param categoryMapperImpl CategoryMapper
     */
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapperImpl categoryMapperImpl) {
        this.categoryRepository = categoryRepository;
        this.categoryMapperImpl = categoryMapperImpl;
    }

    /**
     * Obtiene todos los Categorys
     *
     * @return Lista de Categorys
     */
    @Cacheable
    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    /**
     * Obtiene un Category por su ID
     *
     * @param id ID del Category a obtener
     * @return Category con el ID indicado
     * @throws CategoryNotValidIDException Si el ID no tiene un formato válido
     * @throws CategoryNotFoundException   Si no se ha encontrado el Category con el ID indicado
     */
    @Cacheable(key = "#result.id")
    @Override
    public Category getCategoryById(Long id) throws CategoryNotFoundException {
        return categoryRepository.findById(id).orElseThrow(() ->
                new CategoryNotFoundException(FUNKO_NOT_FOUND_MSG));
    }

    /**
     * Actualiza un Category
     *
     * @param id    ID del Category a actualizar
     * @param funko Category con los datos a actualizar
     * @return Category actualizado
     * @throws CategoryNotFoundException Si no se ha encontrado el Category con el ID indicado
     */
    @CachePut(key = "#result.id")
    @Override
    public Category patchCategory(Long id, PatchCategoryDTO funko) throws CategoryNotFoundException {
        var opt = categoryRepository.findById(id);
        if (opt.isPresent()) {
            BeanUtils.copyProperties(funko, opt.get(), Util.getNullPropertyNames(funko));
            opt.get().setId(id);
            opt.get().setUpdatedAt(LocalDateTime.now());
            return categoryRepository.save(opt.get());
        }
        throw new CategoryNotFoundException(FUNKO_NOT_FOUND_MSG);
    }

    /**
     * Crea un Category
     *
     * @param funko CreateCategoryDTO con los datos del Category a crear
     * @return Category creado
     */
    @CachePut(key = "#result.id")
    @Override
    public Category postCategory(CreateCategoryDTO funko) {
        return categoryRepository.save(categoryMapperImpl.toCategory(funko));
    }

    /**
     * Actualiza un Category
     *
     * @param id    ID del Category a actualizar
     * @param funko UpdateCategoryDTO con los datos a actualizar
     * @return Category actualizado
     * @throws CategoryNotFoundException Si no se ha encontrado el Category con el ID indicado
     */
    @CachePut(key = "#result.id")
    @Override
    public Category putCategory(Long id, UpdateCategoryDTO funko) throws CategoryNotFoundException {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Categoría no encontrada"));
        Category f = categoryMapperImpl.toCategory(existingCategory, funko);
        f.setId(id);
        return categoryRepository.save(f);
    }

    /**
     * Elimina un Category
     *
     * @param id ID del Category a eliminar
     * @throws CategoryNotFoundException Si no se ha encontrado el Category con el ID indicado
     */
    @CacheEvict(key = "#id")
    @Override
    public void deleteCategory(Long id) throws CategoryNotFoundException, DeleteCategoryException {
        var opt = categoryRepository.findById(id);
        if (opt.isEmpty()) {
            throw new CategoryNotFoundException(FUNKO_NOT_FOUND_MSG);
        }
        try {
            categoryRepository.delete(opt.get());
        } catch (Exception ex) {
            throw new DeleteCategoryException(ex.getMessage());
        }
    }
}