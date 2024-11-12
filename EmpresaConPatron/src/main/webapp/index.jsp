<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Menú de Opciones</title>
<link rel="stylesheet" type="text/css" href="styles/style.css">
<!-- Incluir Font Awesome para los íconos -->
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>

	<jsp:include page="views/header.jsp" />

	<h1>Menú de Opciones Empleados</h1>

	<table>
		<tr>
			<td><a href="index.jsp?opcion=crear">Dar de alta a Empleado</a></td>
		</tr>
		<tr>
			<td><a href="index.jsp?opcion=listar">Listado de Empleados</a></td>
		</tr>
		<tr>
			<td><a href="index.jsp?opcion=salario">Obtener Salario de
					Empleado</a></td>
		</tr>
		<tr>
			<td><a href="index.jsp?opcion=buscar">Buscador de Empleado</a></td>
		</tr>
	</table>

	<!-- Carga del contenido dinámico según el valor de 'opcion' -->
	<c:choose>
		<%-- Opción para crear empleado --%>
		<c:when test="${param.opcion == 'crear'}">
			<jsp:include page="views/crear.jsp" />
		</c:when>

		<%-- Opción para listar empleados --%>
		<c:when test="${param.opcion == 'listar'}">
			<jsp:include page="views/listar.jsp" />
		</c:when>

		<%-- Opción para buscar salario --%>
		<c:when test="${param.opcion == 'salario'}">
			<jsp:include page="views/salario.jsp" />
		</c:when>

		<%-- Opción para buscar empleado --%>
		<c:when test="${param.opcion == 'buscar'}">
			<jsp:include page="views/buscar.jsp" />
		</c:when>

		<%-- Vista por defecto si no hay opción seleccionada --%>
		<c:otherwise>
		</c:otherwise>
	</c:choose>

</body>
</html>
