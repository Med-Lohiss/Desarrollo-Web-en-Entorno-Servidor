<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Crear Empleado</title>
<link rel="stylesheet" type="text/css" href="styles/style.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
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
	<h1>Dar de alta a empleado</h1>

	<c:if test="${not empty mensajeError}">
		<div style="color: red;">${mensajeError}</div>
	</c:if>

	<c:if test="${not empty mensajeExito}">
		<div style="color: green;">${mensajeExito}</div>
	</c:if>

	<form action="empleados" method="post">
		<input type="hidden" name="opcion" value="guardar">
		<table border="1">
			<tr>
				<td>DNI:</td>
				<td><input type="text" name="dni" size="50"
					value="${dni != null ? dni : ''}"></td>
			</tr>
			<tr>
				<td>Nombre:</td>
				<td><input type="text" name="nombre" size="50"
					value="${nombre != null ? nombre : ''}"></td>
			</tr>
			<tr>
				<td>Sexo:</td>
				<td><input type="text" name="sexo" size="50"
					value="${sexo != null ? sexo : ''}"></td>
			</tr>
			<tr>
				<td>Categoria:</td>
				<td><input type="text" name="categoria" size="50"
					value="${categoria != null ? categoria : ''}"></td>
			</tr>
			<tr>
				<td>Años Trabajados:</td>
				<td><input type="text" name="anyos" size="50"
					value="${anyos != null ? anyos : ''}"></td>
			</tr>
		</table>
		<input type="submit" value="Guardar" class="submit"> <input
			type="reset" value="Borrar" class="reset">
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
