package com.aprendec.repository;

import com.aprendec.model.Nomina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link Nomina}.
 */
@Repository
public interface NominaRepository extends JpaRepository<Nomina, Long> {

    /**
     * Busca una nómina utilizando el DNI del empleado asociado.
     *
     * @param dni El DNI del empleado para el cual se desea obtener la nómina.
     * @return Una instancia {@link Optional} que contiene la nómina del empleado si existe.
     */
    Optional<Nomina> findByEmpleadoDni(String dni);
}

