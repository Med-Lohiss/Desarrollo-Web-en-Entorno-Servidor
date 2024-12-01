package com.aprendec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal para iniciar la aplicación Spring Boot.
 * Esta clase contiene el método main que ejecuta la aplicación Spring Boot.
 * Se utiliza para arrancar la aplicación y configurar los componentes necesarios.
 */
@SpringBootApplication
public class EmpresaSpringBootApplication {

    /**
     * Método principal para ejecutar la aplicación Spring Boot.
     *
     * @param args Argumentos de la línea de comandos, si los hubiera.
     */
    public static void main(String[] args) {
        SpringApplication.run(EmpresaSpringBootApplication.class, args);
    }
}
