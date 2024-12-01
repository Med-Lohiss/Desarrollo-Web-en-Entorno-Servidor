package com.aprendec.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que define los atributos básicos de una persona.
 */
@MappedSuperclass
@Data
@NoArgsConstructor
public class Persona {

    @NotNull(message = "El nombre no puede ser nulo")
    @Size(min = 1, max = 100, message = "El nombre debe tener entre 1 y 100 caracteres")
    @Column(nullable = false, name = "nombre")
    private String nombre;

    @Id
    @NotNull(message = "El DNI no puede ser nulo")
    @Column(unique = true, nullable = false, name = "dni")
    private String dni;

    @NotNull(message = "El sexo no puede ser nulo")
    @Column(name = "sexo")
    private Character sexo;

    /**
     * Constructor que inicializa el nombre, el DNI y el sexo.
     * 
     * @param nombre El nombre de la persona.
     * @param dni    El Documento Nacional de Identidad (DNI) de la persona.
     * @param sexo   El sexo de la persona ('M' para masculino, 'F' para femenino).
     */
    public Persona(String nombre, String dni, char sexo) {
        this.nombre = nombre;
        this.dni = dni;
        this.sexo = sexo;
    }

    /**
     * Constructor alternativo que solo inicializa el nombre y el sexo de la persona.
     * 
     * @param nombre El nombre de la persona.
     * @param sexo   El sexo de la persona ('M' para masculino, 'F' para femenino).
     */
    public Persona(String nombre, char sexo) {
        this.nombre = nombre;
        this.sexo = sexo;
    }
}
