package com.aprendec.model;

/**
 * Clase que define los atributos básicos de una persona
 */
public class Persona {

    private String nombre;
    private String dni;
    private char sexo;

    /**
     * Constructor de la clase que inicializa el nombre, el DNI y el sexo.
     * 
     * @param nombre El nombre de la persona.
     * @param dni El Documento Nacional de Identidad (DNI) de la persona.
     * @param sexo El sexo de la persona ('M' para masculino, 'F' para femenino).
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
     * @param sexo El sexo de la persona ('M' para masculino, 'F' para femenino).
     */
    public Persona(String nombre, char sexo) {
        this.nombre = nombre;
        this.sexo = sexo;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public char getSexo() {
        return sexo;
    }

    // Setters
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    // Método para imprimir el nombre y el DNI de la persona.
    public void imprime() {
        System.out.println("Nombre: " + nombre + ", DNI: " + dni);
    }
}
