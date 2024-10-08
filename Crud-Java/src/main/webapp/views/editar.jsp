<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Editar Producto</title>
    <link rel="stylesheet" type="text/css" href="Style/style.css">
</head>
<body>
    <h1>Editar Producto</h1>

    <!-- Mostrar mensajes de error si los hay -->
    <c:if test="${not empty mensajeError}">
        <div style="color: red;">${mensajeError}</div>
    </c:if>
    
    <!-- Mostrar mensajes de éxito si los hay -->
    <c:if test="${not empty mensajeExito}">
        <div style="color: green;">${mensajeExito}</div>
    </c:if>

    <form action="productos" method="post">
        <input type="hidden" name="opcion" value="editar">
        <input type="hidden" name="id" value="${producto.id}">
        <table>
            <tr>
                <td>Nombre:</td>
                <td>
                    <!-- Mantener el valor del campo 'nombre' -->
                    <input type="text" name="nombre" size="50" 
                           value="${not empty nombre ? nombre : producto.nombre}">
                </td>
            </tr>
            <tr>
                <td>Cantidad:</td>
                <td>
                    <!-- Mantener el valor del campo 'cantidad' -->
                    <input type="text" name="cantidad" size="50" 
                           value="${not empty cantidad ? cantidad : producto.cantidad}">
                </td>
            </tr>
            <tr>
                <td>Precio:</td>
                <td>
                    <!-- Mantener el valor del campo 'precio' -->
                    <input type="text" name="precio" size="50" 
                           value="${not empty precio ? precio : producto.precio}">
                </td>
            </tr>
        </table>

        <input type="submit" value="Guardar">
    </form>
</body>
</html>
