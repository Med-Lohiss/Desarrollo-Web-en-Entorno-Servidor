<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.aprendec.model.Empleado" %>
<%@ page import="java.text.NumberFormat" %>
<%@ page import="java.util.Locale" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" type="text/css" href="styles/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <title>Buscar Salario</title>
</head>
<body>
    <header>
        <!-- Logo de la empresa a la izquierda -->
        <div class="logo">
            <img src="images/Logo.png" alt="Logo Empresa">
        </div>

        <nav>
            <!-- Enlace al index.jsp con ícono de "home" -->
            <a href="index.jsp" title="Inicio"><i class="fas fa-home"></i></a>
            <a href="empleados?opcion=crear" title="Crear Empleado"><i class="fas fa-user-plus"></i></a>
            <a href="empleados?opcion=listar" title="Listar Empleados"><i class="fas fa-list"></i></a>
            <a href="empleados?opcion=salario" title="Buscar Salario de Empleado"><i class="fas fa-search-dollar"></i></a>
            <a href="empleados?opcion=buscar" title="Buscar Empleado"><i class="fas fa-search"></i></a>
        </nav>
    </header>

    <h1>Obtener Salario de Empleado</h1>

    <%-- Mostrar mensaje de error si existe --%>
    <%
    String mensajeError = (String) request.getAttribute("mensajeError");
    if (mensajeError != null) {
    %>
    <div style="color: red;">
        <strong><%=mensajeError%></strong>
    </div>
    <%
    }
    %>

    <div class="busqueda">
        <!-- Formulario de búsqueda -->
        <form action="empleados" method="get">
            <input type="hidden" name="opcion" value="salario">
            <table>
                <tr>
                    <td><label for="dni">DNI del empleado:</label></td>
                    <td><input type="text" id="dni" name="dni" required></td>
                </tr>
            </table>
            <button type="submit" class="btn-submit">Buscar</button>
        </form>

        <hr>

        <%-- Mostrar información del empleado solo si está presente y no hay mensaje de error --%>
        <%
        Empleado empleado = (Empleado) request.getAttribute("empleado");
        Integer salario = (Integer) request.getAttribute("salario");

        if (empleado != null && salario != null) {
            // Crear un formato de número para mostrar el salario con separadores de miles y símbolo de euro
            NumberFormat formatoNumero = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
            String salarioFormateado = formatoNumero.format(salario);
        %>
        <h3>Información del Empleado</h3>
        <p>
            <strong>DNI:</strong> <%=empleado.getDni()%>
        </p>
        <p>
            <strong>Nombre:</strong> <%=empleado.getNombre()%>
        </p>
        <p>
            <strong>Salario:</strong> <%=salarioFormateado%>
        </p>
        <%
        }
        %>
    </div>

    <button onclick="goBack()" class="btn-volver">
        <i class="fas fa-arrow-left"></i> Volver
    </button>

    <script>
        function goBack() {
            window.history.back();
        }
    </script>
</body>
</html>
