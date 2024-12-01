package com.aprendec.service;

import com.aprendec.model.Empleado;
import com.aprendec.model.Nomina;
import com.aprendec.repository.EmpleadoRepository;
import com.aprendec.repository.NominaRepository;
import com.aprendec.specifications.EmpleadoSpecification;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que maneja la lógica de negocio relacionada con los empleados y sus nóminas.
 * Esta clase proporciona métodos para validar, guardar, editar, eliminar, y consultar empleados.
 * También maneja la lógica de cálculo y almacenamiento de las nóminas asociadas.
 */
@Service
public class EmpleadoService {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Autowired
    private NominaRepository nominaRepository;
    
    @Autowired
    private NominaService nominaService;

    /**
     * Valida si un DNI es válido según el formato español.
     * El DNI debe tener entre 8 y 9 caracteres, los primeros 8 deben ser numéricos,
     * y la letra debe coincidir con el número calculado según el algoritmo oficial.
     *
     * @param dni El DNI que se va a validar.
     * @return true si el DNI es válido, false si es inválido.
     */
    public boolean esDniValido(String dni) {
        if (dni == null || dni.length() < 8 || dni.length() > 9) {
            return false; // El DNI debe tener entre 8 y 9 caracteres
        }

        try {
            int numero = Integer.parseInt(dni.substring(0, 8)); // Los primeros 8 caracteres son números
            char letra = "TRWAGMYFPDXBNJZSQVHLCKE".charAt(numero % 23); // La letra es calculada según el número
            return dni.charAt(8) == letra; // La letra en la posición 9 debe ser la correcta
        } catch (NumberFormatException e) {
            return false; // Si la conversión falla, no es un DNI válido
        }
    }

    /**
     * Verifica si un empleado existe en el repositorio mediante su DNI.
     *
     * @param dni El DNI del empleado a verificar.
     * @return true si el empleado existe, false en caso contrario.
     */
    public boolean existeEmpleado(String dni) {
        return empleadoRepository.existsById(dni);
    }

    /**
     * Guarda un nuevo empleado y su nómina asociada en la base de datos.
     * Si el DNI del empleado no es válido, se lanza una excepción.
     * Se calcula el sueldo del empleado utilizando el servicio de nómina.
     *
     * @param empleado El empleado que se desea guardar.
     * @return El empleado guardado.
     * @throws IllegalArgumentException Si el DNI no es válido.
     */
    @Transactional
    public Empleado guardar(Empleado empleado) {
        // Validar el DNI antes de guardar
        if (!esDniValido(empleado.getDni())) {
            throw new IllegalArgumentException("DNI no válido.");
        }

        // Guardar el empleado en la base de datos
        Empleado empleadoGuardado = empleadoRepository.save(empleado);

        // Calcular el sueldo utilizando el servicio de Nómina
        int sueldo = nominaService.calcularSueldo(empleado);  // Calculamos el sueldo con la clase Nomina

        // Crear una instancia de Nomina y asignar los valores
        Nomina nomina = new Nomina();
        nomina.setDni(empleado.getDni());  // Establecer el DNI del empleado
        nomina.setSueldo(sueldo);  // Establecer el sueldo calculado

        // Guardar la nómina en la base de datos
        nominaRepository.save(nomina);

        // Devolver el empleado guardado
        return empleadoGuardado;
    }

    /**
     * Edita los datos de un empleado existente en el repositorio.
     * Si el empleado no existe, no se realiza ninguna acción.
     *
     * @param empleado El empleado con los nuevos datos.
     * @return El empleado editado, o null si no se encuentra.
     */
    public Empleado editar(Empleado empleado) {
        if (empleadoRepository.existsById(empleado.getDni())) {
            return empleadoRepository.save(empleado);
        }
        return null;
    }

    /**
     * Elimina un empleado del repositorio utilizando su DNI.
     *
     * @param dni El DNI del empleado que se desea eliminar.
     * @return true si el empleado fue eliminado correctamente, false si no se encontró.
     */
    public boolean eliminar(String dni) {
        if (empleadoRepository.existsById(dni)) {
            empleadoRepository.deleteById(dni);
            return true;
        }
        return false;
    }

    /**
     * Obtiene todos los empleados del repositorio.
     *
     * @return Lista de todos los empleados.
     */
    public List<Empleado> obtenerEmpleados() {
        return empleadoRepository.findAll();
    }

    /**
     * Obtiene un empleado específico por su DNI.
     *
     * @param dni El DNI del empleado a buscar.
     * @return Un {@link Optional} que contiene el empleado si existe, o está vacío si no existe.
     */
    public Optional<Empleado> obtenerEmpleado(String dni) {
        return empleadoRepository.findById(dni);
    }

    /**
     * Busca empleados utilizando filtros dinámicos.
     * Se pueden aplicar filtros por nombre, sexo, categoría y años de experiencia.
     *
     * @param nombre Nombre del empleado (puede ser nulo).
     * @param sexo Sexo del empleado (puede ser nulo).
     * @param categoria Categoría del empleado (puede ser nulo).
     * @param anyos Años de experiencia del empleado (puede ser nulo).
     * @return Lista de empleados que coinciden con los filtros proporcionados.
     */
    public List<Empleado> buscarEmpleados(String nombre, String sexo, Integer categoria, Integer anyos) {
        Specification<Empleado> spec = Specification.where(null);

        // Aplica cada filtro si el parámetro no es nulo o vacío
        if (nombre != null && !nombre.isEmpty()) {
            spec = spec.and(EmpleadoSpecification.buscarPorNombre(nombre));
        }
        if (sexo != null && !sexo.isEmpty()) {
            spec = spec.and(EmpleadoSpecification.buscarPorSexo(sexo));
        }
        if (categoria != null) {
            spec = spec.and(EmpleadoSpecification.buscarPorCategoria(categoria));
        }
        if (anyos != null) {
            spec = spec.and(EmpleadoSpecification.buscarPorAnyos(anyos));
        }

        // Ejecuta la consulta con los filtros aplicados
        return empleadoRepository.findAll(spec);
    }
}
