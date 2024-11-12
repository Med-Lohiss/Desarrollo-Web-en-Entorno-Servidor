package com.aprendec.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aprendec.exception.DatosNoCorrectosException;

/**
 * Front Controller que gestiona todas las solicitudes entrantes para la
 * aplicación.
 * 
 * Esta clase actúa como un controlador principal que recibe y maneja todas las
 * solicitudes HTTP. Se encarga de redirigir las solicitudes a los controladores
 * específicos en función de los parámetros proporcionados en la solicitud, como
 * la entidad y la acción. Actualmente, gestiona solicitudes relacionadas con
 * los empleados.
 * 
 * La clase extiende {@link HttpServlet} y está mapeada a la URL
 * {@code /MainController}, recibiendo y procesando tanto solicitudes GET como
 * POST.
 * 
 * <p>
 * <strong>Entidades soportadas:</strong> empleado
 * </p>
 * <p>
 * <strong>Acciones soportadas para la entidad "empleado":</strong> crear,
 * listar, buscar, meditar, salario, eliminar, guardar, editar
 * </p>
 */
@WebServlet(urlPatterns = { "/MainController" })
public class MainController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private EmpleadoController empleadoController;
	// private ClienteController clienteController;

	/**
	 * Constructor de la clase {@code MainController}. Inicializa los controladores
	 * necesarios. En este caso, solo se inicializa {@code empleadoController}.
	 */
	public MainController() {
		this.empleadoController = new EmpleadoController();
		// this.clienteController = new ClienteController();
	}

	/**
	 * Método que maneja las solicitudes HTTP GET.
	 * 
	 * @param request  La solicitud HTTP recibida.
	 * @param response La respuesta HTTP que se enviará al cliente.
	 * @throws ServletException Si ocurre un error en el procesamiento de la
	 *                          solicitud.
	 * @throws IOException      Si ocurre un error durante la entrada/salida de la
	 *                          solicitud o respuesta.
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Método que maneja las solicitudes HTTP POST.
	 * 
	 * @param request  La solicitud HTTP recibida.
	 * @param response La respuesta HTTP que se enviará al cliente.
	 * @throws ServletException Si ocurre un error en el procesamiento de la
	 *                          solicitud.
	 * @throws IOException      Si ocurre un error durante la entrada/salida de la
	 *                          solicitud o respuesta.
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Procesa la solicitud HTTP, recuperando los parámetros {@code entidad} y
	 * {@code opcion} para dirigir la acción correspondiente a un controlador
	 * específico.
	 * 
	 * @param request  La solicitud HTTP recibida.
	 * @param response La respuesta HTTP que se enviará al cliente.
	 * @throws ServletException Si ocurre un error en el procesamiento de la
	 *                          solicitud.
	 * @throws IOException      Si ocurre un error durante la entrada/salida de la
	 *                          solicitud o respuesta.
	 */
	private void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String entidad = request.getParameter("entidad");
		String opcion = request.getParameter("opcion");

		// Verificar si los parámetros son nulos
		if (entidad == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Entidad no especificada.");
			return;
		}

		if (opcion == null) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Opción no especificada.");
			return;
		}

		// Comprobar si la solicitud es AJAX
		boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

		try {
			// Manejar las solicitudes según la entidad
			switch (entidad) {
			case "empleado":
				manejarSolicitudEmpleado(opcion, request, response, isAjax);
				break;
			// Se pueden agregar más entidades aquí
			default:
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Entidad desconocida.");
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
					"Error en el procesamiento de la solicitud.");
		}
	}

	/**
	 * Maneja las solicitudes relacionadas con la entidad "empleado", redirigiendo a
	 * la acción correspondiente del {@code EmpleadoController}.
	 * 
	 * @param opcion   La acción solicitada (crear, listar, buscar, meditar,
	 *                 salario, eliminar, guardar, editar).
	 * @param request  La solicitud HTTP recibida.
	 * @param response La respuesta HTTP que se enviará al cliente.
	 * @param isAjax   Indica si la solicitud es AJAX.
	 * @throws ServletException          Si ocurre un error en el procesamiento de
	 *                                   la solicitud.
	 * @throws IOException               Si ocurre un error durante la
	 *                                   entrada/salida de la solicitud o respuesta.
	 * @throws DatosNoCorrectosException Si ocurre un error relacionado con datos
	 *                                   incorrectos durante el procesamiento.
	 */
	private void manejarSolicitudEmpleado(String opcion, HttpServletRequest request, HttpServletResponse response,
			boolean isAjax) throws ServletException, IOException, DatosNoCorrectosException {

		switch (opcion) {
		case "crear":
			empleadoController.crearEmpleado(request, response);
			break;
		case "listar":
			empleadoController.listarEmpleados(request, response);
			break;
		case "buscar":
			empleadoController.buscarEmpleados(request, response);
			break;
		case "meditar":
			empleadoController.meditarEmpleado(request, response);
			break;
		case "salario":
			empleadoController.calcularSalario(request, response);
			break;
		case "eliminar":
			empleadoController.eliminarEmpleado(request, response);
			break;
		case "guardar":
			empleadoController.guardarEmpleado(request, response);
			break;
		case "editar":
			empleadoController.editarEmpleado(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Acción no reconocida para Empleado");
		}
	}

	// Puedes agregar más lógica para manejar otras entidades si lo deseas.
}
