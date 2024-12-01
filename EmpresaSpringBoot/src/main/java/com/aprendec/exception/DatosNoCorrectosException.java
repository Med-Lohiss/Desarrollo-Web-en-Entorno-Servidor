package com.aprendec.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para manejar errores relacionados con datos incorrectos.
 */
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DatosNoCorrectosException extends RuntimeException {

    /**
     * ID de serialización para garantizar la compatibilidad en la deserialización.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Crea una nueva excepción de datos incorrectos con un mensaje detallado.
     * 
     * @param mensaje Descripción del error que causó la excepción.
     */
    public DatosNoCorrectosException(String mensaje) {
        super(mensaje);
    }
}
