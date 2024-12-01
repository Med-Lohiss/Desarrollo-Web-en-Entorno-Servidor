package com.aprendec.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aprendec.exception.DatosNoCorrectosException;
import com.aprendec.model.Empleado;
import com.aprendec.model.Nomina;
import com.aprendec.repository.NominaRepository;

import java.util.Optional;

/**
 * Servicio encargado de gestionar la lógica relacionada con las nóminas de los empleados.
 * Este servicio calcula el sueldo de los empleados en función de su categoría y años trabajados,
 * y actualiza o consulta la información de la nómina asociada a cada empleado.
 */
@Service
public class NominaService {

    private static final int SUELDO_BASE[] = {50000, 70000, 90000, 110000, 130000, 150000, 170000, 190000, 210000, 230000};

    @Autowired
    private NominaRepository nominaRepository;

    /**
     * Calcula el sueldo total de un empleado basado en su categoría y años trabajados.
     * El sueldo base está determinado por la categoría del empleado, y se le suman 5000 por cada año trabajado.
     *
     * @param empleado El empleado cuyo sueldo se va a calcular.
     * @return El sueldo calculado para el empleado.
     */
    public int calcularSueldo(Empleado empleado) {
        // Obtenemos el sueldo base según la categoría del empleado (asumimos que la categoría es un valor entre 1 y 10)
        int sueldoBase = SUELDO_BASE[empleado.getCategoria() - 1];
        // Calculamos el incremento por años trabajados
        int trabajados = empleado.getAnyos();
        return sueldoBase + 5000 * trabajados;
    }

    /**
     * Actualiza el sueldo de un empleado en su nómina.
     * Si no se encuentra una nómina para el empleado con el DNI proporcionado, se lanza una excepción.
     *
     * @param dni        El DNI del empleado cuyo sueldo se quiere actualizar.
     * @param nuevoSueldo El nuevo sueldo que se desea establecer.
     * @throws DatosNoCorrectosException Si no se encuentra una nómina para el empleado con el DNI proporcionado.
     */
    public void actualizarSueldo(String dni, int nuevoSueldo) {
        // Buscamos la nómina del empleado con el DNI proporcionado
        Optional<Nomina> nominaOptional = nominaRepository.findByEmpleadoDni(dni);
        if (nominaOptional.isPresent()) {
            // Si existe la nómina, actualizamos el sueldo
            Nomina nomina = nominaOptional.get();
            nomina.setSueldo(nuevoSueldo);
            // Guardamos los cambios en la base de datos
            nominaRepository.save(nomina);
        } else {
            // Si no se encuentra la nómina, lanzamos una excepción
            throw new DatosNoCorrectosException("No se encontró una nómina para el empleado con DNI: " + dni);
        }
    }
    
    /**
     * Obtiene el sueldo de un empleado basado en su DNI.
     * Si no se encuentra el sueldo asociado al empleado, se lanza una excepción.
     *
     * @param dni El DNI del empleado cuyo sueldo se desea obtener.
     * @return El sueldo del empleado.
     * @throws DatosNoCorrectosException Si no se encuentra el sueldo para el empleado con el DNI proporcionado.
     */
    public int obtenerSueldo(String dni) {
        // Obtenemos la nómina asociada al empleado por su DNI
        return nominaRepository.findByEmpleadoDni(dni)
                .map(Nomina::getSueldo) // Si existe la nómina, obtenemos el sueldo
                .orElseThrow(() -> new DatosNoCorrectosException("Sueldo no encontrado para el empleado con DNI: " + dni)); // Si no existe, lanzamos una excepción
    }
}
