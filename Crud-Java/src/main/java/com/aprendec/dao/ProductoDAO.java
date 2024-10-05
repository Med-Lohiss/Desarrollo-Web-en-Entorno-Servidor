package com.aprendec.dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
import com.aprendec.conexion.Conexion;
import com.aprendec.model.Producto;

/**
 * Clase que gestiona las operaciones CRUD para los productos.
 */
public class ProductoDAO {
	
 private Connection connection;
 private PreparedStatement statement;
 private boolean estadoOperacion;
 
 /**
  * Verifica si un producto existe en la base de datos.
  * 
  * @param id     ID del producto (puede ser 0 si solo se verifica por nombre).
  * @param nombre Nombre del producto a verificar.
  * @return true si el producto existe, false si no.
  * @throws SQLException si ocurre un error de SQL.
  */
public boolean existeProducto(int id, String nombre) throws SQLException {
	
  String sql = "SELECT COUNT(*) FROM productos WHERE id = ? OR nombre = ?";
  try (Connection conn = obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
	  
      stmt.setInt(1, id);
      stmt.setString(2, nombre);
      ResultSet rs = stmt.executeQuery();
      
      if (rs.next()) {
    	  
          return rs.getInt(1) > 0; // Devuelve true si el producto existe
          
      }
  }
  
  return false; // Si no existe, devuelve false
  
}

 
/**
 * Guarda un nuevo producto en la base de datos.
 * 
 * @param producto el producto a guardar.
 * @return true si la operación fue exitosa, false en caso contrario.
 * @throws SQLException si ocurre un error de SQL.
 */
 public boolean guardar(Producto producto) throws SQLException {
	 
  String sql = null;
  estadoOperacion = false;
  connection = obtenerConexion();
 
  try {
	  
   connection.setAutoCommit(false);
   sql = "INSERT INTO productos (id, nombre, cantidad, precio, fechaCrear,fechaActualizar) VALUES(?,?,?,?,?,?)";
   statement = connection.prepareStatement(sql);
 
   statement.setString(1, null);
   statement.setString(2, producto.getNombre());
   statement.setDouble(3, producto.getCantidad());
   statement.setDouble(4, producto.getPrecio());
   statement.setTimestamp(5, producto.getFechaCrear()); // Cambiado a setTimestamp
   statement.setTimestamp(6, producto.getFechaActualizar()); // Cambiado a setTimestamp
 
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
  * Método para editar un producto en la base de datos.
  *
  * @param producto es el producto a editar.
  * @return true si la operación de actualización fue exitosa; false de lo contrario.
  * @throws SQLException si ocurre un error relacionado con la base de datos durante la ejecución de la consulta.
  */
 public boolean editar(Producto producto) throws SQLException {
	 
  String sql = null;
  estadoOperacion = false;
  connection = obtenerConexion();
  
  try {
	  
   connection.setAutoCommit(false);
   sql = "UPDATE productos SET nombre=?, cantidad=?, precio=?, fechaActualizar=? WHERE id=?";
   statement = connection.prepareStatement(sql);
 
   statement.setString(1, producto.getNombre());
   statement.setDouble(2, producto.getCantidad());
   statement.setDouble(3, producto.getPrecio());
   statement.setTimestamp(4, producto.getFechaActualizar()); // Cambiado a setTimestamp // fechaActualizar
   statement.setInt(5, producto.getId());
 
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
  * Elimina un producto de la base de datos.
  * 
  * @param idProducto es el id del producto a eliminar.
  * @return true si la operación fue exitosa, false en caso contrario.
  * @throws SQLException si ocurre un error de SQL.
  */
 public boolean eliminar(int idProducto) throws SQLException {
	 
  String sql = null;
  estadoOperacion = false;
  connection = obtenerConexion();
  
  try {
	  
   connection.setAutoCommit(false);
   sql = "DELETE FROM productos WHERE id=?";
   statement = connection.prepareStatement(sql);
   statement.setInt(1, idProducto);
 
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
  * Obtiene una lista de todos los productos almacenados en la base de datos.
  * 
  * @return una lista de objetos que contiene todos los productos.
  *         Si no se encuentran productos, devuelve una lista vacía.
  *         
  * @throws SQLException si ocurre un error durante la consulta a la base de datos.
  */ 
 public List<Producto> obtenerProductos(String orden, String direccion) throws SQLException {
	 
     ResultSet resultSet = null;
     List<Producto> listaProductos = new ArrayList<>();

     String sql = "SELECT * FROM productos ORDER BY " + orden + " " + direccion; // Inyección del campo y la dirección de ordenación
     estadoOperacion = false;
     connection = obtenerConexion();

     try {
    	 
         statement = connection.prepareStatement(sql);
         resultSet = statement.executeQuery();
         while (resultSet.next()) {
             Producto p = new Producto();
             p.setId(resultSet.getInt(1));
             p.setNombre(resultSet.getString(2));
             p.setCantidad(resultSet.getDouble(3));
             p.setPrecio(resultSet.getDouble(4));
             p.setFechaCrear(resultSet.getTimestamp(5)); 
             p.setFechaActualizar(resultSet.getTimestamp(6)); 
             listaProductos.add(p);
             
         }

     } catch (SQLException e) {
    	 
         e.printStackTrace();
         
     } finally {
    	 
         statement.close();
         connection.close();
     }

     return listaProductos;
 }
 
 /**
  * Obtiene un producto específico de la base de datos a partir de su ID.
  * 
  * @param idProducto el ID del producto a buscar.
  * @return un objeto que representa el producto correspondiente al ID proporcionado.
  *         Si no se encuentra un producto con ese ID, devuelve un producto vacío.
  *         
  * @throws SQLException si ocurre un error durante la consulta a la base de datos.
  */
 public Producto obtenerProducto(int idProducto) throws SQLException {
	 
  ResultSet resultSet = null;
  Producto p = new Producto();
 
  String sql = null;
  estadoOperacion = false;
  connection = obtenerConexion();
 
  try {
	  
   sql = "SELECT * FROM productos WHERE id =?";
   statement = connection.prepareStatement(sql);
   statement.setInt(1, idProducto);
 
   resultSet = statement.executeQuery();
 
   if (resultSet.next()) {
	   
    p.setId(resultSet.getInt(1));
    p.setNombre(resultSet.getString(2));
    p.setCantidad(resultSet.getDouble(3));
    p.setPrecio(resultSet.getDouble(4));
    p.setFechaCrear(resultSet.getTimestamp(5)); // Cambiado a getTimestamp
    p.setFechaActualizar(resultSet.getTimestamp(6)); // Cambiado a getTimestamp
    
   }
 
  } catch (SQLException e) {
	  
   e.printStackTrace();
   
  }
 
  return p;
  
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