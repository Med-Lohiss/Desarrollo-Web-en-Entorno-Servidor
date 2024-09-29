package Laboral;

import java.io.*;
import java.sql.*;
import java.util.Scanner;

/**
 * Clase principal que gestiona la calculadora de nóminas de empleados y permite mostrar, insertar, actualizar 
 * y eliminar empleados tanto en una base de datos como en un archivo de texto.
 * 
 * @author Mohamed
 * @version 1.2.0
 */
public class CalculaNominas {

    private static final Scanner scanner = new Scanner(System.in);
    private static final String ARCHIVO_EMPLEADOS = "empleados.txt";

    /**
     * Muestra todos los empleados almacenados en la base de datos.
     * 
     * @throws SQLException si ocurre un error en la base de datos
     */
    private static void mostrarEmpleados() {
    	
        try (Connection conn = ConexionMariaDB.obtenerConexion()) {
        	
            String sql = "SELECT * FROM Empleados";
            
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            	
                while (rs.next()) {
                    String dni = rs.getString("dni");
                    String nombre = rs.getString("nombre");
                    char sexo = rs.getString("sexo").charAt(0);
                    int categoria = rs.getInt("categoria");
                    int anyos = rs.getInt("anyos");
                    System.out.println("Nombre: " + nombre + ", DNI: " + dni + ", Sexo: " + sexo + ", Categoría: " + categoria + ", Años trabajados: " + anyos+"\n");
                }
            }
        } catch (SQLException e) {
        	
            System.out.println("Error en la base de datos: " + e.getMessage());
        }
    }

    /**
     * Muestra el salario de un empleado según su DNI.
     * 
     * @throws SQLException si ocurre un error en la base de datos
     */
    private static void mostrarSalarioPorDni() {
    	
        System.out.print("Introduce el DNI del empleado: ");
        String dni = scanner.nextLine();
        
        try (Connection conn = ConexionMariaDB.obtenerConexion()) {
        	
            String sql = "SELECT * FROM Empleados WHERE dni = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            	
                stmt.setString(1, dni);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                    	
                        String nombre = rs.getString("nombre");
                        char sexo = rs.getString("sexo").charAt(0);
                        int categoria = rs.getInt("categoria");
                        int anyos = rs.getInt("anyos");
                        System.out.println("Nombre: " + nombre + ", DNI: " + dni + ", Sexo: " + sexo + ", Categoría: " + categoria + ", Años trabajados: " + anyos);

                        Empleado empleado = new Empleado(nombre, dni, sexo, categoria, anyos);
                        System.out.println("Sueldo: " + Nomina.sueldo(empleado) + "\n");
                        
                    } else {
                    	
                        System.out.println("Empleado no encontrado.");
                    }
                }
            }
        } catch (SQLException | DatosNoCorrectosException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Inserta un nuevo empleado en la base de datos y lo agrega al archivo empleados.txt.
     * 
     * @throws SQLException si ocurre un error al insertar en la base de datos
     */
    private static void insertarNuevoEmpleado() {
    	
        System.out.print("Introduce el nombre del empleado: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Introduce el DNI del empleado: ");
        String dni = scanner.nextLine();
        
        System.out.print("Introduce el sexo del empleado (M/F): ");
        char sexo = scanner.nextLine().charAt(0);
        
        System.out.print("Introduce la categoría del empleado (número): ");
        int categoria = scanner.nextInt();
        
        System.out.print("Introduce los años trabajados del empleado: ");
        int anyos = scanner.nextInt();
        
        scanner.nextLine(); // Limpiar el buffer

        try {
        	
            Empleado nuevoEmpleado = new Empleado(nombre, dni, sexo, categoria, anyos);
            insertarEmpleadoEnBD(nuevoEmpleado);
            agregarEmpleadoAArchivo(nuevoEmpleado); // Sincronizar con empleados.txt
            System.out.println("Empleado insertado correctamente.");
            
        } catch (SQLException | DatosNoCorrectosException e) {
        	
            System.out.println("Error al insertar el empleado: " + e.getMessage());
            
        }
    }

    /**
     * Inserta un empleado en la base de datos.
     * 
     * @param empleado El objeto Empleado a insertar
     * @throws SQLException si ocurre un error al insertar en la base de datos
     */
    private static void insertarEmpleadoEnBD(Empleado empleado) throws SQLException {
    	
        try (Connection conn = ConexionMariaDB.obtenerConexion()) {
        	
            String sql = "INSERT INTO Empleados (dni, nombre, sexo, categoria, anyos) VALUES (?, ?, ?, ?, ?)";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            	
                stmt.setString(1, empleado.dni);
                stmt.setString(2, empleado.nombre);
                stmt.setString(3, String.valueOf(empleado.sexo));
                stmt.setInt(4, empleado.getCategoria());
                stmt.setInt(5, empleado.anyos);
                stmt.executeUpdate();
            }
        }
    }

    /**
     * Lee el archivo empleados.txt y añade los empleados a la base de datos y nóminas.
     * 
     * @throws SQLException si ocurre un error en la base de datos
     */
    private static void leerYAgregarEmpleadosDesdeArchivo() {
    	
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_EMPLEADOS))) {
        	
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(";");
                if (partes.length == 5) {
                    String nombre = partes[0];
                    String dni = partes[1];
                    char sexo = partes[2].charAt(0);
                    int categoria = Integer.parseInt(partes[3]);
                    int anyos = Integer.parseInt(partes[4]);

                    Empleado empleado = new Empleado(nombre, dni, sexo, categoria, anyos);
                    insertarEmpleadoEnBD(empleado);
                }
            }
            
            System.out.println("Empleados agregados desde el archivo.");
            
        } catch (IOException | SQLException | DatosNoCorrectosException e) {
        	
            System.out.println("Error al leer o agregar empleados desde el archivo: " + e.getMessage());
        }
    }

    /**
     * Actualiza el sueldo de un empleado específico según su DNI y sincroniza con empleados.txt.
     * 
     * @throws SQLException si ocurre un error en la base de datos
     */
    private static void actualizarSueldo() {
    	
        System.out.print("Introduce el DNI del empleado: ");
        String dni = scanner.nextLine();
        
        try (Connection conn = ConexionMariaDB.obtenerConexion()) {
        	
            String sql = "SELECT * FROM Empleados WHERE dni = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            	
                stmt.setString(1, dni);
                
                try (ResultSet rs = stmt.executeQuery()) {
                	
                    if (rs.next()) {
                    	
                        String nombre = rs.getString("nombre");
                        char sexo = rs.getString("sexo").charAt(0);
                        int categoria = rs.getInt("categoria");
                        int anyos = rs.getInt("anyos");

                        Empleado empleado = new Empleado(nombre, dni, sexo, categoria, anyos);
                        int sueldo = Nomina.sueldo(empleado);
                        System.out.println("Sueldo recalculado: " + sueldo + "\n");

                        actualizarEmpleadoEnArchivo();
                        
                    } else {
                    	
                        System.out.println("Empleado no encontrado.");
                        
                    }
                }
            }
        } catch (SQLException | DatosNoCorrectosException e) {
        	
            System.out.println("Error al actualizar el sueldo: " + e.getMessage());
            
        }
    }

    /**
     * Actualiza los sueldos de todos los empleados y sincroniza el archivo empleados.txt.
     * 
     * @throws SQLException si ocurre un error en la base de datos
     */
    private static void actualizarTodosLosSueldos() {
    	
        try (Connection conn = ConexionMariaDB.obtenerConexion()) {
        	
            String sql = "SELECT * FROM Empleados";
            
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            	
                while (rs.next()) {
                	
                    String dni = rs.getString("dni");
                    String nombre = rs.getString("nombre");
                    char sexo = rs.getString("sexo").charAt(0);
                    int categoria = rs.getInt("categoria");
                    int anyos = rs.getInt("anyos");

                    Empleado empleado = new Empleado(nombre, dni, sexo, categoria, anyos);
                    int sueldo = Nomina.sueldo(empleado);
                    System.out.println("Sueldo recalculado de " + nombre + " (DNI: " + dni + "): " + sueldo);
                    
                }
                
                actualizarEmpleadoEnArchivo();
                
            }
        } catch (SQLException | DatosNoCorrectosException e) {
        	
            System.out.println("Error al actualizar los sueldos: " + e.getMessage());
            
        }
    }

    /**
     * Elimina un empleado de la base de datos y del archivo empleados.txt.
     * 
     * @throws SQLException si ocurre un error en la base de datos
     */
    private static void eliminarEmpleado() {
    	
        System.out.print("Introduce el DNI del empleado a eliminar: ");
        String dni = scanner.nextLine();
        
        try (Connection conn = ConexionMariaDB.obtenerConexion()) {
        	
            String sql = "DELETE FROM Empleados WHERE dni = ?";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            	
                stmt.setString(1, dni);
                int filasAfectadas = stmt.executeUpdate();
                if (filasAfectadas > 0) {
                    System.out.println("Empleado eliminado correctamente.");
                    actualizarEmpleadoEnArchivo();
                    
                } else {
                	
                    System.out.println("Empleado no encontrado.");
                }
            }
        } catch (SQLException e) {
        	
            System.out.println("Error al eliminar el empleado: " + e.getMessage());
            
        }
    }

    /**
     * Actualiza el archivo empleados.txt con los datos actuales de la base de datos.
     * 
     * @throws SQLException si ocurre un error en la base de datos
     */
    private static void actualizarEmpleadoEnArchivo() {
    	
        try (Connection conn = ConexionMariaDB.obtenerConexion();
             BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_EMPLEADOS))) {

            String sql = "SELECT * FROM Empleados";
            
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            	
                while (rs.next()) {
                    String nombre = rs.getString("nombre");
                    String dni = rs.getString("dni");
                    char sexo = rs.getString("sexo").charAt(0);
                    int categoria = rs.getInt("categoria");
                    int anyos = rs.getInt("anyos");

                    // Escribir en el archivo
                    writer.write(nombre + ";" + dni + ";" + sexo + ";" + categoria + ";" + anyos);
                    writer.newLine();
                    
                }
            }
        } catch (SQLException | IOException e) {
        	
            System.out.println("Error al actualizar el archivo de empleados: " + e.getMessage());
            
        }
    }

    /**
     * Agrega un empleado al archivo empleados.txt.
     * 
     * @param empleado El objeto Empleado a agregar
     * @throws IOException si ocurre un error al escribir en el archivo
     */
    private static void agregarEmpleadoAArchivo(Empleado empleado) {
    	
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_EMPLEADOS, true))) {
            writer.write(empleado.nombre + ";" + empleado.dni + ";" + empleado.sexo + ";" + empleado.getCategoria() + ";" + empleado.anyos);
            writer.newLine();
            
        } catch (IOException e) {
        	
            System.out.println("Error al agregar el empleado al archivo: " + e.getMessage());
            
        }
    }

    /**
     * Menú principal del programa.
     */
    public static void main(String[] args) {
    	
        int opcion;
        do {
        	
            System.out.println("Menú:");
            System.out.println("1. Mostrar los datos de los empleados");
            System.out.println("2. Mostrar salario por DNI");
            System.out.println("3. Insertar nuevo empleado");
            System.out.println("4. Agregar empleados desde archivo");
            System.out.println("5. Actualizar sueldo de un empleado");
            System.out.println("6. Actualizar sueldos de todos los empleados");
            System.out.println("7. Eliminar empleado por DNI");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
            
                case 1:
                    mostrarEmpleados();
                    break;
                case 2:
                    mostrarSalarioPorDni();
                    break;
                case 3:
                    insertarNuevoEmpleado();
                    break;
                case 4:
                    leerYAgregarEmpleadosDesdeArchivo();
                    break;
                case 5:
                    actualizarSueldo();
                    break;
                case 6:
                    actualizarTodosLosSueldos();
                    break;
                case 7:
                    eliminarEmpleado();
                    break;
                case 0:
                    System.out.println("Saliendo...Adiosss!!!!");
                    break;
                default:
                    System.out.println("Opción incorrecta. Intenta de nuevo.");
            }
        } while (opcion != 0);

        scanner.close();
    }
}
