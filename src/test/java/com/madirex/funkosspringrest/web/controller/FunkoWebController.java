package com.madirex.funkosspringrest.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.madirex.funkosspringrest.rest.entities.category.models.Category;
import com.madirex.funkosspringrest.rest.entities.category.services.CategoryService;
import com.madirex.funkosspringrest.rest.entities.funko.dto.CreateFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.GetFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.dto.UpdateFunkoDTO;
import com.madirex.funkosspringrest.rest.entities.funko.services.FunkoService;
import com.madirex.funkosspringrest.web.store.UserStore;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

/**
 * FunkoWebController
 */
@Controller
@RequestMapping("/funko")
@Slf4j
public class FunkoWebController {
    public static final String NOT_LOGGED_BACK_TO_LOGIN_MSG = "No hay sesión o no ha hecho login. Volviendo al Login";
    private final FunkoService funkoService;
    private final CategoryService categoryService;
    private final MessageSource messageSource;
    private final UserStore userSession;

    /**
     * Constructor FunkoWebController
     *
     * @param funkoService    Servicio de Funko
     * @param categoryService Servicio de Categoría
     * @param messageSource   MessageSource
     * @param userSession     UserStore
     */
    @Autowired
    public FunkoWebController(FunkoService funkoService, CategoryService categoryService, MessageSource messageSource,
                              UserStore userSession) {
        this.funkoService = funkoService;
        this.categoryService = categoryService;
        this.messageSource = messageSource;
        this.userSession = userSession;
    }

    /**
     * Método para mostrar el formulario de login
     *
     * @param session Sesión
     * @return Vista login
     */
    @GetMapping("/login")
    public String login(HttpSession session) {
        log.info("Login GET");
        if (isLoggedAndSessionIsActive(session)) {
            log.info("Volviendo al index");
            return "redirect:/funko";
        }
        return "funko/login";
    }

    /**
     * Método para comprobar el login
     *
     * @param password Contraseña
     * @param session  Sesión
     * @param model    Modelo
     * @return Vista login o index
     */
    @PostMapping
    public String login(@RequestParam("password") String password, HttpSession session, Model model) {
        log.info("Login POST");
        if ("pass".equals(password)) {
            userSession.setLastLogin(new Date());
            userSession.setLogged(true);
            session.setAttribute("userSession", userSession);
            session.setMaxInactiveInterval(1800);
            return "redirect:/funko";
        } else {
            return "funko/login";
        }
    }

    /**
     * Método para cerrar sesión
     *
     * @param session Sesión
     * @return Vista login
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        log.info("Logout GET");
        session.invalidate();
        return "redirect:/funko";
    }

    /**
     * Método para mostrar el index
     *
     * @param session   Sesión
     * @param model     Modelo
     * @param search    Búsqueda
     * @param page      Página
     * @param size      Tamaño
     * @param sortBy    Ordenar por
     * @param direction Dirección
     * @param locale    Localización
     * @return Vista index
     */
    @GetMapping(path = {"", "/", "/index", "/list"})
    public String index(HttpSession session,
                        Model model,
                        @RequestParam(value = "search", required = false) Optional<String> search,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "10") int size,
                        @RequestParam(defaultValue = "id") String sortBy,
                        @RequestParam(defaultValue = "asc") String direction,
                        Locale locale
    ) {
        if (!isLoggedAndSessionIsActive(session)) {
            log.info(NOT_LOGGED_BACK_TO_LOGIN_MSG);
            return "redirect:/funko/login";
        }
        log.info("Index GET con parámetros search: " + search + ", page: " + page + ", size: " + size);
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy)
                .ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        var funkoPage = funkoService.getAllFunko(search, Optional.empty(), Optional.empty(),
                pageable);
        String welcomeMessage = messageSource.getMessage("welcome.message", null, locale);
        UserStore sessionData = (UserStore) session.getAttribute("userSession");
        sessionData.incrementLoginCount();
        var visitNum = sessionData.getLoginCount();
        var lastLogin = sessionData.getLastLogin();
        var localizedLastLoginDate = getLocalizedDate(lastLogin, locale);
        model.addAttribute("funkoPage", funkoPage);
        model.addAttribute("search", search.orElse(""));
        model.addAttribute("welcomeMessage", welcomeMessage);
        model.addAttribute("visitNum", visitNum);
        model.addAttribute("lastLoginDate", localizedLastLoginDate);
        return "funko/index";
    }

    /**
     * Método para mostrar los detalles de un Funko
     *
     * @param id      Id del Funko
     * @param model   Modelo
     * @param session Sesión
     * @return Vista details
     */
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") String id, Model model, HttpSession session) {
        log.info("Details GET");
        if (!isLoggedAndSessionIsActive(session)) {
            log.info(NOT_LOGGED_BACK_TO_LOGIN_MSG);
            return "redirect:/funko/login";
        }
        GetFunkoDTO funko = funkoService.getFunkoById(id);
        model.addAttribute("funko", funko);
        return "funko/details";
    }

    /**
     * Método para mostrar el formulario de creación
     *
     * @param model   Modelo
     * @param session Sesión
     * @return Vista create
     */
    @GetMapping("/create")
    public String createForm(Model model, HttpSession session) {
        log.info("Create GET");
        if (!isLoggedAndSessionIsActive(session)) {
            log.info(NOT_LOGGED_BACK_TO_LOGIN_MSG);
            return "redirect:/funko/login";
        }
        var categories = categoryService.getAllCategory(Optional.empty(), Optional.empty(), PageRequest.of(0, 1000))
                .get()
                .map(Category::getType);
        var funko = CreateFunkoDTO.builder()
                .name("")
                .price(0.0)
                .quantity(0)
                .image("https://www.madirex.com/favicon.ico")
                .categoryId(null)
                .build();
        model.addAttribute("funko", funko);
        model.addAttribute("categories", categories);
        return "funko/create";
    }

    /**
     * Método para crear un Funko
     *
     * @param funkoDto DTO del Funko
     * @param result   Resultado
     * @param model    Modelo
     * @return Vista index
     * @throws JsonProcessingException excepción
     */
    @PostMapping("/create")
    public String create(@Valid @ModelAttribute("funko") CreateFunkoDTO funkoDto,
                         BindingResult result,
                         Model model) throws JsonProcessingException {
        log.info("Create POST");
        if (result.hasErrors()) {
            var categories = categoryService.getAllCategory(Optional.empty(), Optional.empty(), PageRequest.of(0, 1000))
                    .get()
                    .map(Category::getType);
            model.addAttribute("categories", categories);
            return "funko/create";
        }
        funkoService.postFunko(funkoDto);
        return "redirect:/funko";
    }

    /**
     * Método para mostrar el formulario de actualización
     *
     * @param id      Id del Funko
     * @param model   Modelo
     * @param session Sesión
     * @return Vista update
     */
    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable("id") String id, Model model, HttpSession session) {
        if (!isLoggedAndSessionIsActive(session)) {
            log.info(NOT_LOGGED_BACK_TO_LOGIN_MSG);
            return "redirect:/funko/login";
        }
        var categories = categoryService.getAllCategory(Optional.empty(), Optional.empty(),
                        PageRequest.of(0, 1000))
                .get()
                .map(Category::getType);
        GetFunkoDTO funko = funkoService.getFunkoById(id);
        UpdateFunkoDTO funkoUpdateRequest = UpdateFunkoDTO.builder()
                .name(funko.getName())
                .price(funko.getPrice())
                .quantity(funko.getQuantity())
                .image(funko.getImage())
                .categoryId(funko.getCategory().getId())
                .build();
        model.addAttribute("funko", funkoUpdateRequest);
        model.addAttribute("categories", categories);
        return "funko/update";
    }

    /**
     * Método para actualizar un Funko
     *
     * @param id                 Id del Funko
     * @param funkoUpdateRequest DTO del Funko de actualización
     * @param result             Resultado
     * @param model              Modelo
     * @return Vista index
     * @throws JsonProcessingException excepción
     */
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") String id, @Valid @ModelAttribute("funko")
    UpdateFunkoDTO funkoUpdateRequest, BindingResult result, Model model) throws JsonProcessingException {
        if (result.hasErrors()) {
            var categories = categoryService.getAllCategory(Optional.empty(), Optional.empty(),
                            PageRequest.of(0, 1000))
                    .get()
                    .map(Category::getType);
            model.addAttribute("categories", categories);
            return "funko/update";
        }
        log.info("Update POST");
        funkoService.putFunko(id, funkoUpdateRequest);
        return "redirect:/funko";
    }

    /**
     * Método para eliminar un Funko
     *
     * @param id      Id del Funko
     * @param session Sesión
     * @return Vista index
     * @throws JsonProcessingException excepción
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") String id, HttpSession session) throws JsonProcessingException {
        if (!isLoggedAndSessionIsActive(session)) {
            log.info(NOT_LOGGED_BACK_TO_LOGIN_MSG);
            return "redirect:/funko/login";
        }
        funkoService.deleteFunko(id);
        return "redirect:/funko";
    }

    /**
     * Método para mostrar el formulario de actualización de imagen
     *
     * @param productId Id del Funko
     * @param model     Modelo
     * @param session   Sesión
     * @return Vista update-image
     */
    @GetMapping("/update-image/{id}")
    public String showUpdateImageForm(@PathVariable("id") String productId, Model model, HttpSession session) {
        if (!isLoggedAndSessionIsActive(session)) {
            log.info(NOT_LOGGED_BACK_TO_LOGIN_MSG);
            return "redirect:/funko/login";
        }
        GetFunkoDTO funko = funkoService.getFunkoById(productId);
        model.addAttribute("funko", funko);
        return "funko/update-image";
    }

    /**
     * Método para actualizar la imagen de un Funko
     *
     * @param productId Id del Funko
     * @param image     Imagen
     * @return Vista index
     * @throws IOException excepción
     */
    @PostMapping("/update-image/{id}")
    public String updateProductImage(@PathVariable("id") String productId, @RequestParam("image") MultipartFile image)
            throws IOException {
        log.info("Update POST con imagen");
        funkoService.updateImage(productId, image, true);
        return "redirect:/funko";
    }

    /**
     * Método para obtener la fecha localizada
     *
     * @param date   Fecha
     * @param locale Localización
     * @return Fecha localizada
     */
    private String getLocalizedDate(Date date, Locale locale) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withLocale(locale);
        return localDateTime.format(formatter);
    }

    /**
     * Método para comprobar si ha hecho login
     *
     * @param session Sesión
     * @return true si ha hecho login
     */
    private boolean isLoggedAndSessionIsActive(HttpSession session) {
        log.info("Comprobando si ha hecho login");
        UserStore sessionData = (UserStore) session.getAttribute("userSession");
        return sessionData != null && sessionData.isLogged();
    }
}