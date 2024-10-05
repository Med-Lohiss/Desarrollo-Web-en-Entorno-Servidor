package com.aprendec.conexion;
 
import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

/**
 * Clase encargada de gestionar la conexión con la base de datos.
 */
public class Conexion {
	
 private static BasicDataSource dataSource = null;
 
 /**
  * Este método inicializa el pool de conexiones si aún no está creado.
  *
  * @return un objeto {@link DataSource} configurado para la base de datos MySQL.
  */
 private static DataSource getDataSource() {
	 
  if (dataSource == null) {
	  
   dataSource = new BasicDataSource();
   dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
   dataSource.setUsername("company");
   dataSource.setPassword("company");
   dataSource.setUrl("jdbc:mysql://localhost:3306/crud?useTimezone=true&serverTimezone=UTC");
   dataSource.setInitialSize(20);
   dataSource.setMaxIdle(15);
   dataSource.setMaxTotal(20);
   dataSource.setMaxWaitMillis(5000);
   
  }
  
  return dataSource;
  
 }
 
 /**
  * Método público que devuelve una conexión del pool de conexiones.
  *
  * @return un objeto para interactuar con la base de datos.
  * @throws SQLException si ocurre un error al intentar obtener la conexión del pool.
  */
 public static Connection getConnection() throws SQLException {
	 
  return getDataSource().getConnection();
  
 }
}