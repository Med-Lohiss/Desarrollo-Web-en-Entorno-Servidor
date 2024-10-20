package com.aprendec.dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
import com.aprendec.conexion.Conexion;
import com.aprendec.exception.DatosNoCorrectosException;
import com.aprendec.model.*;
/**
 * Clase que gestiona las operaciones CRUD para los empleados.
 */
public class EmpleadoDAO {
	
 private static final String LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE"; 
 private Connection connection;
 private PreparedStatement statement;
 private boolean estadoOperacion;
 
 
 public boolean validateDNI(String dni) {
     // Verificar que el string tenga 8 o 9 caracteres
     if (dni.length() < 8 || dni.length() > 9) {
         return false;
     }

     // Extraer los 8 primeros caracteres como número
     int number;
     try {
         number = Integer.parseInt(dni.substring(0, 8));
     } catch (NumberFormatException e) {
         return false; // En caso de que no sea un número válido
     }

     // Calcular la letra final
     char letter = LETTERS.charAt(number % LETTERS.length());

     // Verificar que la letra final coincide con el string
     return dni.charAt(8) == letter;
 }
 
 /**
  * Verifica si un empleado existe en la base de datos.
  * 
  * @param dni     DNI del empleado.
  * @return true si el empleado existe, false si no.
  * @throws SQLException si ocurre un error de SQL.
  */
 public boolean existeEmpleado(String dni) throws SQLException {
	    String sql = "SELECT COUNT(*) FROM empleados WHERE dni = ?";
	    try (Connection conn = obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
	        stmt.setString(1, dni);
	        ResultSet rs = stmt.executeQuery();
	        
	        if (rs.next()) {
	            return rs.getInt(1) > 0; // Devuelve true si el empleado existe
	        }
	    }
	    
	    return false; // Si no existe, devuelve false
	}

 
/**
 * Guarda un nuevo empleado en la base de datos.
 * 
 * @param empleado el empleado a guardar.
 * @return true si la operación fue exitosa, false en caso contrario.
 * @throws SQLException si ocurre un error de SQL.
 * @throws DatosNoCorrectosException 
 */
 public boolean guardar(Empleado empleado) throws SQLException, DatosNoCorrectosException {
	 
	 if (!validateDNI(empleado.getDni())) {
         throw new DatosNoCorrectosException("DNI no válido.");
     }
	 
	    String sqlEmpleado = null;
	    String sqlNomina = null;
	    estadoOperacion = false;
	    connection = obtenerConexion();

	    try {
	        connection.setAutoCommit(false);
	        
	        // Insertar en la tabla empleados
	        sqlEmpleado = "INSERT INTO empleados (dni, nombre, sexo, categoria, anyos) VALUES(?,?,?,?,?)";
	        statement = connection.prepareStatement(sqlEmpleado);

	        statement.setString(1, empleado.getDni());
	        statement.setString(2, empleado.getNombre());
	        statement.setString(3, empleado.getSexo() + "");
	        statement.setInt(4, empleado.getCategoria());
	        statement.setInt(5, empleado.getAnyos());

	        estadoOperacion = statement.executeUpdate() > 0;

	        // Ahora insertamos en la tabla nominas
	        if (estadoOperacion) {
	            sqlNomina = "INSERT INTO nominas (dni, sueldo) VALUES(?,?)";
	            statement = connection.prepareStatement(sqlNomina);

	            // Calcular sueldo usando la clase Nomina
	            int sueldo = Nomina.sueldo(empleado);
	            statement.setString(1, empleado.getDni());
	            statement.setInt(2, sueldo);

	            estadoOperacion = statement.executeUpdate() > 0;
	        }

	        connection.commit();
	        statement.close();
	        connection.close();

	    } catch (SQLException e) {
	        connection.rollback();
	        e.printStackTrace();
	    }

	    return estadoOperacion;
	}

 
 /**
  * Método para editar un empleado en la base de datos.
  *
  * @param empleado es el empleado a editar.
  * @return true si la operación de actualización fue exitosa; false de lo contrario.
  * @throws SQLException si ocurre un error relacionado con la base de datos durante la ejecución de la consulta.
  */
 public boolean editar(Empleado empleado) throws SQLException {
	    String sqlEmpleado = null;
	    String sqlNomina = null;
	    estadoOperacion = false;
	    connection = obtenerConexion();

	    try {
	        connection.setAutoCommit(false);

	        // Actualizar los datos del empleado en la tabla empleados
	        sqlEmpleado = "UPDATE empleados SET nombre=?, sexo=?, categoria=?, anyos=? WHERE dni=?";
	        statement = connection.prepareStatement(sqlEmpleado);
	        statement.setString(1, empleado.getNombre());
	        statement.setString(2, empleado.getSexo() + "");
	        statement.setInt(3, empleado.getCategoria());
	        statement.setInt(4, empleado.getAnyos());
	        statement.setString(5, empleado.getDni());

	        estadoOperacion = statement.executeUpdate() > 0;

	        // Si la actualización del empleado fue exitosa, actualizar la nómina
	        if (estadoOperacion) {
	            // Calcular el nuevo sueldo usando la clase Nomina
	            int nuevoSueldo = Nomina.sueldo(empleado);

	            // Actualizar el sueldo en la tabla nominas
	            sqlNomina = "UPDATE nominas SET sueldo=? WHERE dni=?";
	            statement = connection.prepareStatement(sqlNomina);
	            statement.setInt(1, nuevoSueldo);
	            statement.setString(2, empleado.getDni());

	            estadoOperacion = statement.executeUpdate() > 0;
	        }

	        connection.commit();
	        statement.close();
	        connection.close();

	    } catch (SQLException e) {
	        connection.rollback();
	        e.printStackTrace();
	    }

	    return estadoOperacion;
	}
 
 /**
  * Elimina un empleado de la base de datos.
  * 
  * @param dni es el DNI del empleado a eliminar.
  * @return true si la operación fue exitosa, false en caso contrario.
  * @throws SQLException si ocurre un error de SQL.
  */
 public boolean eliminar(String dni) throws SQLException {
	 
  String sql = null;
  estadoOperacion = false;
  connection = obtenerConexion();
  
  try {
	  
   connection.setAutoCommit(false);
   sql = "DELETE FROM empleados WHERE dni=?";
   statement = connection.prepareStatement(sql);
   statement.setString(1, dni);
 
   estadoOperacion = statement.executeUpdate() > 0;
   connection.commit();
   statement.close();
   connection.close();
 
  } catch (SQLException e) {
	  
   connection.rollback();
   e.printStackTrace();
   
  }
 
  return estadoOperacion;
 }
 
 /**
  * Elimina un empleado de la base de datos.
  *
  * @param dni es el DNI del empleado a eliminar.
  * @return true si la operación fue exitosa, false en caso contrario.
  * @throws SQLException si ocurre un error de SQL.
  */
 public boolean eliminarNominaPorDNI(String dni) throws SQLException {
	    String sql = null;
	    boolean estadoOperacion = false;
	    Connection connection = null;
	    PreparedStatement statement = null;

	    try {
	        // Obtener la conexión
	        connection = obtenerConexion();
	        connection.setAutoCommit(false); // Iniciar transacción

	        // Crear la consulta SQL
	        sql = "DELETE FROM nominas WHERE dni = ?";
	        statement = connection.prepareStatement(sql);
	        statement.setString(1, dni);

	        // Ejecutar la consulta
	        estadoOperacion = statement.executeUpdate() > 0;

	        // Confirmar la transacción
	        connection.commit();
	    } catch (SQLException e) {
	        // Revertir la transacción en caso de error
	        if (connection != null) {
	            connection.rollback();
	        }
	        e.printStackTrace();
	    } finally {
	        // Cerrar los recursos
	        if (statement != null) {
	            statement.close();
	        }
	        if (connection != null) {
	            connection.close();
	        }
	    }

	    return estadoOperacion;
	}

 
 /**
  * Obtiene una lista de todos los empleados almacenados en la base de datos.
  * 
  * @return una lista de objetos que contiene todos los empleados.
  *         Si no se encuentran empleados, devuelve una lista vacía.
  *         
  * @throws SQLException si ocurre un error durante la consulta a la base de datos.
  */ 
 public List<Empleado> obtenerEmpleados(String orden, String direccion) throws SQLException, DatosNoCorrectosException {
	    ResultSet resultSet = null;
	    List<Empleado> listaEmpleados = new ArrayList<>();

	    String sql = "SELECT * FROM empleados ORDER BY " + orden + " " + direccion; 
	    estadoOperacion = false;
	    connection = obtenerConexion();

	    try {
	        statement = connection.prepareStatement(sql);
	        resultSet = statement.executeQuery();
	        while (resultSet.next()) {
	            Empleado em = new Empleado();
	            em.setDni(resultSet.getString(1));
	            em.setNombre(resultSet.getString(2));  // Usa el setter heredado de Persona
	            em.setSexo(resultSet.getString(3).charAt(0));  // El sexo es de tipo char
	            em.setCategoria(resultSet.getInt(4));
	            em.setAnyos(resultSet.getInt(5));
	            listaEmpleados.add(em);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        statement.close();
	        connection.close();
	    }

	    return listaEmpleados;
	}

 
 /**
  * Obtiene un empleado específico de la base de datos a partir de su dni.
  * 
  * @param dni el DNI del empleado a buscar.
  * @return un objeto que representa el empleado correspondiente al dni proporcionado.
  *         Si no se encuentra un empleado con ese DNI, devuelve un empleado vacío.
  *         
  * @throws SQLException si ocurre un error durante la consulta a la base de datos.
  */
 public Empleado obtenerEmpleado(String dni) throws SQLException, DatosNoCorrectosException {
	    ResultSet resultSet = null;
	    Empleado em = null;  // Cambiamos a null para indicar que el empleado no fue encontrado

	    String sql = null;
	    estadoOperacion = false;
	    connection = obtenerConexion();

	    try {
	        sql = "SELECT * FROM empleados WHERE dni = ?";
	        statement = connection.prepareStatement(sql);
	        statement.setString(1, dni);

	        resultSet = statement.executeQuery();

	        if (resultSet.next()) {
	            em = new Empleado(); // Creamos un nuevo objeto solo si encontramos el empleado
	            em.setDni(resultSet.getString(1));
	            em.setNombre(resultSet.getString(2));
	            em.setSexo(resultSet.getString(3).charAt(0));
	            em.setCategoria(resultSet.getInt(4));
	            em.setAnyos(resultSet.getInt(5));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        if (resultSet != null) resultSet.close();
	        if (statement != null) statement.close();
	        if (connection != null) connection.close();
	    }

	    return em; // Retornará null si no se encontró el empleado
	}
 
 /**
  * Busca empleados en la base de datos según criterios opcionales de filtrado.
  *
  * @param sexo      el sexo del empleado a buscar (opcional).
  * @param nombre    el nombre del empleado a buscar (opcional).
  * @param categoria la categoría del empleado a buscar (opcional).
  * @param anyos     los años de experiencia del empleado a buscar (opcional).
  * @return una lista de empleados que coinciden con los criterios de búsqueda.
  * @throws SQLException si ocurre un error durante la consulta.
  */
 public List<Empleado> buscarEmpleados(String sexo, String nombre, String categoria, String anyos) throws SQLException {
	    List<Empleado> empleadosFiltrados = new ArrayList<>();
	    Connection connection = null;
	    PreparedStatement statement = null;
	    ResultSet resultSet = null;

	    StringBuilder sql = new StringBuilder("SELECT * FROM empleados WHERE 1=1");
	    List<Object> parameters = new ArrayList<>();

	    // Añadir condiciones a la consulta
	    if (sexo != null && !sexo.isEmpty()) {
	        sql.append(" AND sexo = ?");
	        parameters.add(sexo);
	    }
	    if (nombre != null && !nombre.isEmpty()) {
	        sql.append(" AND nombre LIKE ?");
	        parameters.add("%" + nombre + "%");
	    }
	    if (categoria != null && !categoria.isEmpty()) {
	        try {
	            int categoriaInt = Integer.parseInt(categoria);
	            sql.append(" AND categoria = ?");
	            parameters.add(categoriaInt);
	        } catch (NumberFormatException e) {
	            throw new SQLException("La categoría debe ser un número entero.");
	        }
	    }
	    if (anyos != null && !anyos.isEmpty()) {
	        try {
	            int anyosInt = Integer.parseInt(anyos);
	            sql.append(" AND anyos = ?");
	            parameters.add(anyosInt);
	        } catch (NumberFormatException e) {
	            throw new SQLException("Los años deben ser un número entero.");
	        }
	    }

	    try {
	        connection = obtenerConexion(); // Método para obtener la conexión
	        statement = connection.prepareStatement(sql.toString());

	        // Asignar parámetros a la consulta
	        for (int i = 0; i < parameters.size(); i++) {
	            statement.setObject(i + 1, parameters.get(i));
	        }

	        resultSet = statement.executeQuery();

	        while (resultSet.next()) {
	            Empleado emp = new Empleado();
	            emp.setDni(resultSet.getString("dni"));
	            emp.setNombre(resultSet.getString("nombre"));
	            emp.setSexo(resultSet.getString("sexo").charAt(0));
	            emp.setCategoria(resultSet.getInt("categoria"));
	            emp.setAnyos(resultSet.getInt("anyos"));
	            empleadosFiltrados.add(emp);
	        }
	    } catch (SQLException | DatosNoCorrectosException e) {
	        // Manejo de errores
	        throw new SQLException("Error al buscar empleados: " + e.getMessage(), e);
	    } finally {
	        // Cerrar recursos en el bloque finally
	        if (resultSet != null) resultSet.close();
	        if (statement != null) statement.close();
	        if (connection != null) connection.close();
	    }

	    return empleadosFiltrados; // Retornar la lista de empleados filtrados
	}


 
 /**
  * Obtiene la conexión desde el pool de conexiones.
  * 
  * @return la conexión establecida.
  * @throws SQLException si ocurre un error de SQL.
  */
 private Connection obtenerConexion() throws SQLException {
	 
  return Conexion.getConnection();
  
 }
 
}