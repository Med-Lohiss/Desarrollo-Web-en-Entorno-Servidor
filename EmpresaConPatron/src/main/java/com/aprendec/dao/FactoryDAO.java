package com.aprendec.dao;

/**
 * Clase que implementa el patrón de diseño Factory para proporcionar instancias de objetos DAO.
 * 
 * Esta clase tiene un método genérico que permite obtener instancias de clases DAO específicas,
 * como {@link EmpleadoDAO}, y extenderlo fácilmente para otros DAOs si es necesario.
 * 
 */
public class FactoryDAO {

    /**
     * Método estático genérico para obtener una instancia del DAO solicitado.
     * 
     * <p>Actualmente soporta el {@link EmpleadoDAO}, pero se puede extender fácilmente
     * para soportar otros DAOs.</p>
     * 
     * @param <T> El tipo de DAO que se desea obtener
     * @param daoClass La clase del DAO que se desea instanciar.
     * @return Una instancia del DAO solicitado.
     * @throws IllegalArgumentException Si la clase del DAO no está soportada.
     */
    public static <T> T getDAO(Class<T> daoClass) {
        if (daoClass == EmpleadoDAO.class) {
            return daoClass.cast(new EmpleadoDAO());
        }
        // else if (daoClass == ProductoDAO.class) {
        // return daoClass.cast(new ProductoDAO());
        // }
        throw new IllegalArgumentException("DAO no soportado para la clase: " + daoClass.getName());
    }
}
