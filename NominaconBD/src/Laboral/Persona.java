package Laboral;

/**
 * Clase que define los atributos básicos de una persona
 */
public class Persona {

    public String nombre;

    public String dni;

    public char sexo;

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

    /**
     * Método para establecer el DNI de la persona.
     * 
     * @param dni de la persona.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Método para imprimir el nombre y el DNI de la persona.
     */
    public void imprime() {
        System.out.println("Nombre: " + nombre + ", DNI: " + dni);
    }
}

