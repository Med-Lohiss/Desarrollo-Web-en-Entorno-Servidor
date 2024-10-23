<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Editar Empleado</title>
<link rel="stylesheet" type="text/css" href="styles/style.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>
	<header>
		<!-- Logo de la empresa a la izquierda -->
		<div class="logo">
			<img src="images/Logo.png" alt="Logo Empresa">
		</div>

		<nav>
			<!-- Enlace al index.jsp con ícono de "home" -->
			<a href="index.jsp" title="Inicio"> <i class="fas fa-home"></i>
			</a> <a href="empleados?opcion=crear" title="Crear Empleado"> <i
				class="fas fa-user-plus"></i>
			</a> <a href="empleados?opcion=listar" title="Listar Empleados"> <i
				class="fas fa-list"></i>
			</a> <a href="empleados?opcion=salario"
				title="Buscar Salario de Empleado"> <i
				class="fas fa-search-dollar"></i>
			</a> <a href="empleados?opcion=buscar" title="Buscar Empleado"> <i
				class="fas fa-search"></i>
			</a>
		</nav>
	</header>
	<h1>Actualizar Empleado</h1>

	<!-- Mostrar mensajes de error si los hay -->
	<c:if test="${not empty mensajeError}">
		<div style="color: red;">${mensajeError}</div>
	</c:if>

	<!-- Mostrar mensajes de éxito si los hay -->
	<c:if test="${not empty mensajeExito}">
		<div style="color: green;">${mensajeExito}</div>
	</c:if>

	<form action="empleados" method="post">
		<input type="hidden" name="opcion" value="editar"> <input
			type="hidden" name="dni" value="${empleado.dni}">
		<table>
			<tr>
				<td>Nombre:</td>
				<td>
					<!-- Mantener el valor del campo 'nombre' --> <input type="text"
					name="nombre" size="50"
					value="${not empty nombre ? nombre : empleado.nombre}">
				</td>
			</tr>
			<tr>
				<td>Sexo:</td>
				<td>
					<!-- Mantener el valor del campo 'nombre' --> <input type="text"
					name="sexo" size="50"
					value="${not empty sexo ? sexo : empleado.sexo}">
				</td>
			</tr>
			<tr>
				<td>Categoría:</td>
				<td>
					<!-- Mantener el valor del campo 'categoria' --> <input type="text"
					name="categoria" size="50"
					value="${not empty categoria ? categoria : empleado.categoria}">
				</td>
			</tr>
			<tr>
				<td>Años trabajados:</td>
				<td>
					<!-- Mantener el valor del campo 'anyos' --> <input type="text"
					name="anyos" size="50"
					value="${not empty anyos ? anyos : empleado.anyos}">
				</td>
			</tr>
		</table>

		<input type="submit" value="Guardar">
	</form>
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
