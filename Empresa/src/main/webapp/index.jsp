<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Menú de Opciones</title>
<link rel="stylesheet" type="text/css" href="styles/style.css">
<!-- Incluir Font Awesome para los íconos -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>

    <!-- Header con logo a la izquierda y enlaces con íconos a la derecha -->
    <header>
        <!-- Logo de la empresa a la izquierda -->
        <div class="logo">
            <img src="images/Logo.png" alt="Logo Empresa">
        </div>

        <nav>
            <!-- Enlace al index.jsp con ícono de "home" -->
            <a href="index.jsp" title="Inicio">
                <i class="fas fa-home"></i>
            </a>
            <a href="empleados?opcion=crear" title="Crear Empleado">
                <i class="fas fa-user-plus"></i>
            </a>
            <a href="empleados?opcion=listar" title="Listar Empleados">
                <i class="fas fa-list"></i>
            </a>
            <a href="empleados?opcion=salario" title="Buscar Salario de Empleado">
                <i class="fas fa-search-dollar"></i>
            </a>
            <a href="empleados?opcion=buscar" title="Buscar Empleado">
                <i class="fas fa-search"></i>
            </a>
        </nav>
    </header>

    <h1>Menú de Opciones Empleados</h1>
    
    <table>
        <tr>
            <td><a href="empleados?opcion=crear"> Dar de alta a Empleado</a></td>
        </tr>
        <tr>
            <td><a href="empleados?opcion=listar"> Listado de Empleados</a></td>
        </tr>
        <tr>
            <td><a href="empleados?opcion=salario">Obtener Salario de Empleado</a></td>
        </tr>
        <tr>
            <td><a href="empleados?opcion=buscar">Buscador de Empleado</a></td>
        </tr>
    </table>

</body>
</html>
