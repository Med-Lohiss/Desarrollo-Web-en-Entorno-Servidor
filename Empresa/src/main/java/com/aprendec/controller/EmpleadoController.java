package com.aprendec.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aprendec.dao.EmpleadoDAO;
import com.aprendec.model.Empleado;
import com.aprendec.model.Nomina;
import com.aprendec.exception.DatosNoCorrectosException;

/**
 * Servlet que maneja las peticiones relacionadas con la tabla de empleados.
 */
@WebServlet(description = "administra peticiones para la tabla empleados", urlPatterns = { "/empleados" })
public class EmpleadoController extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor del controlador EmpleadoController.
	 */
	public EmpleadoController() {
		super();
	}

	/**
	 * Maneja las solicitudes GET enviadas al servlet.
	 * 
	 * @param request  la solicitud HTTP.
	 * @param response la respuesta HTTP.
	 * @throws ServletException si ocurre un error en el servlet.
	 * @throws IOException      si ocurre un error de entrada/salida.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");

		String opcion = request.getParameter("opcion");

		if ("crear".equals(opcion)) {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
			requestDispatcher.forward(request, response);
		} else if ("listar".equals(opcion)) {
			String orden = request.getParameter("orden") != null ? request.getParameter("orden") : "nombre";
			String direccion = request.getParameter("direccion") != null ? request.getParameter("direccion") : "asc";

			EmpleadoDAO empleadoDAO = new EmpleadoDAO();
			List<Empleado> lista = new ArrayList<>();

			try {
				lista = empleadoDAO.obtenerEmpleados(orden, direccion);
				request.setAttribute("lista", lista);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
				requestDispatcher.forward(request, response);

			} catch (SQLException | DatosNoCorrectosException e) {
				e.printStackTrace();
				request.setAttribute("mensajeError", "Error al obtener empleados: " + e.getMessage());
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
				requestDispatcher.forward(request, response);
			}

		} else if ("buscar".equals(opcion)) { // Nueva opción para buscar empleados
			String sexo = request.getParameter("sexo");
			String nombre = request.getParameter("nombre");
			String categoria = request.getParameter("categoria");
			String anyos = request.getParameter("anyos");

			EmpleadoDAO empleadoDAO = new EmpleadoDAO();
			List<Empleado> empleadosFiltrados = new ArrayList<>();

			try {
				empleadosFiltrados = empleadoDAO.buscarEmpleados(sexo, nombre, categoria, anyos);
				request.setAttribute("lista", empleadosFiltrados); // Establecer la lista filtrada en el request
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/buscar.jsp");
				requestDispatcher.forward(request, response);

			} catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("mensajeError", "Error al buscar empleados: " + e.getMessage());
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/buscar.jsp");
				requestDispatcher.forward(request, response);
			}

		} else if ("meditar".equals(opcion)) {
			String dni = request.getParameter("dni");
			EmpleadoDAO empleadoDAO = new EmpleadoDAO();
			Empleado em = new Empleado();

			try {
				em = empleadoDAO.obtenerEmpleado(dni);
				request.setAttribute("empleado", em);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
				requestDispatcher.forward(request, response);

			} catch (SQLException | DatosNoCorrectosException e) {
				e.printStackTrace();
				request.setAttribute("mensajeError", "Error al obtener el empleado: " + e.getMessage());
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
				requestDispatcher.forward(request, response);
			}

		} else if ("salario".equals(opcion)) {
			String dni = request.getParameter("dni");
			EmpleadoDAO empleadoDAO = new EmpleadoDAO();

			// Validar si el DNI está vacío
			if (dni == null || dni.trim().isEmpty()) {
				// Redirigir a salario.jsp sin establecer un mensaje de error
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/salario.jsp");
				requestDispatcher.forward(request, response);
				return; // Termina el flujo si el DNI está vacío
			}

			try {
				Empleado empleado = empleadoDAO.obtenerEmpleado(dni);
				if (empleado == null) {
					request.setAttribute("mensajeError", "No se encontró el empleado con DNI: " + dni);
					request.setAttribute("salario", null); // Asegúrate de establecer salario como null
				} else {
					// Si se encuentra el empleado, calculamos el salario
					int salario = Nomina.sueldo(empleado);
					request.setAttribute("empleado", empleado);
					request.setAttribute("salario", salario);
				}
			} catch (SQLException | DatosNoCorrectosException e) {
				e.printStackTrace();
				request.setAttribute("mensajeError", "Error al obtener el salario del empleado: " + e.getMessage());
			}

			// Redirigir a salario.jsp al final para mostrar los resultados
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/salario.jsp");
			requestDispatcher.forward(request, response);

		} else if ("eliminar".equals(opcion)) {
			String dni = request.getParameter("dni");
			EmpleadoDAO empleadoDAO = new EmpleadoDAO();

			try {
				// Primero, elimina los datos de la tabla de nóminas
				empleadoDAO.eliminarNominaPorDNI(dni);
				// Luego, elimina el empleado de la tabla de empleados
				empleadoDAO.eliminar(dni);
				// Redirigir a listar.jsp después de eliminar
				response.sendRedirect(request.getContextPath() + "/empleados?opcion=listar");
			} catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("mensajeError", "Error al eliminar el empleado: " + e.getMessage());
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
				requestDispatcher.forward(request, response);
			}
		}
	}

	/**
	 * Maneja las solicitudes POST enviadas al servlet.
	 * 
	 * @param request  la solicitud HTTP.
	 * @param response la respuesta HTTP.
	 * @throws ServletException si ocurre un error en el servlet.
	 * @throws IOException      si ocurre un error de entrada/salida.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");
		String opcion = request.getParameter("opcion");

		if ("guardar".equals(opcion)) {
			EmpleadoDAO empleadoDAO = new EmpleadoDAO();
			Empleado empleado = new Empleado();

			String dni = request.getParameter("dni");
			String nombre = request.getParameter("nombre");
			String sexoStr = request.getParameter("sexo");
			String categoriaStr = request.getParameter("categoria");
			String anyosStr = request.getParameter("anyos");

			if (!empleadoDAO.validateDNI(dni)) { // Llamada al método de validación del DNI
				request.setAttribute("mensajeError", "El DNI no es válido.");
				// Establecer otros atributos para mantener la información en el formulario
				request.setAttribute("dni", dni);
				request.setAttribute("nombre", nombre);
				request.setAttribute("sexo", sexoStr);
				request.setAttribute("categoria", categoriaStr);
				request.setAttribute("anyos", anyosStr);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			// Validaciones previas
			if (dni == null || dni.trim().isEmpty() || nombre == null || nombre.trim().isEmpty() || sexoStr == null
					|| sexoStr.trim().isEmpty() || categoriaStr == null || categoriaStr.trim().isEmpty()
					|| anyosStr == null || anyosStr.trim().isEmpty()) {

				request.setAttribute("mensajeError", "Por favor, complete todos los campos necesarios.");
				request.setAttribute("dni", dni);
				request.setAttribute("nombre", nombre);
				request.setAttribute("sexo", sexoStr);
				request.setAttribute("categoria", categoriaStr);
				request.setAttribute("anyos", anyosStr);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			// Validar si el empleado ya existe
			try {
				if (empleadoDAO.existeEmpleado(dni)) {
					request.setAttribute("mensajeError", "El empleado ya existe.");
					request.setAttribute("dni", dni);
					request.setAttribute("nombre", nombre);
					request.setAttribute("sexo", sexoStr);
					request.setAttribute("categoria", categoriaStr);
					request.setAttribute("anyos", anyosStr);
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
					requestDispatcher.forward(request, response);
					return;
				}
			} catch (SQLException | ServletException | IOException e) {
				e.printStackTrace();
			}

			// Validar y establecer sexo
			char sexo;
			if (sexoStr.length() == 1 && (sexoStr.charAt(0) == 'M' || sexoStr.charAt(0) == 'F')) {
				sexo = sexoStr.charAt(0);
			} else {
				request.setAttribute("mensajeError", "El sexo debe ser 'M' o 'F'.");
				request.setAttribute("dni", dni);
				request.setAttribute("nombre", nombre);
				request.setAttribute("sexo", sexoStr);
				request.setAttribute("categoria", categoriaStr);
				request.setAttribute("anyos", anyosStr);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			// Validar y establecer categoría
			int categoria;
			try {
				categoria = Integer.parseInt(categoriaStr);
				if (categoria < 1 || categoria > 10) {
					throw new NumberFormatException("La categoría debe estar entre 1 y 10.");
				}
			} catch (NumberFormatException e) {
				request.setAttribute("mensajeError", "La categoría debe ser un número entre 1 y 10.");
				request.setAttribute("dni", dni);
				request.setAttribute("nombre", nombre);
				request.setAttribute("sexo", sexoStr);
				request.setAttribute("categoria", categoriaStr);
				request.setAttribute("anyos", anyosStr);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			// Validar y establecer años
			int anyos;
			try {
				anyos = Integer.parseInt(anyosStr);
				if (anyos < 0) {
					throw new NumberFormatException("Los años no pueden ser negativos.");
				}
			} catch (NumberFormatException e) {
				request.setAttribute("mensajeError", "Los años de experiencia deben ser un número no negativo.");
				request.setAttribute("dni", dni);
				request.setAttribute("nombre", nombre);
				request.setAttribute("sexo", sexoStr);
				request.setAttribute("categoria", categoriaStr);
				request.setAttribute("anyos", anyosStr);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			// Establecer datos en el empleado
			empleado.setDni(dni);
			empleado.setNombre(nombre);
			empleado.setSexo(sexo);
			try {
				empleado.setCategoria(categoria);
			} catch (DatosNoCorrectosException e) {
				e.printStackTrace();
			}
			empleado.setAnyos(anyos);

			// Guardar el empleado en la base de datos
			try {
				empleadoDAO.guardar(empleado);
				request.getSession().setAttribute("mensajeExito", "Empleado dado de alta satisfactoriamente.");
				response.sendRedirect(request.getContextPath() + "/empleados?opcion=listar");
			} catch (SQLException | DatosNoCorrectosException e) {
				e.printStackTrace();
				request.setAttribute("mensajeError", "Error al guardar el empleado: " + e.getMessage());
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
				requestDispatcher.forward(request, response);
			}

		} else if ("editar".equals(opcion)) {
			EmpleadoDAO empleadoDAO = new EmpleadoDAO();
			Empleado empleado = new Empleado();

			String dni = request.getParameter("dni");
			String nombre = request.getParameter("nombre");
			String sexoStr = request.getParameter("sexo");
			String categoriaStr = request.getParameter("categoria");
			String anyosStr = request.getParameter("anyos");

			// Validaciones previas
			if (nombre == null || nombre.trim().isEmpty() || sexoStr == null || sexoStr.trim().isEmpty()
					|| categoriaStr == null || categoriaStr.trim().isEmpty() || anyosStr == null
					|| anyosStr.trim().isEmpty()) {

				request.setAttribute("mensajeError", "Por favor, complete todos los campos necesarios.");
				request.setAttribute("dni", dni);
				request.setAttribute("nombre", nombre);
				request.setAttribute("sexo", sexoStr);
				request.setAttribute("categoria", categoriaStr);
				request.setAttribute("anyos", anyosStr);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			// Validar y establecer sexo
			char sexo;
			if (sexoStr.length() == 1 && (sexoStr.charAt(0) == 'M' || sexoStr.charAt(0) == 'F')) {
				sexo = sexoStr.charAt(0);
			} else {
				request.setAttribute("mensajeError", "El sexo debe ser 'M' o 'F'.");
				request.setAttribute("dni", dni);
				request.setAttribute("nombre", nombre);
				request.setAttribute("sexo", sexoStr);
				request.setAttribute("categoria", categoriaStr);
				request.setAttribute("anyos", anyosStr);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			// Validar y establecer categoría
			int categoria;
			try {
				categoria = Integer.parseInt(categoriaStr);
				if (categoria < 1 || categoria > 10) {
					throw new NumberFormatException("La categoría debe estar entre 1 y 10.");
				}
			} catch (NumberFormatException e) {
				request.setAttribute("mensajeError", "La categoría debe ser un número entre 1 y 10.");
				request.setAttribute("dni", dni);
				request.setAttribute("nombre", nombre);
				request.setAttribute("sexo", sexoStr);
				request.setAttribute("categoria", categoriaStr);
				request.setAttribute("anyos", anyosStr);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			// Validar y establecer años
			int anyos;
			try {
				anyos = Integer.parseInt(anyosStr);
				if (anyos < 0) {
					throw new NumberFormatException("Los años no pueden ser negativos.");
				}
			} catch (NumberFormatException e) {
				request.setAttribute("mensajeError", "Los años de experiencia deben ser un número no negativo.");
				request.setAttribute("dni", dni);
				request.setAttribute("nombre", nombre);
				request.setAttribute("sexo", sexoStr);
				request.setAttribute("categoria", categoriaStr);
				request.setAttribute("anyos", anyosStr);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
				requestDispatcher.forward(request, response);
				return;
			}

			// Establecer datos en el empleado
			empleado.setDni(dni);
			empleado.setNombre(nombre);
			empleado.setSexo(sexo);
			try {
				empleado.setCategoria(categoria);
			} catch (DatosNoCorrectosException e) {
				e.printStackTrace();
			}
			empleado.setAnyos(anyos);

			// Editar el empleado en la base de datos
			try {
				empleadoDAO.editar(empleado);
				request.getSession().setAttribute("mensajeExito", "Empleado editado satisfactoriamente.");
				response.sendRedirect(request.getContextPath() + "/empleados?opcion=listar");
			} catch (SQLException e) {
				e.printStackTrace();
				request.setAttribute("mensajeError", "Error al editar el empleado: " + e.getMessage());
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
				requestDispatcher.forward(request, response);
			}
		}
	}
}
