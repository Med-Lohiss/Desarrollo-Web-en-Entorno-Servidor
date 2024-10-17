package com.aprendec.conexion;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

/**
 * Clase que maneja la conexión a la base de datos utilizando un pool de conexiones.
 */
public class Conexion {
	
    /** Pool de conexiones de la base de datos. */
	private static BasicDataSource dataSource = null;
	 
	/**
     * Obtiene la fuente de datos para la conexión a la base de datos.
     * 
     * @return un objeto DataSource que contiene las configuraciones del pool de conexiones.
     */
	 private static DataSource getDataSource() {
	  if (dataSource == null) {
	   dataSource = new BasicDataSource();
	   dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	   dataSource.setUsername("company");
	   dataSource.setPassword("company");
	   dataSource.setUrl("jdbc:mysql://localhost:3306/company_db?useTimezone=true&serverTimezone=UTC");
	   dataSource.setInitialSize(20);
	   dataSource.setMaxIdle(15);
	   dataSource.setMaxTotal(20);
	  }
	  return dataSource;
	 }
	 
	 /**
	     * Obtiene una conexión a la base de datos desde el pool de conexiones.
	     * 
	     * @return un objeto Connection para interactuar con la base de datos.
	     * @throws SQLException si ocurre un error al obtener la conexión.
	     */
	 public static Connection getConnection() throws SQLException {
	  return getDataSource().getConnection();
	 }
}