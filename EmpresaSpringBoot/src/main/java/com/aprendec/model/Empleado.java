package com.aprendec.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Clase que representa a un empleado.
 */
@Entity
@Data
@Table(name = "empleados")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true) // Incluye los atributos de Persona en equals y hashCode
public class Empleado extends Persona {

    @NotNull(message = "La categoría no puede ser nula")
    @Min(value = 1, message = "La categoría debe ser mayor o igual a 1")
    @Max(value = 10, message = "La categoría debe ser menor o igual a 10")
    @Column(nullable = false,name = "categoria" )
    private Integer categoria;

    @NotNull(message = "Los años trabajados no pueden ser nulos")
    @Positive(message = "Los años trabajados deben ser positivos")
    @Column(nullable = false,name = "anyos" )
    private Integer anyos;

    /**
     * Constructor que inicializa un empleado con todos sus atributos.
     * 
     * @param nombre    El nombre del empleado.
     * @param dni       El DNI del empleado.
     * @param sexo      El sexo del empleado, 'M' o 'F'.
     * @param categoria La categoría del empleado, un valor entre 1 y 10.
     * @param anyos     Los años trabajados por el empleado, un valor positivo.
     */
    public Empleado(String nombre, String dni, char sexo, int categoria, int anyos) {
        super(nombre, dni, sexo);
        this.categoria = categoria;
        this.anyos = anyos;
    }

    /**
     * Método para incrementar en 1 los años trabajados del empleado.
     */
    public void incrAnyo() {
        this.anyos++;
    }
}
