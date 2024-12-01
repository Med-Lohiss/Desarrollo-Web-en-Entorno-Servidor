package com.aprendec.controller;

import com.aprendec.model.Empleado;
import com.aprendec.service.EmpleadoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * MainController para gestionar solicitudes relacionadas con empleados.
 * Este controlador actúa como el punto central para las operaciones CRUD y otras acciones
 * sobre la entidad "Empleado".
 */
@RestController
@RequestMapping("/api")
public class MainController {

    @Autowired
    private EmpleadoService empleadoService;
    
    /**
     * Crea un nuevo empleado.
     *
     * @param empleado Objeto con los datos del empleado.
     * @return El empleado guardado o un error si el DNI no es válido.
     */
    @PostMapping("/empleado")
    public ResponseEntity<Empleado> crearEmpleado(@RequestBody Empleado empleado) {
        try {
            Empleado nuevoEmpleado = empleadoService.guardar(empleado);
            return ResponseEntity.ok(nuevoEmpleado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Lista todos los empleados.
     *
     * @return Lista de empleados.
     */
    @GetMapping("/empleados")
    public ResponseEntity<List<Empleado>> listarEmpleados() {
        List<Empleado> empleados = empleadoService.obtenerEmpleados();
        return ResponseEntity.ok(empleados);
    }

    /**
     * Busca un empleado por su DNI.
     *
     * @param dni DNI del empleado.
     * @return El empleado encontrado o un error si no existe.
     */
    @GetMapping("/empleado/{dni}")
    public ResponseEntity<Empleado> buscarEmpleado(@PathVariable String dni) {
        Optional<Empleado> empleado = empleadoService.obtenerEmpleado(dni);
        return empleado.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Edita un empleado existente.
     *
     * @param empleado Objeto con los datos del empleado a editar.
     * @return El empleado editado o un error si no existe.
     */
    @PutMapping("/empleado")
    public ResponseEntity<Empleado> editarEmpleado(@RequestBody Empleado empleado) {
        try {
            Empleado empleadoEditado = empleadoService.editar(empleado);
            if (empleadoEditado != null) {
                return ResponseEntity.ok(empleadoEditado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    /**
     * Elimina un empleado por su DNI.
     *
     * @param dni DNI del empleado.
     * @return Respuesta con el estado de la operación.
     */
    @DeleteMapping("/empleado/{dni}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable String dni) {
        boolean eliminado = empleadoService.eliminar(dni);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Busca empleados usando filtros dinámicos.
     *
     * @param nombre    Nombre del empleado (opcional).
     * @param sexo      Sexo del empleado (opcional).
     * @param categoria Categoría del empleado (opcional).
     * @param anyos     Años de experiencia del empleado (opcional).
     * @return Lista de empleados que coinciden con los filtros.
     */
    @GetMapping("/empleados/filtrar")
    public ResponseEntity<List<Empleado>> buscarEmpleados(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String sexo,
            @RequestParam(required = false) Integer categoria,
            @RequestParam(required = false) Integer anyos) {

        List<Empleado> empleados = empleadoService.buscarEmpleados(nombre, sexo, categoria, anyos);
        return ResponseEntity.ok(empleados);
    }
}
