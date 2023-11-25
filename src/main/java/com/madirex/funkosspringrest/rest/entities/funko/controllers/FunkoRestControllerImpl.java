package com.madirex.funkosspringrest.rest.entities.funko.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.madirex.funkosspringrest.rest.entities.category.exceptions.CategoryNotFoundException;
import com.madirex.funkosspringrest.rest.entities.funko.dto.CreateFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.GetFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.PatchFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.UpdateFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.exceptions.FunkoNotFoundException;
import com.madirex.funkosspringrest.rest.entities.funko.exceptions.FunkoNotValidUUIDException;
import com.madirex.funkosspringrest.rest.entities.funko.services.FunkoServiceImpl;
import com.madirex.funkosspringrest.rest.pagination.exceptions.PageNotValidException;
import com.madirex.funkosspringrest.rest.pagination.model.PageResponse;
import com.madirex.funkosspringrest.rest.pagination.util.PaginationLinksUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

/**
 * Clase FunkoRestControllerImpl
 */
@RestController
@RequestMapping("/api/funkos")
public class FunkoRestControllerImpl implements FunkoRestController {

    private final FunkoServiceImpl service;
    private final PaginationLinksUtils paginationLinksUtils;

    /**
     * Constructor de la clase
     *
     * @param service              Servicio de Funko
     * @param paginationLinksUtils Utilidad para la paginación
     */
    @Autowired
    public FunkoRestControllerImpl(FunkoServiceImpl service, PaginationLinksUtils paginationLinksUtils) {
        this.service = service;
        this.paginationLinksUtils = paginationLinksUtils;
    }

    /**
     * Método para obtener todos los Funkos
     *
     * @param category    categoría
     * @param maxPrice    precio máximo
     * @param maxQuantity cantidad máxima
     * @param page        página
     * @param size        tamaño de página
     * @param sortBy      ordenar por
     * @param direction   dirección
     * @param request     petición
     * @return ResponseEntity con el código de estado
     */
    @GetMapping()
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<PageResponse<GetFunkoDTO>> getAllFunko(
            @Valid @RequestParam(required = false) Optional<String> category,
            @RequestParam(required = false) Optional<Double> maxPrice,
            @RequestParam(required = false) Optional<Integer> maxQuantity,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            HttpServletRequest request
    ) {
        if (page < 0 || size < 1) {
            throw new PageNotValidException("La página no puede ser menor que 0 y su tamaño no debe de ser menor a 1.");
        }
        Sort sort = direction.equalsIgnoreCase(
                Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString());
        Page<GetFunkoDTO> pageResult = service.getAllFunko(category, maxPrice, maxQuantity,
                PageRequest.of(page, size, sort));
        return ResponseEntity.ok()
                .header("link", paginationLinksUtils.createLinkHeader(pageResult, uriBuilder))
                .body(PageResponse.of(pageResult, sortBy, direction));
    }

    /**
     * Método para obtener un Funko por su UUID
     *
     * @param id UUID del Funko en formato String
     * @return ResponseEntity con el código de estado
     * @throws FunkoNotFoundException Si no se ha encontrado el Funko con el UUID indicado
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @Override
    public ResponseEntity<GetFunkoDTO> getFunkoById(@Valid @PathVariable String id) throws FunkoNotFoundException {
        return ResponseEntity.ok(service.getFunkoById(id));
    }

    /**
     * Método para crear un Funko
     *
     * @param funko Funko a crear
     * @return ResponseEntity con el código de estado
     * @throws JsonProcessingException   Si no se ha podido convertir el objeto a JSON
     * @throws CategoryNotFoundException Si no se ha encontrado la categoría
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<GetFunkoDTO> postFunko(@Valid @RequestBody CreateFunkoDTO funko) throws JsonProcessingException,
            CategoryNotFoundException {
        GetFunkoDTO funkoDTO = service.postFunko(funko);
        return ResponseEntity.status(HttpStatus.CREATED).body(funkoDTO);
    }

    /**
     * Método para actualizar un Funko
     *
     * @param id    id del Funko
     * @param funko Funko a actualizar
     * @return ResponseEntity con el código de estado
     * @throws FunkoNotFoundException     del Funko
     * @throws FunkoNotValidUUIDException del Funko
     * @throws JsonProcessingException    excepción Json
     * @throws CategoryNotFoundException  de la categoría
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<GetFunkoDTO> putFunko(@Valid @PathVariable String id, @Valid @RequestBody UpdateFunkoDTO funko)
            throws FunkoNotFoundException, FunkoNotValidUUIDException, JsonProcessingException,
            CategoryNotFoundException {
        return ResponseEntity.ok(service.putFunko(id, funko));
    }

    /**
     * Método para actualizar parcialmente un Funko
     *
     * @param id    id del Funko
     * @param funko Funko a actualizar
     * @return ResponseEntity con el código de estado
     * @throws FunkoNotFoundException     del Funko
     * @throws FunkoNotValidUUIDException del Funko
     * @throws JsonProcessingException    excepción Json
     * @throws CategoryNotFoundException  de la categoría
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<GetFunkoDTO> patchFunko(@Valid @PathVariable String id, @Valid @RequestBody PatchFunkoDTO funko)
            throws FunkoNotFoundException, FunkoNotValidUUIDException, JsonProcessingException,
            CategoryNotFoundException {
        return ResponseEntity.ok(service.patchFunko(id, funko));
    }

    /**
     * Método para eliminar un Funko
     *
     * @param id id del Funko
     * @return ResponseEntity con el código de estado
     * @throws FunkoNotFoundException  del Funko
     * @throws JsonProcessingException excepción Json
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public ResponseEntity<String> deleteFunko(@Valid @PathVariable String id) throws FunkoNotFoundException,
            JsonProcessingException {
        service.deleteFunko(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Método para actualizar la imagen de un Funko
     *
     * @param id   id del Funko
     * @param file imagen del Funko
     * @return ResponseEntity con el código de estado
     * @throws FunkoNotValidUUIDException del Funko
     * @throws CategoryNotFoundException  de la categoría
     * @throws FunkoNotFoundException     del Funko
     * @throws IOException                excepción de entrada/salida
     */
    @PatchMapping(value = "/image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetFunkoDTO> newFunkoImg(
            @PathVariable String id,
            @RequestPart("file") MultipartFile file) throws FunkoNotValidUUIDException, CategoryNotFoundException,
            FunkoNotFoundException, IOException {
        if (!file.isEmpty()) {
            return ResponseEntity.ok(service.updateImage(id, file, true));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No se ha enviado una imagen para el Funko");
        }
    }
}