package com.aprendec.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp; // Importar Timestamp
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aprendec.dao.ProductoDAO;
import com.aprendec.model.Producto;

/**
 * Servlet que maneja las peticiones relacionadas con la tabla de productos.
 */
@WebServlet(description = "administra peticiones para la tabla productos", urlPatterns = { "/productos" })
public class ProductoController extends HttpServlet {
	
    private static final long serialVersionUID = 1L;

    /**
     * Constructor del controlador ProductoController.
     */
    public ProductoController() {
        super();
    }

    /**
     * Maneja las solicitudes GET enviadas al servlet.
     * 
     * @param request  la solicitud HTTP.
     * @param response la respuesta HTTP.
     * @throws ServletException si ocurre un error en el servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String opcion = request.getParameter("opcion");

        if (opcion.equals("crear")) {
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
            requestDispatcher.forward(request, response);

        } else if (opcion.equals("listar")) {
            String orden = request.getParameter("orden") != null ? request.getParameter("orden") : "nombre";
            String direccion = request.getParameter("direccion") != null ? request.getParameter("direccion") : "asc";

            ProductoDAO productoDAO = new ProductoDAO();
            List<Producto> lista = new ArrayList<>();

            try {
                lista = productoDAO.obtenerProductos(orden, direccion);
                request.setAttribute("lista", lista);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
                requestDispatcher.forward(request, response);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else if (opcion.equals("meditar")) {
            int id = Integer.parseInt(request.getParameter("id"));
            ProductoDAO productoDAO = new ProductoDAO();
            Producto p = new Producto();

            try {
                p = productoDAO.obtenerProducto(id);
                request.setAttribute("producto", p);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
                requestDispatcher.forward(request, response);

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } else if (opcion.equals("eliminar")) {
            ProductoDAO productoDAO = new ProductoDAO();
            int id = Integer.parseInt(request.getParameter("id"));

            try {
                productoDAO.eliminar(id);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/productos?opcion=listar");
                requestDispatcher.forward(request, response);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Maneja las solicitudes POST enviadas al servlet.
     * 
     * @param request  la solicitud HTTP.
     * @param response la respuesta HTTP.
     * @throws ServletException si ocurre un error en el servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String opcion = request.getParameter("opcion");
        Date fechaActual = new Date();

        if (opcion.equals("guardar")) {
            ProductoDAO productoDAO = new ProductoDAO();
            Producto producto = new Producto();

            String nombre = request.getParameter("nombre");
            String cantidadStr = request.getParameter("cantidad");
            String precioStr = request.getParameter("precio");

            // Validación de campos vacíos
            if (cantidadStr == null || cantidadStr.trim().isEmpty() || precioStr == null || precioStr.trim().isEmpty()) {
                request.setAttribute("mensajeError", "Por favor, complete todos los campos necesarios.");
                request.setAttribute("nombre", nombre);
                request.setAttribute("cantidad", cantidadStr);
                request.setAttribute("precio", precioStr);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
                requestDispatcher.forward(request, response);
                return;
            }

            // Validación de que los campos cantidad y precio sean numéricos
            try {
                producto.setCantidad(Double.parseDouble(cantidadStr));
                producto.setPrecio(Double.parseDouble(precioStr));
            } catch (NumberFormatException e) {
                request.setAttribute("mensajeError", "La cantidad y el precio deben ser valores numéricos.");
                request.setAttribute("nombre", nombre);
                request.setAttribute("cantidad", cantidadStr);
                request.setAttribute("precio", precioStr);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
                requestDispatcher.forward(request, response);
                return;
            }

            producto.setNombre(nombre);
            producto.setFechaCrear(new Timestamp(fechaActual.getTime()));

            try {
                if (productoDAO.existeProducto(0, producto.getNombre())) {
                    request.setAttribute("mensajeError", "El producto ya existe.");
                    request.setAttribute("nombre", nombre);
                    request.setAttribute("cantidad", cantidadStr);
                    request.setAttribute("precio", precioStr);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
                    requestDispatcher.forward(request, response);
                    return;
                }

                productoDAO.guardar(producto);

                // Mensaje de éxito
                request.setAttribute("mensajeExito", "Producto guardado satisfactoriamente.");
                List<Producto> lista = productoDAO.obtenerProductos("nombre", "asc");
                request.setAttribute("lista", lista);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
                requestDispatcher.forward(request, response);

            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Error al guardar el producto: " + e.getMessage());
                request.setAttribute("nombre", nombre);
                request.setAttribute("cantidad", cantidadStr);
                request.setAttribute("precio", precioStr);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/crear.jsp");
                requestDispatcher.forward(request, response);
            }

        } else if (opcion.equals("editar")) {
            Producto producto = new Producto();
            ProductoDAO productoDAO = new ProductoDAO();

            int id = Integer.parseInt(request.getParameter("id"));
            String nombre = request.getParameter("nombre");
            String cantidadStr = request.getParameter("cantidad");
            String precioStr = request.getParameter("precio");

            // Validar campos vacíos
            if (nombre == null || nombre.trim().isEmpty() || cantidadStr == null || cantidadStr.trim().isEmpty() || precioStr == null || precioStr.trim().isEmpty()) {
                request.setAttribute("mensajeError", "Por favor, complete todos los campos.");
                producto.setId(id);
                producto.setNombre(nombre);
                producto.setCantidad(cantidadStr.isEmpty() ? 0 : Double.parseDouble(cantidadStr));
                producto.setPrecio(precioStr.isEmpty() ? 0 : Double.parseDouble(precioStr));
                request.setAttribute("producto", producto);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
                requestDispatcher.forward(request, response);
                return;
            }

            // Validación de cantidad y precio como números
            try {
                producto.setCantidad(Double.parseDouble(cantidadStr));
                producto.setPrecio(Double.parseDouble(precioStr));
            } catch (NumberFormatException e) {
                request.setAttribute("mensajeError", "La cantidad y el precio deben ser valores numéricos.");
                producto.setId(id);
                producto.setNombre(nombre);
                request.setAttribute("producto", producto);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
                requestDispatcher.forward(request, response);
                return;
            }

            producto.setId(id);
            producto.setNombre(nombre);
            producto.setFechaActualizar(new Timestamp(fechaActual.getTime()));

            try {
                if (productoDAO.existeProducto(id, nombre)) {
                    request.setAttribute("mensajeError", "El producto con este nombre ya existe.");
                    request.setAttribute("producto", producto);
                    RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
                    requestDispatcher.forward(request, response);
                    return;
                }

                productoDAO.editar(producto);
                request.setAttribute("mensajeExito", "Producto editado satisfactoriamente.");
                List<Producto> lista = productoDAO.obtenerProductos("nombre", "asc");
                request.setAttribute("lista", lista);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/listar.jsp");
                requestDispatcher.forward(request, response);

            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("mensajeError", "Error al editar el producto: " + e.getMessage());
                request.setAttribute("producto", producto);
                RequestDispatcher requestDispatcher = request.getRequestDispatcher("/views/editar.jsp");
                requestDispatcher.forward(request, response);
            }
        }
    }
}