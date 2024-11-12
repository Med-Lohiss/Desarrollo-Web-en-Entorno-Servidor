package com.aprendec.exception;

/**
 * Excepción personalizada para manejar errores relacionados con datos incorrectos.
 */
public class DatosNoCorrectosException extends Exception {

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