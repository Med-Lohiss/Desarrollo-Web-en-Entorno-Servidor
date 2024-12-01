package com.aprendec.repository;

import com.aprendec.model.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.domain.Specification;
import java.util.List;

/**
 * Repositorio para la entidad {@link Empleado}.
 * 
 * Permite la creación de consultas personalizadas usando especificaciones.
 */
public interface EmpleadoRepository extends JpaRepository<Empleado, String> {

    /**
     * Busca un empleado por su DNI.
     *
     * @param dni El DNI del empleado a buscar.
     * @return El empleado con el DNI proporcionado, o {@code null} si no existe.
     */
    Empleado findByDni(String dni);

    /**
     * Obtiene todos los empleados que coinciden con las especificaciones proporcionadas.
     * Permite realizar consultas personalizadas con criterios dinámicos.
     *
     * @param spec Especificación que define los criterios de búsqueda.
     * @return Una lista de empleados que cumplen con los criterios definidos en la especificación.
     */
    List<Empleado> findAll(Specification<Empleado> spec);
}

