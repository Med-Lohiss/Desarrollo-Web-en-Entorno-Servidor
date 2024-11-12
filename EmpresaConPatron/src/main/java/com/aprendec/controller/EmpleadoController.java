package com.aprendec.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aprendec.dao.EmpleadoDAO;
import com.aprendec.dao.FactoryDAO;
import com.aprendec.exception.DatosNoCorrectosException;
import com.aprendec.model.Empleado;
import com.aprendec.model.Nomina;

/**
 * Controlador de la lógica de negocio para gestionar empleados. Esta clase
 * maneja las peticiones relacionadas con los empleados
 */
public class EmpleadoController {

	private EmpleadoDAO empleadoDAO;

	/**
	 * Constructor que inicializa el controlador y obtiene la instancia de
	 * EmpleadoDAO mediante la fábrica de DAOs.
	 */
	public EmpleadoController() {
		this.empleadoDAO = FactoryDAO.getDAO(EmpleadoDAO.class);
	}

	/**
	 * Muestra la vista para la creación de un nuevo empleado.
	 * 
	 * @param request  La solicitud HTTP.
	 * @param response La respuesta HTTP.
	 * 
	 * @throws ServletException Si ocurre un error en el procesamiento de la
	 *                          solicitud.
	 * @throws IOException      Si ocurre un error en la entrada/salida de datos.
	 */
	public void crearEmpleado(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
		requestDispatcher.forward(request, response);
	}

	/**
	 * Muestra un listado de empleados, ordenados según el parámetro de orden y
	 * dirección.
	 * 
	 * @param request  La solicitud HTTP.
	 * @param response La respuesta HTTP.
	 * 
	 * @throws ServletException Si ocurre un error en el procesamiento de la
	 *                          solicitud.
	 * @throws IOException      Si ocurre un error en la entrada/salida de datos.
	 */
	public void listarEmpleados(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String orden = request.getParameter("orden") != null ? request.getParameter("orden") : "nombre";
		String direccion = request.getParameter("direccion") != null ? request.getParameter("direccion") : "asc";
		List<Empleado> lista = new ArrayList<>();

		try {
			lista = empleadoDAO.obtenerEmpleados(orden, direccion);
			request.setAttribute("lista", lista);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);
		} catch (SQLException | DatosNoCorrectosException e) {
			e.printStackTrace();
			request.setAttribute("mensajeError", "Error al obtener empleados: " + e.getMessage());
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);
		}
	}

	/**
	 * Busca empleados en función de filtros como sexo, nombre, categoría y años de
	 * experiencia.
	 * 
	 * @param request  La solicitud HTTP.
	 * @param response La respuesta HTTP.
	 * 
	 * @throws ServletException          Si ocurre un error en el procesamiento de
	 *                                   la solicitud.
	 * @throws IOException               Si ocurre un error en la entrada/salida de
	 *                                   datos.
	 * @throws DatosNoCorrectosException Si los datos de búsqueda no son correctos.
	 */
	public void buscarEmpleados(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, DatosNoCorrectosException {
		String sexo = request.getParameter("sexo");
		String nombre = request.getParameter("nombre");
		String categoria = request.getParameter("categoria");
		String anyos = request.getParameter("anyos");

		EmpleadoDAO empleadoDAO = new EmpleadoDAO();
		List<Empleado> empleadosFiltrados = new ArrayList<>();

		try {
			empleadosFiltrados = empleadoDAO.buscarEmpleados(sexo, nombre, categoria, anyos);
			request.setAttribute("lista", empleadosFiltrados);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("mensajeError", "Error al buscar empleados: " + e.getMessage());
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);
		}
	}

	/**
	 * Muestra la vista para editar un empleado existente.
	 * 
	 * @param request  La solicitud HTTP.
	 * @param response La respuesta HTTP.
	 * 
	 * @throws ServletException Si ocurre un error en el procesamiento de la
	 *                          solicitud.
	 * @throws IOException      Si ocurre un error en la entrada/salida de datos.
	 */
	public void meditarEmpleado(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
	}

	/**
	 * Calcula el salario de un empleado a partir de su DNI.
	 * 
	 * @param request  La solicitud HTTP.
	 * @param response La respuesta HTTP.
	 * 
	 * @throws ServletException Si ocurre un error en el procesamiento de la
	 *                          solicitud.
	 * @throws IOException      Si ocurre un error en la entrada/salida de datos.
	 */
	public void calcularSalario(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String dni = request.getParameter("dni");

		if (dni == null || dni.trim().isEmpty()) {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
			requestDispatcher.forward(request, response);
			return;
		}

		try {
			Empleado empleado = empleadoDAO.obtenerEmpleado(dni);
			if (empleado == null) {
				request.setAttribute("mensajeError", "No se encontró el empleado con DNI: " + dni);
				request.setAttribute("salario", null);
			} else {
				int salario = Nomina.sueldo(empleado);
				request.setAttribute("empleado", empleado);
				request.setAttribute("salario", salario);
			}
		} catch (SQLException | DatosNoCorrectosException e) {
			e.printStackTrace();
			request.setAttribute("mensajeError", "Error al obtener el salario del empleado: " + e.getMessage());
		}

		RequestDispatcher requestDispatcher = request.getRequestDispatcher("index.jsp");
		requestDispatcher.forward(request, response);
	}

	/**
	 * Elimina un empleado de la base de datos.
	 * 
	 * @param request  La solicitud HTTP.
	 * @param response La respuesta HTTP.
	 * 
	 * @throws ServletException Si ocurre un error en el procesamiento de la
	 *                          solicitud.
	 * @throws IOException      Si ocurre un error en la entrada/salida de datos.
	 */
	public void eliminarEmpleado(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String dni = request.getParameter("dni");
		EmpleadoDAO empleadoDAO = new EmpleadoDAO();

		try {
			empleadoDAO.eliminarNominaPorDNI(dni);
			empleadoDAO.eliminar(dni);
			response.sendRedirect(request.getContextPath() + "/MainController?entidad=empleado&opcion=listar");
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("mensajeError", "Error al eliminar el empleado: " + e.getMessage());
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
			requestDispatcher.forward(request, response);
		}
	}

	/**
	 * Guarda un nuevo empleado en la base de datos después de realizar
	 * validaciones.
	 * 
	 * @param request  La solicitud HTTP.
	 * @param response La respuesta HTTP.
	 * 
	 * @throws ServletException          Si ocurre un error en el procesamiento de
	 *                                   la solicitud.
	 * @throws IOException               Si ocurre un error en la entrada/salida de
	 *                                   datos.
	 * @throws DatosNoCorrectosException Si los datos del empleado no son correctos.
	 */
	public void guardarEmpleado(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, DatosNoCorrectosException {
		EmpleadoDAO empleadoDAO = new EmpleadoDAO();
		Empleado empleado = new Empleado();

		String dni = request.getParameter("dni");
		String nombre = request.getParameter("nombre");
		String sexoStr = request.getParameter("sexo");
		String categoriaStr = request.getParameter("categoria");
		String anyosStr = request.getParameter("anyos");

		// Validar el DNI
		if (!empleadoDAO.validateDNI(dni)) {
			request.setAttribute("mensajeError", "El DNI no es válido.");
			mantenerDatosFormulario(request, dni, nombre, sexoStr, categoriaStr, anyosStr);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp?opcion=crear");
			requestDispatcher.forward(request, response);
			return;
		}

		// Validar campos obligatorios
		if (dni == null || dni.trim().isEmpty() || nombre == null || nombre.trim().isEmpty() || sexoStr == null
				|| sexoStr.trim().isEmpty() || categoriaStr == null || categoriaStr.trim().isEmpty() || anyosStr == null
				|| anyosStr.trim().isEmpty()) {
			request.setAttribute("mensajeError", "Por favor, complete todos los campos necesarios.");
			mantenerDatosFormulario(request, dni, nombre, sexoStr, categoriaStr, anyosStr);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp?opcion=crear");
			requestDispatcher.forward(request, response);
			return;
		}

		// Validar si el empleado ya existe en la base de datos
		try {
			if (empleadoDAO.existeEmpleado(dni)) {
				request.setAttribute("mensajeError", "El empleado ya existe.");
				mantenerDatosFormulario(request, dni, nombre, sexoStr, categoriaStr, anyosStr);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp?opcion=crear");
				requestDispatcher.forward(request, response);
				return; // Detener aquí si el empleado ya existe
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Validar y establecer el sexo
		char sexo;
		if (sexoStr.length() == 1 && (sexoStr.charAt(0) == 'M' || sexoStr.charAt(0) == 'F')) {
			sexo = sexoStr.charAt(0);
		} else {
			request.setAttribute("mensajeError", "El sexo debe ser 'M' o 'F'.");
			mantenerDatosFormulario(request, dni, nombre, sexoStr, categoriaStr, anyosStr);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp?opcion=crear");
			requestDispatcher.forward(request, response);
			return;
		}

		// Validar categoría
		int categoria;
		try {
			categoria = Integer.parseInt(categoriaStr);
			if (categoria < 1 || categoria > 10) {
				throw new NumberFormatException("La categoría debe estar entre 1 y 10.");
			}
		} catch (NumberFormatException e) {
			request.setAttribute("mensajeError", "La categoría debe ser un número entre 1 y 10.");
			mantenerDatosFormulario(request, dni, nombre, sexoStr, categoriaStr, anyosStr);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp?opcion=crear");
			requestDispatcher.forward(request, response);
			return;
		}

		// Validar años de experiencia
		int anyos;
		try {
			anyos = Integer.parseInt(anyosStr);
			if (anyos < 0) {
				throw new NumberFormatException("Los años no pueden ser negativos.");
			}
		} catch (NumberFormatException e) {
			request.setAttribute("mensajeError", "Los años de experiencia deben ser un número no negativo.");
			mantenerDatosFormulario(request, dni, nombre, sexoStr, categoriaStr, anyosStr);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp?opcion=crear");
			requestDispatcher.forward(request, response);
			return;
		}

		// Guardar el empleado en la base de datos
		empleado.setDni(dni);
		empleado.setNombre(nombre);
		empleado.setSexo(sexo);
		empleado.setCategoria(categoria);
		empleado.setAnyos(anyos);

		try {
			empleadoDAO.guardar(empleado);
			request.getSession().setAttribute("mensajeExito", "Empleado dado de alta satisfactoriamente.");
			response.sendRedirect(request.getContextPath() + "/index.jsp?opcion=listar");
		} catch (SQLException | DatosNoCorrectosException e) {
			e.printStackTrace();
			request.setAttribute("mensajeError", "Error al guardar el empleado: " + e.getMessage());
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp?opcion=crear");
			requestDispatcher.forward(request, response);
		}
	}

	/**
	 * Mantiene los datos ingresados en el formulario para ser reutilizados en caso
	 * de error de validación.
	 * 
	 * @param request   La solicitud HTTP.
	 * @param dni       El DNI del empleado.
	 * @param nombre    El nombre del empleado.
	 * @param sexo      El sexo del empleado.
	 * @param categoria La categoría del empleado.
	 * @param anyos     Los años de experiencia del empleado.
	 */
	private void mantenerDatosFormulario(HttpServletRequest request, String dni, String nombre, String sexo,
			String categoria, String anyos) {
		request.setAttribute("dni", dni);
		request.setAttribute("nombre", nombre);
		request.setAttribute("sexo", sexo);
		request.setAttribute("categoria", categoria);
		request.setAttribute("anyos", anyos);
	}

	/**
	 * Edita la información de un empleado en la base de datos.
	 * 
	 * @param request  La solicitud HTTP que contiene los parámetros del formulario.
	 * @param response La respuesta HTTP que se usará para redirigir o mostrar
	 *                 errores.
	 * 
	 * @throws ServletException Si ocurre un error durante el procesamiento de la
	 *                          solicitud.
	 * @throws IOException      Si ocurre un error en la entrada/salida de datos,
	 *                          como redirección o reenvío de la solicitud.
	 */
	public void editarEmpleado(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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
			response.sendRedirect(request.getContextPath() + "/MainController?entidad=empleado&opcion=listar");
		} catch (SQLException e) {
			e.printStackTrace();
			request.setAttribute("mensajeError", "Error al editar el empleado: " + e.getMessage());
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
			requestDispatcher.forward(request, response);
		}
	}
}
