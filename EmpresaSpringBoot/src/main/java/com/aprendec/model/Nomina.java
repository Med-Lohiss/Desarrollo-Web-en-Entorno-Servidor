package com.aprendec.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Representa una nómina asociada a un empleado.
 * La clase {@link Nomina} almacena el sueldo de un empleado junto con su DNI.
 * La relación entre la nómina y el empleado es de tipo {@link ManyToOne}.
 */
@Entity
@Data
@Table(name = "nominas")
public class Nomina {

    /**
     * El DNI del empleado, que debe ser único.
     * Este campo es la clave primaria de la tabla "nominas" y está relacionado con la
     * entidad {@link Empleado}.
     */
    @Id
    private String dni;  // DNI del empleado (debería ser único)
    
    /**
     * El sueldo calculado del empleado, almacenado como un valor entero.
     * Representa el salario mensual o el sueldo base del empleado.
     */
    private int sueldo;  // Sueldo calculado del empleado
    
    /**
     * Relación de muchos a uno con la entidad {@link Empleado}.
     * Un empleado puede tener una nómina asociada.
     * Este campo se utiliza para la relación entre las tablas "empleados" y "nominas"
     * usando el campo "dni" como clave foránea.
     */
    @ManyToOne
    @JoinColumn(name = "dni", referencedColumnName = "dni", insertable = false, updatable = false)
    private Empleado empleado;
}

