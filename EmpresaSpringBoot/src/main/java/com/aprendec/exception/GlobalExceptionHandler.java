package com.aprendec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Clase global para el manejo de excepciones en la aplicación.
 * Esta clase captura y maneja excepciones de tipo {@link DatosNoCorrectosException}
 * que se producen en cualquier parte de la aplicación.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de tipo {@link DatosNoCorrectosException}.
     * Se captura cuando los datos proporcionados no son válidos y se devuelve una respuesta
     * HTTP con el mensaje de error correspondiente.
     *
     * @param ex Excepción capturada de tipo {@link DatosNoCorrectosException}.
     * @return ResponseEntity con el código de estado HTTP 400 (BAD_REQUEST) y el mensaje de error.
     */
    @ExceptionHandler(DatosNoCorrectosException.class)
    public ResponseEntity<String> handleDatosNoCorrectosException(DatosNoCorrectosException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Error: " + ex.getMessage());
    }
}
