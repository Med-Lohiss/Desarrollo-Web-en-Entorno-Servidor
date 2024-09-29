package Laboral;

/**
 * Clase que se encarga de calcular el sueldo de los empleados.
 */
public class Nomina {


    private static final int SUELDO_BASE[] = {50000, 70000, 90000, 110000, 130000, 150000, 170000, 190000, 210000, 230000};

    /**
     * Calcula el sueldo de un empleado basado en su categoría y años trabajados.
     * 
     * @param empleado El empleado del cual se quiere calcular el sueldo.
     * @return El sueldo total del empleado.
     */
    public static int sueldo(Empleado empleado) {
        // Obtener el sueldo base según la categoría del empleado (categoría - 1 por indexación 0)
        int sueldoBase = SUELDO_BASE[empleado.getCategoria() - 1];

        // Obtener los años trabajados por el empleado
        int trabajados = empleado.anyos;

        // Calcular el sueldo total sumando 5000 por cada año trabajado
        return sueldoBase + 5000 * trabajados;
    }
}
