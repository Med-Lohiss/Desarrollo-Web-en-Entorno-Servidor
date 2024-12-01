package com.aprendec.controller;

import com.aprendec.exception.DatosNoCorrectosException;
import com.aprendec.model.Empleado;
import com.aprendec.service.EmpleadoService;
import com.aprendec.service.NominaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;


/**
 * Controlador para la gestión de empleados.
 * Proporciona operaciones para listar, crear, editar, eliminar y buscar empleados,
 * así como calcular sus salarios.
 */
@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

	@Autowired
	private EmpleadoService empleadoService;

	@Autowired
	private NominaService nominaService;

	/**
     * Muestra la página de inicio.
     *
     * @return El nombre de la vista de inicio ("index").
     */
	@GetMapping("/")
	public String mostrarInicio() {
		return "index"; // Renderiza la página de inicio.
	}

	/**
     * Lista todos los empleados.
     *
     * @param mensajeExito Mensaje opcional de éxito para mostrar en la vista.
     * @return ModelAndView con la lista de empleados y un posible mensaje de éxito.
     */
	@GetMapping("/listar")
	public ModelAndView listarEmpleados(@RequestParam(value = "mensajeExito", required = false) String mensajeExito) {
	    List<Empleado> empleados = empleadoService.obtenerEmpleados();
	    ModelAndView mav = new ModelAndView("listar");

	    if (mensajeExito != null) {
	        mav.addObject("mensajeExito", mensajeExito);
	    }

	    mav.addObject("lista", empleados);
	    return mav;
	}

	/**
     * Muestra el formulario para crear un nuevo empleado.
     *
     * @return ModelAndView del formulario de creación con un objeto vacío de empleado.
     */
	@GetMapping("/crear")
	public ModelAndView mostrarFormularioCrear() {
		ModelAndView mav = new ModelAndView("crear");
		mav.addObject("empleado", new Empleado());
		return mav;
	}

	/**
     * Guarda un nuevo empleado en el sistema.
     *
     * @param empleado Datos del empleado a guardar.
     * @param redirectAttributes Atributos para redirigir mensajes de éxito.
     * @return ModelAndView que redirige a la lista de empleados o muestra un mensaje de error.
     */
	@PostMapping("/guardar")
	public ModelAndView guardarEmpleado(@ModelAttribute Empleado empleado, RedirectAttributes redirectAttributes) {
	    ModelAndView mav = new ModelAndView();
	    try {
	        validarEmpleado(empleado);

	        if (empleadoService.existeEmpleado(empleado.getDni())) {
	            mav.setViewName("crear");
	            mav.addObject("mensajeError", "El empleado ya existe.");
	            return mav;
	        }

	        empleadoService.guardar(empleado);
	        // Usa RedirectAttributes para pasar el mensaje de éxito
	        redirectAttributes.addFlashAttribute("mensajeExito", "Empleado dado de alta satisfactoriamente.");
	        mav.setViewName("redirect:/empleados/listar");
	    } catch (DatosNoCorrectosException e) {
	        mav.setViewName("crear");
	        mav.addObject("mensajeError", e.getMessage());
	    }
	    return mav;
	}


	/**
     * Muestra el formulario para editar un empleado existente.
     *
     * @param dni DNI del empleado a editar.
     * @return ModelAndView con los datos del empleado a editar o una redirección si no se encuentra.
     */
	@GetMapping("/editar/{dni}")
	public ModelAndView mostrarFormularioEditar(@PathVariable String dni) {
		ModelAndView mav = new ModelAndView("editar");
		Optional<Empleado> empleadoOptional = empleadoService.obtenerEmpleado(dni);
		if (empleadoOptional.isPresent()) {
			mav.addObject("empleado", empleadoOptional.get());
		} else {
			mav.setViewName("redirect:/empleados/listar");
			mav.addObject("mensajeError", "Empleado no encontrado.");
		}
		return mav;
	}

	/**
     * Edita un empleado existente en el sistema.
     *
     * @param empleado Datos actualizados del empleado.
     * @param redirectAttributes Atributos para redirigir mensajes de éxito.
     * @return ModelAndView que redirige a la lista de empleados o muestra un mensaje de error.
     */
	@PostMapping("/editar")
	public ModelAndView editarEmpleado(
	        @ModelAttribute Empleado empleado, 
	        RedirectAttributes redirectAttributes) {
	    ModelAndView mav = new ModelAndView();
	    try {
	        validarEmpleado(empleado);

	        if (!empleadoService.existeEmpleado(empleado.getDni())) {
	            mav.setViewName("editar");
	            mav.addObject("mensajeError", "El empleado no existe.");
	            return mav;
	        }

	        // Actualizar los datos del empleado
	        empleadoService.editar(empleado);

	        // Calcular el nuevo sueldo del empleado
	        int nuevoSueldo = nominaService.calcularSueldo(empleado);

	        // Actualizar la nómina del empleado
	        nominaService.actualizarSueldo(empleado.getDni(), nuevoSueldo);

	        // Usa RedirectAttributes para pasar el mensaje de éxito
	        redirectAttributes.addFlashAttribute(
	            "mensajeExito", 
	            "Empleado editado satisfactoriamente."
	        );

	        mav.setViewName("redirect:/empleados/listar");
	    } catch (DatosNoCorrectosException e) {
	        mav.setViewName("editar");
	        mav.addObject("mensajeError", e.getMessage());
	    }
	    return mav;
	}


	/**
     * Elimina un empleado por su DNI.
     *
     * @param dni DNI del empleado a eliminar.
     * @return ModelAndView que redirige a la lista de empleados con un mensaje de éxito o error.
     */
	@GetMapping("/eliminar/{dni}")
	public ModelAndView eliminarEmpleado(@PathVariable String dni) {
		ModelAndView mav = new ModelAndView("redirect:/empleados/listar");
		try {
			empleadoService.eliminar(dni);
			mav.addObject("mensajeExito");
		} catch (Exception e) {
			mav.addObject("mensajeError", "Error al eliminar el empleado: " + e.getMessage());
		}
		return mav;
	}

	/**
     * Calcula el salario de un empleado por su DNI.
     *
     * @param dni DNI del empleado.
     * @return ModelAndView con el salario calculado o un mensaje de error.
     */
	@GetMapping("/calcular-salario")
	public ModelAndView mostrarSalario(@RequestParam(required = false) String dni) {
	    ModelAndView mav = new ModelAndView("calcular-salario");
	    
	    try {
	        if (dni == null || dni.trim().isEmpty()) {
	            return mav;
	        }

	        // Buscar el empleado
	        Optional<Empleado> empleadoOptional = empleadoService.obtenerEmpleado(dni);

	        if (empleadoOptional.isPresent()) {
	            Empleado empleado = empleadoOptional.get();
	            
	            // Obtener el sueldo del empleado desde la tabla de nóminas
	            int sueldo = nominaService.obtenerSueldo(dni); // Asegúrate de tener este método en `NominaService`
	            
	         // Formatear el salario
	            DecimalFormat formato = new DecimalFormat("#,###");
	            String sueldoFormateado = formato.format(sueldo) + "€";

	            mav.addObject("empleado", empleado);
	            mav.addObject("salario", sueldoFormateado);
	        } else {
	            mav.addObject("mensajeError", "Empleado no encontrado.");
	        }
	    } catch (Exception e) {
	        mav.addObject("mensajeError", "Error al obtener los datos: " + e.getMessage());
	    }

	    return mav;
	}
	
	/**
     * Busca empleados en el sistema basado en múltiples criterios.
     *
     * @param nombre    Nombre del empleado (opcional).
     * @param sexo      Sexo del empleado (opcional).
     * @param categoria Categoría del empleado (opcional).
     * @param anyos     Años trabajados por el empleado (opcional).
     * @param model     Modelo de datos para la vista.
     * @return Nombre de la vista de búsqueda.
     */
	@GetMapping("/buscar")
	public String buscarEmpleados(@RequestParam(value = "nombre", required = false) String nombre,
	                               @RequestParam(value = "sexo", required = false) String sexo,
	                               @RequestParam(value = "categoria", required = false) Integer categoria,
	                               @RequestParam(value = "anyos", required = false) Integer anyos,
	                               Model model) {
	    List<Empleado> empleados = empleadoService.buscarEmpleados(nombre, sexo, categoria, anyos);
	    model.addAttribute("empleados", empleados);
	    return "buscar";
	}


	/**
     * Valida los datos de un empleado antes de procesarlos.
     *
     * @param empleado El empleado a validar.
     * @throws DatosNoCorrectosException Si los datos son incorrectos.
     */
	private void validarEmpleado(Empleado empleado) throws DatosNoCorrectosException {
		if (empleado.getDni() == null || empleado.getDni().trim().isEmpty()) {
			throw new DatosNoCorrectosException("El DNI es obligatorio.");
		}
		if (!empleadoService.esDniValido(empleado.getDni())) {
			throw new DatosNoCorrectosException("El DNI no es válido.");
		}
		if (empleado.getNombre() == null || empleado.getNombre().trim().isEmpty()) {
			throw new DatosNoCorrectosException("El nombre es obligatorio.");
		}
		if (empleado.getSexo() != 'M' && empleado.getSexo() != 'F') {
			throw new DatosNoCorrectosException("El sexo debe ser 'M' o 'F'.");
		}
		if (empleado.getCategoria() == null) { // Validación para null
			throw new DatosNoCorrectosException("La categoría es obligatoria.");
		}
		if (empleado.getCategoria() < 1 || empleado.getCategoria() > 10) {
			throw new DatosNoCorrectosException("La categoría debe estar entre 1 y 10.");
		}
		if (empleado.getAnyos() == null) { // Validación para null
			throw new DatosNoCorrectosException("Los años trabajados son obligatorios.");
		}
		if (empleado.getAnyos() < 0) {
			throw new DatosNoCorrectosException("Los años de experiencia no pueden ser negativos.");
		}
	}

}
