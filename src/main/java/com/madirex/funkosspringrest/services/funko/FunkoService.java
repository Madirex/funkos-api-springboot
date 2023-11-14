package com.madirex.funkosspringrest.services.funko;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.madirex.funkosspringrest.dto.funko.CreateFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.GetFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.PatchFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.UpdateFunkoDTO;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotFoundException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotValidUUIDException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * Interface FunkoService
 */
public interface FunkoService {
    /**
     * Obtiene todos los Funkos
     *
     * @param category    Categoría por la que filtrar
     * @param maxPrice    Precio máximo por el que filtrar
     * @param maxQuantity Cantidad máxima por la que filtrar
     * @param pageable    Paginación
     * @return Lista de Funkos
     */
    Page<GetFunkoDTO> getAllFunko(Optional<String> category, Optional<Double> maxPrice,
                                  Optional<Integer> maxQuantity, Pageable pageable);

    /**
     * Obtiene un Funko por su UUID
     *
     * @param id UUID del Funko a obtener
     * @return Funko con el UUID indicado
     * @throws FunkoNotValidUUIDException Si el UUID no tiene un formato válido
     * @throws FunkoNotFoundException     Si no se ha encontrado el Funko con el UUID indicado
     */
    GetFunkoDTO getFunkoById(String id) throws FunkoNotValidUUIDException, FunkoNotFoundException;

    /**
     * Crea un Funko
     *
     * @param funko CreateFunkoDTO con los datos del Funko a crear
     * @return Funko creado
     * @throws CategoryNotFoundException Si no se ha encontrado la categoría con el ID indicado
     * @throws CategoryNotValidException Si el ID no tiene un formato válido
     */
    GetFunkoDTO postFunko(CreateFunkoDTO funko) throws CategoryNotFoundException, CategoryNotValidException, JsonProcessingException;

    /**
     * Actualiza un Funko
     *
     * @param id    UUID del Funko a actualizar
     * @param funko UpdateFunkoDTO con los datos a actualizar
     * @return Funko actualizado
     * @throws FunkoNotValidUUIDException Si el UUID no tiene un formato válido
     * @throws CategoryNotFoundException  Si no se ha encontrado la categoría con el ID indicado
     * @throws CategoryNotValidException  Si el ID no tiene un formato válido
     * @throws FunkoNotFoundException     Si no se ha encontrado el Funko con el UUID indicado
     */
    GetFunkoDTO putFunko(String id, UpdateFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidException, JsonProcessingException;

    /**
     * Actualiza un Funko
     *
     * @param id    UUID del Funko a actualizar
     * @param funko Funko con los datos a actualizar
     * @return Funko actualizado
     * @throws FunkoNotValidUUIDException Si el UUID no tiene un formato válido
     * @throws FunkoNotFoundException     Si no se ha encontrado el Funko con el UUID indicado
     * @throws CategoryNotFoundException  Si no se ha encontrado la categoría con el ID indicado
     * @throws CategoryNotValidException  Si el ID no tiene un formato válido
     */
    GetFunkoDTO patchFunko(String id, PatchFunkoDTO funko) throws FunkoNotValidUUIDException, FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidException, JsonProcessingException;

    /**
     * Elimina un Funko
     *
     * @param id UUID del Funko a eliminar
     * @throws FunkoNotFoundException     Si no se ha encontrado el Funko con el UUID indicado
     * @throws FunkoNotValidUUIDException Si el UUID no tiene un formato válido
     * @throws JsonProcessingException    Si no se ha podido convertir el Funko a JSON
     */
    void deleteFunko(String id) throws FunkoNotFoundException, FunkoNotValidUUIDException, JsonProcessingException;

    /**
     * Actualiza la imagen de un Funko
     *
     * @param id      UUID del Funko a actualizar
     * @param image   Imagen a actualizar
     * @param withUrl Si se quiere devolver la URL de la imagen
     * @return Funko actualizado
     * @throws FunkoNotFoundException     Si no se ha encontrado el Funko con el UUID indicado
     * @throws FunkoNotValidUUIDException Si el UUID no tiene un formato válido
     * @throws CategoryNotFoundException  Si no se ha encontrado la categoría con el ID indicado
     * @throws CategoryNotValidException  Si el ID no tiene un formato válido
     */
    GetFunkoDTO updateImage(String id, MultipartFile image, Boolean withUrl) throws FunkoNotFoundException, FunkoNotValidUUIDException, CategoryNotFoundException, CategoryNotValidException, IOException;
}
