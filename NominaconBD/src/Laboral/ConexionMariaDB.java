package Laboral;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase para manejar la conexión a la base de datos MariaDB.
 * Proporciona un método para obtener una conexión a la base de datos
 * especificada.
 * 
 * @author Mohamed
 * @version 1.0
 */
public class ConexionMariaDB {

    /**
     * Nombre de usuario para la conexión a la base de datos.
     */
    private static final String USER = "company";

    /**
     * Contraseña para la conexión a la base de datos.
     */
    private static final String PASS = "company";

    /**
     * Nombre de la base de datos a la que se conectará.
     */
    private static final String DB_NAME = "company_db";

    /**
     * URL completa de la base de datos para la conexión JDBC.
     */
    private static final String CONN_URL = "jdbc:mariadb://localhost:3306/" + DB_NAME;

    /**
     * Obtiene una conexión activa a la base de datos MariaDB usando los parámetros
     * predefinidos. 
     * @return Una conexión activa a la base de datos MariaDB.
     * @throws SQLException Si ocurre algún error durante la conexión a la base de datos
     * @see Connection
     */
    public static Connection obtenerConexion() throws SQLException {
        Connection conn = DriverManager.getConnection(CONN_URL, USER, PASS);
        return conn;
    }
}

