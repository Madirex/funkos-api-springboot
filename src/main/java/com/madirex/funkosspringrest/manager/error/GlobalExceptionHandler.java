package com.madirex.funkosspringrest.manager.error;

import com.madirex.funkosspringrest.manager.error.exceptions.ResponseException;
import com.madirex.funkosspringrest.manager.error.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Manejador de excepciones globales
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Método para manejar las excepciones de validación
     *
     * @param ex Excepción
     * @return Mapa con los errores
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("code", HttpStatus.BAD_REQUEST.value());
        response.put("errors", errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Método para manejar las excepciones que heredan de ResponseException
     *
     * @param ex Excepción
     * @return ResponseEntity con el código de estado
     */
    @ExceptionHandler(ResponseException.class)
    public ResponseEntity<ErrorResponse> handleException(ResponseException ex) {
        var errorResponse = new ErrorResponse(
                ex.getHttpStatus(),
                ex.getMessage(),
                getCurrentHttpRequest().getRequestURI()
        );
        return ResponseEntity.status(ex.getHttpStatus()).body(errorResponse);
    }

    /**
     * Manejador de excepciones de tamaño de fichero excedido
     *
     * @param ex Excepción
     * @return Respuesta HTTP
     */
    @ExceptionHandler(FileSizeLimitExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<ErrorResponse> handleFileSizeLimitExceeded(FileSizeLimitExceededException ex) {
        var errorResponse = new ErrorResponse(
                HttpStatus.PAYLOAD_TOO_LARGE,
                "El tamaño del archivo excede el límite permitido. Máximo permitido: " + ex.getPermittedSize(),
                getCurrentHttpRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(errorResponse);
    }

    /**
     * Manejador de excepciones de referencia a una propiedad no existente
     *
     * @param ex Excepción
     * @return Respuesta HTTP
     */
    @ExceptionHandler(PropertyReferenceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlePropertyReferenceException(PropertyReferenceException ex) {
        var errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Error al procesar la propiedad en la consulta: " + ex.getPropertyName(),
                getCurrentHttpRequest().getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * Método para obtener la petición HTTP actual
     *
     * @return HttpServletRequest
     */
    private HttpServletRequest getCurrentHttpRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getRequest();
    }
}
