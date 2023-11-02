package com.madirex.funkosspringrest.services.funko;

import com.madirex.funkosspringrest.dto.funko.CreateFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.GetFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.PatchFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.UpdateFunkoDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidIDException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotFoundException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotValidUUIDException;
import com.madirex.funkosspringrest.mappers.funko.FunkoMapperImpl;
import com.madirex.funkosspringrest.models.Category;
import com.madirex.funkosspringrest.models.Funko;
import com.madirex.funkosspringrest.repositories.FunkoRepository;
import com.madirex.funkosspringrest.services.category.CategoryService;
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
import java.util.UUID;

/**
 * Clase FunkoServiceImpl
 */
@Service
@CacheConfig(cacheNames = "funkos")
public class FunkoServiceImpl implements FunkoService {

    public static final String FUNKO_NOT_FOUND_MSG = "No se ha encontrado el Funko con el UUID indicado";
    public static final String NOT_VALID_FORMAT_UUID_MSG = "El UUID no tiene un formato válido";
    public static final String CATEGORY_ID_NOT_FOUND_MSG = "No se ha encontrado la categoría con el ID indicado.";

    private final FunkoRepository funkoRepository;
    private final FunkoMapperImpl funkoMapperImpl;
    private final CategoryService categoryService;

    /**
     * Constructor FunkoServiceImpl
     *
     * @param funkoRepository FunkoRepositoryImpl
     * @param funkoMapperImpl FunkoMapper
     * @param categoryService CategoryService
     */
    @Autowired
    public FunkoServiceImpl(FunkoRepository funkoRepository, FunkoMapperImpl funkoMapperImpl, CategoryService categoryService) {
        this.funkoRepository = funkoRepository;
        this.funkoMapperImpl = funkoMapperImpl;
        this.categoryService = categoryService;
    }

    /**
     * Obtiene todos los Funkos filtrados por categoría
     *
     * @param category Categoría por la que filtrar
     * @return Lista de Funkos filtrados por categoría
     */
    @Cacheable
    public List<GetFunkoDTO> getAllFunkoFilterByCategory(List<GetFunkoDTO> funkoList, String category) {
        return funkoList.stream()
                .filter(f -> f.getCategory() != null && f.getCategory().getType().name().equalsIgnoreCase(category))
                .toList();
    }

    /**
     * Obtiene todos los Funkos
     *
     * @return Lista de Funkos
     */
    @Cacheable
    @Override
    public List<GetFunkoDTO> getAllFunko() {
        var list = funkoRepository.findAll();
        return funkoMapperImpl.toFunkoList(list);
    }

    /**
     * Obtiene un Funko por su UUID
     *
     * @param id UUID del Funko a obtener
     * @return Funko con el UUID indicado
     * @throws FunkoNotValidUUIDException Si el UUID no tiene un formato válido
     * @throws FunkoNotFoundException     Si no se ha encontrado el Funko con el UUID indicado
     */
    @Cacheable(key = "#result.id")
    @Override
    public GetFunkoDTO getFunkoById(String id) throws FunkoNotValidUUIDException, FunkoNotFoundException {
        try {
            UUID uuid = UUID.fromString(id);
            var f = funkoRepository.findById(uuid).orElseThrow(() ->
                    new FunkoNotFoundException(FUNKO_NOT_FOUND_MSG));
            return funkoMapperImpl.toGetFunkoDTO(f);
        } catch (IllegalArgumentException e) {
            throw new FunkoNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
        }
    }

    /**
     * Crea un Funko
     *
     * @param funko CreateFunkoDTO con los datos del Funko a crear
     * @return Funko creado
     * @throws CategoryNotFoundException   Si no se ha encontrado la categoría con el ID indicado
     * @throws CategoryNotValidIDException Si el ID no tiene un formato válido
     */
    @CachePut(key = "#result.id")
    @Override
    public GetFunkoDTO postFunko(CreateFunkoDTO funko) throws CategoryNotFoundException, CategoryNotValidIDException {
        var category = categoryService.getCategoryById(funko.getCategoryId());
        var f = funkoRepository.save(funkoMapperImpl.toFunko(funko, category));
        return funkoMapperImpl.toGetFunkoDTO(f);
    }

    /**
     * Actualiza un Funko
     *
     * @param id    UUID del Funko a actualizar
     * @param funko UpdateFunkoDTO con los datos a actualizar
     * @return Funko actualizado
     * @throws FunkoNotValidUUIDException Si el UUID no tiene un formato válido
     */
    @CachePut(key = "#result.id")
    @Override
    public GetFunkoDTO putFunko(String id, UpdateFunkoDTO funko) throws FunkoNotValidUUIDException, CategoryNotFoundException, CategoryNotValidIDException, FunkoNotFoundException {
        try {
            UUID uuid = UUID.fromString(id);
            Funko existingFunko = funkoRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new FunkoNotFoundException("Funko no encontrado"));
            Category category = categoryService.getCategoryById(funko.getCategoryId());
            Funko f = funkoMapperImpl.toFunko(existingFunko, funko, category);
            f.setId(uuid);
            var modified = funkoRepository.save(f);
            return funkoMapperImpl.toGetFunkoDTO(modified);
        } catch (IllegalArgumentException e) {
            throw new FunkoNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
        }
    }

    /**
     * Actualiza un Funko
     *
     * @param id    UUID del Funko a actualizar
     * @param funko Funko con los datos a actualizar
     * @return Funko actualizado
     * @throws FunkoNotValidUUIDException  Si el UUID no tiene un formato válido
     * @throws FunkoNotFoundException      Si no se ha encontrado el Funko con el UUID indicado
     * @throws CategoryNotFoundException   Si no se ha encontrado la categoría con el ID indicado
     * @throws CategoryNotValidIDException Si el ID no tiene un formato válido
     */
    @CachePut(key = "#result.id")
    @Override
    public GetFunkoDTO patchFunko(String id, PatchFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidIDException {
        try {
            UUID uuid = UUID.fromString(id);
            var opt = funkoRepository.findById(uuid);
            if (opt.isEmpty()) {
                throw new FunkoNotFoundException(FUNKO_NOT_FOUND_MSG);
            }
            BeanUtils.copyProperties(funko, opt.get(), Util.getNullPropertyNames(funko));
            opt.get().setId(uuid);
            opt.get().setUpdatedAt(LocalDateTime.now());
            Funko modified = funkoRepository.save(opt.get());
            return funkoMapperImpl.toGetFunkoDTO(modified);
        } catch (IllegalArgumentException e) {
            throw new FunkoNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
        }
    }

    /**
     * Elimina un Funko
     *
     * @param id UUID del Funko a eliminar
     * @throws FunkoNotFoundException Si no se ha encontrado el Funko con el UUID indicado
     */
    @CacheEvict(key = "#id")
    @Override
    public void deleteFunko(String id) throws FunkoNotFoundException, FunkoNotValidUUIDException {
        try {
            UUID uuid = UUID.fromString(id);
            var opt = funkoRepository.findById(uuid);
            if (opt.isEmpty()) {
                throw new FunkoNotFoundException(FUNKO_NOT_FOUND_MSG);
            }
            funkoRepository.delete(opt.get());
        } catch (IllegalArgumentException e) {
            throw new FunkoNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
        }
    }
}