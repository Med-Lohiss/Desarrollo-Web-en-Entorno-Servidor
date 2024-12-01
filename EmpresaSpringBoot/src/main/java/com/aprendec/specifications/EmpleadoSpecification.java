package com.aprendec.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.aprendec.model.Empleado;

/**
 * Clase que contiene las especificaciones para realizar consultas dinámicas en la base de datos
 * sobre los empleados. Utiliza el patrón Specification de Spring Data JPA para crear filtros de búsqueda
 * basados en los campos del modelo {@link Empleado}.
 */
public class EmpleadoSpecification {

    /**
     * Crea una especificación para buscar empleados por su nombre.
     * Si el nombre no es nulo ni vacío, se realiza una búsqueda parcial usando LIKE.
     *
     * @param nombre El nombre (o parte del nombre) del empleado a buscar.
     * @return Una especificación para filtrar por el nombre del empleado.
     */
    public static Specification<Empleado> buscarPorNombre(String nombre) {
        return (root, query, criteriaBuilder) -> {
            if (nombre != null && !nombre.isEmpty()) {
                return criteriaBuilder.like(root.get("nombre"), "%" + nombre + "%");
            }
            return null;
        };
    }

    /**
     * Crea una especificación para buscar empleados por su sexo.
     * Si el sexo no es nulo ni vacío, se realiza una búsqueda exacta.
     *
     * @param sexo El sexo del empleado (por ejemplo, 'M' o 'F') a buscar.
     * @return Una especificación para filtrar por el sexo del empleado.
     */
    public static Specification<Empleado> buscarPorSexo(String sexo) {
        return (root, query, criteriaBuilder) -> {
            if (sexo != null && !sexo.isEmpty()) {
                return criteriaBuilder.equal(root.get("sexo"), sexo.charAt(0));
            }
            return null;
        };
    }

    /**
     * Crea una especificación para buscar empleados por su categoría.
     * Si la categoría no es nula, se realiza una búsqueda exacta.
     *
     * @param categoria La categoría del empleado a buscar.
     * @return Una especificación para filtrar por la categoría del empleado.
     */
    public static Specification<Empleado> buscarPorCategoria(Integer categoria) {
        return (root, query, criteriaBuilder) -> {
            if (categoria != null) {
                return criteriaBuilder.equal(root.get("categoria"), categoria);
            }
            return null;
        };
    }

    /**
     * Crea una especificación para buscar empleados por los años trabajados.
     * Si el número de años no es nulo, se realiza una búsqueda exacta.
     *
     * @param anyos Los años trabajados por el empleado a buscar.
     * @return Una especificación para filtrar por los años trabajados del empleado.
     */
    public static Specification<Empleado> buscarPorAnyos(Integer anyos) {
        return (root, query, criteriaBuilder) -> {
            if (anyos != null) {
                return criteriaBuilder.equal(root.get("anyos"), anyos);
            }
            return null;
        };
    }
}
