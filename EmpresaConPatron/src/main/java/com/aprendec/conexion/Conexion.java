package com.aprendec.conexion;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

/**
 * Clase que maneja la conexión a la base de datos utilizando un pool de conexiones.
 * Implementa el patrón Singleton para asegurar una única instancia de conexión.
 */
public class Conexion {

    // Única instancia de la clase (Singleton)
    private static Conexion instancia;
    private BasicDataSource dataSource;

    /**
     * Constructor privado para evitar la creación directa de instancias.
     * Inicializa el pool de conexiones.
     */
    private Conexion() {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUsername("company");
        dataSource.setPassword("company");
        dataSource.setUrl("jdbc:mysql://localhost:3306/company_db?useTimezone=true&serverTimezone=UTC");
        dataSource.setInitialSize(20);
        dataSource.setMaxIdle(15);
        dataSource.setMaxTotal(20);
    }

    /**
     * Obtiene la instancia única de la clase (Singleton).
     * Si no existe, la crea.
     *
     * @return la instancia única de Conexion.
     */
    public static Conexion getInstance() {
        if (instancia == null) {
            synchronized (Conexion.class) {
                if (instancia == null) {  // Doble verificación para mayor seguridad
                    instancia = new Conexion();
                }
            }
        }
        return instancia;
    }

    /**
     * Obtiene una conexión a la base de datos desde el pool de conexiones.
     *
     * @return un objeto Connection para interactuar con la base de datos.
     * @throws SQLException si ocurre un error al obtener la conexión.
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
