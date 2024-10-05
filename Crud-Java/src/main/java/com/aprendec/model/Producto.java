package com.aprendec.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;


/**
 * Clase que representa un producto con sus atributos y métodos.
 */
public class Producto {

    private int id;
    private String nombre;
    private double cantidad;
    private double precio;
    private Timestamp fechaCrear;  // Cambiado de Date a Timestamp
    private Timestamp fechaActualizar;  // Cambiado de Date a Timestamp

    // Formato deseado para las fechas
    private static final SimpleDateFormat FORMATO_FECHA = new SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy, h:mm:ss a");

    /**
     * Constructor con todos los atributos del producto.
     * 
     * @param id             ID del producto.
     * @param nombre         Nombre del producto.
     * @param cantidad       Cantidad del producto.
     * @param precio         Precio del producto.
     * @param fechaCrear     Fecha de creación del producto.
     * @param fechaActualizar Fecha de última actualización del producto.
     */
    public Producto(int id, String nombre, double cantidad, double precio, Timestamp fechaCrear, Timestamp fechaActualizar) {
    	
        super();
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precio = precio;
        this.fechaCrear = fechaCrear;
        this.fechaActualizar = fechaActualizar;
        
    }

    // Constructor vacío
    public Producto() {
    	
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Timestamp getFechaCrear() {
        return fechaCrear;
    }

    public void setFechaCrear(Timestamp timestamp) {  // Cambiado a Timestamp
        this.fechaCrear = timestamp;
    }

    public Timestamp getFechaActualizar() {
        return fechaActualizar;
    }

    public void setFechaActualizar(Timestamp timestamp) {  // Cambiado a Timestamp
        this.fechaActualizar = timestamp;
    }

    /**
     * Formatea la fecha de creación para ser más legible.
     * 
     * @return la fecha de creación formateada.
     */
    public String getFechaCrearFormateada() {
    	
        if (fechaCrear != null) {
            return FORMATO_FECHA.format(fechaCrear);
            
        }
        
        return null;
        
    }

    /**
     * Formatea la fecha de última actualización para ser más legible.
     * 
     * @return la fecha de actualización formateada.
     */
    public String getFechaActualizarFormateada() {
    	
        if (fechaActualizar != null) {
        	
            return FORMATO_FECHA.format(fechaActualizar);
            
        }
        
        return null;
    }

    @Override
    public String toString() {
    	
        return "Producto [id=" + id + ", nombre=" + nombre + ", cantidad=" + cantidad + ", precio=" + precio
                + ", fechaCrear=" + getFechaCrearFormateada() + ", fechaActualizar=" + getFechaActualizarFormateada() + "]";
        
    }
}
