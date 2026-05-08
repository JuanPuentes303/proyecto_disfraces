package com.disfracesrivera.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "com.disfracesrivera.controller.api")
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> manejarIllegalArgument(IllegalArgumentException e) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        respuesta.put("mensaje", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(respuesta);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(MethodArgumentNotValidException e) {
        Map<String, String> errores = new HashMap<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errores.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        respuesta.put("mensaje", "Hay errores de validación");
        respuesta.put("campos", errores);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(respuesta);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> manejarErrorGeneral(Exception e) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("error", true);
        respuesta.put("mensaje", "Ocurrió un error inesperado");

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(respuesta);
    }
}