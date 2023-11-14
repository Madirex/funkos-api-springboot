package com.madirex.funkosspringrest.services.funko;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.madirex.funkosspringrest.config.websockets.WebSocketConfig;
import com.madirex.funkosspringrest.config.websockets.WebSocketHandler;
import com.madirex.funkosspringrest.dto.funko.CreateFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.GetFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.PatchFunkoDTO;
import com.madirex.funkosspringrest.dto.funko.UpdateFunkoDTO;
import com.madirex.funkosspringrest.dto.notification.FunkoNotificationResponse;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotFoundException;
import com.madirex.funkosspringrest.exceptions.category.CategoryNotValidException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotFoundException;
import com.madirex.funkosspringrest.exceptions.funko.FunkoNotValidUUIDException;
import com.madirex.funkosspringrest.mappers.funko.FunkoMapperImpl;
import com.madirex.funkosspringrest.mappers.notification.FunkoNotificationMapper;
import com.madirex.funkosspringrest.models.Category;
import com.madirex.funkosspringrest.models.Funko;
import com.madirex.funkosspringrest.models.Notification;
import com.madirex.funkosspringrest.repositories.FunkoRepository;
import com.madirex.funkosspringrest.services.category.CategoryService;
import com.madirex.funkosspringrest.services.storage.StorageService;
import com.madirex.funkosspringrest.utils.Util;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Clase FunkoServiceImpl
 */
@Slf4j
@Service
@CacheConfig(cacheNames = "funkos")
public class FunkoServiceImpl implements FunkoService {

    public static final String FUNKO_NOT_FOUND_MSG = "No se ha encontrado el Funko con el UUID indicado";
    public static final String NOT_VALID_FORMAT_UUID_MSG = "El UUID no tiene un formato válido";

    private final FunkoRepository funkoRepository;
    private final FunkoMapperImpl funkoMapperImpl;
    private final WebSocketConfig webSocketConfig;
    @Setter
    private WebSocketHandler webSocketService;
    private final StorageService storageService;
    private final CategoryService categoryService;
    private final ObjectMapper mapper;
    private final FunkoNotificationMapper funkoNotificationMapper;


    /**
     * Constructor FunkoServiceImpl
     *
     * @param funkoRepository         FunkoRepositoryImpl
     * @param funkoMapperImpl         FunkoMapper
     * @param webSocketConfig         WebSocketConfig
     * @param storageService          StorageService
     * @param categoryService         CategoryService
     * @param funkoNotificationMapper FunkoNotificationMapper
     */
    @Autowired
    public FunkoServiceImpl(FunkoRepository funkoRepository, FunkoMapperImpl funkoMapperImpl,
                            WebSocketConfig webSocketConfig, StorageService storageService, CategoryService categoryService, FunkoNotificationMapper funkoNotificationMapper) {
        this.funkoRepository = funkoRepository;
        this.funkoMapperImpl = funkoMapperImpl;
        this.webSocketConfig = webSocketConfig;
        this.webSocketService = webSocketConfig.webSocketHandler();
        this.storageService = storageService;
        this.categoryService = categoryService;
        this.funkoNotificationMapper = funkoNotificationMapper;
        this.mapper = new ObjectMapper();
    }

    /**
     * Obtiene todos los Funkos
     *
     * @param category    Categoría por la que filtrar
     * @param maxPrice    Precio máximo por el que filtrar
     * @param maxQuantity Cantidad máxima por la que filtrar
     * @param pageable    Paginación
     * @return Lista de Funkos
     */
    @Cacheable
    @Override
    public Page<GetFunkoDTO> getAllFunko(Optional<String> category, Optional<Double> maxPrice,
                                         Optional<Integer> maxQuantity, Pageable pageable) {
        Specification<Funko> specType = (root, query, criteriaBuilder) ->
                category.map(m -> {
                    try {
                        return criteriaBuilder.equal(root.get("category").get("type"), Category.Type
                                .valueOf(m.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        return criteriaBuilder.isTrue(criteriaBuilder.literal(false));
                    }
                }).orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Funko> specMaxPrice = (root, query, criteriaBuilder) ->
                maxPrice.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Funko> specMaxQuantity = (root, query, criteriaBuilder) ->
                maxQuantity.map(p -> criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), p))
                        .orElseGet(() -> criteriaBuilder.isTrue(criteriaBuilder.literal(true)));

        Specification<Funko> criterion = Specification.where(specType)
                .and(specMaxPrice)
                .and(specMaxQuantity);

        Page<Funko> funkoPage = funkoRepository.findAll(criterion, pageable);
        List<GetFunkoDTO> dtoList = funkoPage.getContent().stream()
                .map(funkoMapperImpl::toGetFunkoDTO)
                .toList();

        return new PageImpl<>(dtoList, funkoPage.getPageable(), funkoPage.getTotalElements());
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
     * @throws CategoryNotFoundException Si no se ha encontrado la categoría con el ID indicado
     * @throws CategoryNotValidException Si el ID no tiene un formato válido
     */
    @CachePut(key = "#result.id")
    @Override
    public GetFunkoDTO postFunko(CreateFunkoDTO funko) throws CategoryNotFoundException, CategoryNotValidException, JsonProcessingException {
        var category = categoryService.getCategoryById(funko.getCategoryId());
        var f = funkoRepository.save(funkoMapperImpl.toFunko(funko, category));
        var funkoDTO = funkoMapperImpl.toGetFunkoDTO(f);
        onChange(Notification.Type.CREATE, funkoDTO);
        return funkoDTO;
    }

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
    @CachePut(key = "#result.id")
    @Override
    public GetFunkoDTO putFunko(String id, UpdateFunkoDTO funko) throws FunkoNotValidUUIDException,
            CategoryNotFoundException, CategoryNotValidException, FunkoNotFoundException, JsonProcessingException {
        try {
            UUID uuid = UUID.fromString(id);
            Funko existingFunko = funkoRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new FunkoNotFoundException("Funko no encontrado"));
            Category category = categoryService.getCategoryById(funko.getCategoryId());
            Funko f = funkoMapperImpl.toFunko(existingFunko, funko, category);
            f.setId(uuid);
            var modified = funkoRepository.save(f);
            var funkoDTO = funkoMapperImpl.toGetFunkoDTO(modified);
            onChange(Notification.Type.UPDATE, funkoDTO);
            return funkoDTO;
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
     * @throws FunkoNotValidUUIDException Si el UUID no tiene un formato válido
     * @throws FunkoNotFoundException     Si no se ha encontrado el Funko con el UUID indicado
     * @throws CategoryNotFoundException  Si no se ha encontrado la categoría con el ID indicado
     * @throws CategoryNotValidException  Si el ID no tiene un formato válido
     */
    @CachePut(key = "#result.id")
    @Override
    public GetFunkoDTO patchFunko(String id, PatchFunkoDTO funko) throws FunkoNotValidUUIDException,
            FunkoNotFoundException, CategoryNotFoundException, CategoryNotValidException, JsonProcessingException {
        try {
            UUID uuid = UUID.fromString(id);
            var opt = funkoRepository.findById(uuid);
            if (opt.isEmpty()) {
                throw new FunkoNotFoundException(FUNKO_NOT_FOUND_MSG);
            }
            BeanUtils.copyProperties(funko, opt.get(), Util.getNullPropertyNames(funko));
            opt.get().setId(uuid);
            opt.get().setUpdatedAt(LocalDateTime.now());
            opt.get().setCategory(categoryService.getCategoryById(funko.getCategoryId()));
            Funko modified = funkoRepository.save(opt.get());
            var funkoDTO = funkoMapperImpl.toGetFunkoDTO(modified);
            onChange(Notification.Type.UPDATE, funkoDTO);
            return funkoDTO;
        } catch (IllegalArgumentException e) {
            throw new FunkoNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
        }
    }

    /**
     * Elimina un Funko
     *
     * @param id UUID del Funko a eliminar
     * @throws FunkoNotFoundException     Si no se ha encontrado el Funko con el UUID indicado
     * @throws FunkoNotValidUUIDException Si el UUID no tiene un formato válido
     */
    @CacheEvict(key = "#id")
    @Override
    public void deleteFunko(String id) throws FunkoNotFoundException, FunkoNotValidUUIDException, JsonProcessingException {
        try {
            UUID uuid = UUID.fromString(id);
            var opt = funkoRepository.findById(uuid);
            if (opt.isEmpty()) {
                throw new FunkoNotFoundException(FUNKO_NOT_FOUND_MSG);
            }
            funkoRepository.delete(opt.get());
            onChange(Notification.Type.DELETE, funkoMapperImpl.toGetFunkoDTO(opt.get()));
        } catch (IllegalArgumentException e) {
            throw new FunkoNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
        }
    }

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
    @Override
    @CachePut(key = "#result.id")
    @Transactional
    public GetFunkoDTO updateImage(String id, MultipartFile image, Boolean withUrl) throws FunkoNotFoundException,
            FunkoNotValidUUIDException, CategoryNotFoundException, CategoryNotValidException, IOException {
        try {
            UUID uuid = UUID.fromString(id);
            var actualFunko = funkoRepository.findById(uuid).orElseThrow(() -> new FunkoNotFoundException(id));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSSSSS");
            String imageStored = storageService.store(image, List.of("jpg", "jpeg", "png"), id
                    + "-" + LocalDateTime.now().format(formatter));
            String imageUrl = Boolean.FALSE.equals(withUrl) ? imageStored : storageService.getUrl(imageStored);

            if (actualFunko.getImage() != null && !actualFunko.getImage().equals(Funko.IMAGE_DEFAULT)) {
                storageService.delete(actualFunko.getImage());
            }

            return patchFunko(id, PatchFunkoDTO.builder()
                    .image(imageUrl)
                    .build());
        } catch (IllegalArgumentException e) {
            throw new FunkoNotValidUUIDException(NOT_VALID_FORMAT_UUID_MSG);
        }
    }

    /**
     * Método para enviar una notificación a los clientes ws
     *
     * @param type Tipo de notificación
     * @param data Datos de la notificación
     */
    public void onChange(Notification.Type type, GetFunkoDTO data) throws JsonProcessingException {
        log.debug("Servicio de productos onChange con tipo: " + type + " y datos: " + data);
        if (webSocketService == null) {
            log.warn("No se ha podido enviar la notificación a los clientes ws, no se ha encontrado el servicio");
            webSocketService = this.webSocketConfig.webSocketHandler();
        }
        Notification<FunkoNotificationResponse> notification = new Notification<>(
                "FUNKOS",
                type,
                funkoNotificationMapper.toFunkoNotificationDto(data),
                LocalDateTime.now().toString()
        );

        String json = mapper.writeValueAsString(notification);

        log.info("Enviando mensaje a los clientes ws");
        Thread senderThread = new Thread(() -> webSocketService.sendMessage(json));
        senderThread.start();
    }
}