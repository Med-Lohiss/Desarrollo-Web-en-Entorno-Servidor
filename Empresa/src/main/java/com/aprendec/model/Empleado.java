package com.aprendec.model;

import com.aprendec.exception.*;

/**
 * Clase que representa a un empleado 
 * 
 * @author Mohamed
 * @version 1.0
 */
public class Empleado extends Persona {

    private int categoria;
    private int anyos; // debe ser positivo
    
    /**
     * Constructor vacío para crear un empleado sin pasar parámetros
     */
    public Empleado() {
        super("", 'M');  // Llama al constructor de la clase Persona con valores por defecto
        this.categoria = 1;
        this.anyos = 0;
    }

    /**
     * Constructor que inicializa un empleado con todos sus atributos.
     * 
     * @param nombre   El nombre del empleado.
     * @param dni      El DNI del empleado.
     * @param sexo     El sexo del empleado, 'M' o 'F'.
     * @param categoria La categoría del empleado, un valor entre 1 y 10.
     * @param anyos    Los años trabajados por el empleado, un valor positivo.
     * 
     * @throws DatosNoCorrectosException Si la categoría no está entre 1 y 10 o los años trabajados son negativos.
     */
    public Empleado(String nombre, String dni, char sexo, int categoria, int anyos) throws DatosNoCorrectosException {
        super(nombre, dni, sexo);
        setCategoria(categoria);
        if (anyos < 0) {
            throw new DatosNoCorrectosException("Los años trabajados no pueden ser negativos");
        }
        this.anyos = anyos;
    }

    /**
     * Constructor que inicializa un empleado con nombre, DNI y sexo. 
     * Asigna por defecto la categoría 1 y los años trabajados a 0.
     * 
     * @param nombre El nombre del empleado.
     * @param dni    El DNI del empleado.
     * @param sexo   El sexo del empleado, 'M' o 'F'.
     * 
     * @throws DatosNoCorrectosException Si los datos proporcionados no son válidos.
     */
    public Empleado(String nombre, String dni, char sexo) throws DatosNoCorrectosException {
        super(nombre, dni, sexo);
        this.categoria = 1;
        this.anyos = 0;
    }

    /**
     * Método para cambiar la categoría del empleado.
     * 
     * @param categoria La nueva categoría, debe estar entre 1 y 10.
     * 
     * @throws DatosNoCorrectosException Si la categoría no está en el rango de 1 a 10.
     */
    public void setCategoria(int categoria) throws DatosNoCorrectosException {
        if (categoria < 1 || categoria > 10) {
            throw new DatosNoCorrectosException("La categoría debe estar entre 1 y 10");
        }
        this.categoria = categoria;
    }

    /**
     * Método para obtener la categoría actual del empleado.
     * 
     * @return La categoría del empleado, un valor entre 1 y 10.
     */
    public int getCategoria() {
        return categoria;
    }

    public int getAnyos() {
        return anyos;
    }

    public void setAnyos(int anyos) {
        this.anyos = anyos;
    }

    /**
     * Método para incrementar en 1 los años trabajados del empleado.
     */
    public void incrAnyo() {
        anyos++;
    }

    /**
     * Método que imprime todos los datos del empleado, incluyendo nombre, DNI, sexo,
     * categoría y años trabajados.
     */
    @Override
    public void imprime() {
        super.imprime();
        System.out.println("Categoría: " + categoria + ", Años trabajados: " + anyos);
    }
}
