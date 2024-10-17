<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Listar y Crear Empleados</title>
<link rel="stylesheet" type="text/css" href="styles/style.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<script>
	// Función para alternar la clase .active en la caja del formulario
	function toggleForm() {
		var formBox = document.querySelector('.form-box');
		formBox.classList.toggle('active');
	}

	// Función que muestra una ventana de confirmación
	function confirmarEliminacion(dni) {
		var confirmacion = confirm("¿Estás seguro de que deseas eliminar al empleado con DNI "
				+ dni);
		return confirmacion; // Devuelve true si se confirma, false si se cancela
	}

	// Función para volver a la página anterior
	function goBack() {
		window.history.back();
	}
</script>
</head>
<body>
	<header>
		<!-- Logo de la empresa a la izquierda -->
		<div class="logo">
			<img src="images/Logo.png" alt="Logo Empresa">
		</div>

		<nav>
			<!-- Enlaces de navegación -->
			<a href="index.jsp" title="Inicio"><i class="fas fa-home"></i></a> <a
				href="empleados?opcion=crear" title="Crear Empleado"><i
				class="fas fa-user-plus"></i></a> <a href="empleados?opcion=listar"
				title="Listar Empleados"><i class="fas fa-list"></i></a> <a
				href="empleados?opcion=salario" title="Buscar Salario de Empleado"><i
				class="fas fa-search-dollar"></i></a> <a href="empleados?opcion=buscar"
				title="Buscar Empleado"><i class="fas fa-search"></i></a>
		</nav>
	</header>

	<!-- Formulario de creación de empleado (desplegable) -->
	<div class="form-box">
		<h1 onclick="toggleForm()">Dar de alta a empleado</h1>
		<div class="form-content">
			<c:if test="${not empty mensajeError}">
				<div style="color: red;">${mensajeError}</div>
			</c:if>
			<c:if test="${not empty sessionScope.mensajeExito}">
				<div style="color: green;">${sessionScope.mensajeExito}</div>
				<c:remove var="mensajeExito" scope="session" />
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
				<div>
				<input type="submit" value="Guardar"> 
				<input type="reset" value="Borrar">
				</div>
			</form>
		</div>
	</div>

	<h1>Listado de Empleados</h1>

	<!-- Tabla de empleados -->
	<table>
		<tr>
			<th><a href="empleados?opcion=listar&orden=dni&direccion=asc">▲</a>
				DNI <a href="empleados?opcion=listar&orden=dni&direccion=desc">▼</a></th>
			<th><a href="empleados?opcion=listar&orden=nombre&direccion=asc">▲</a>
				Nombre <a href="empleados?opcion=listar&orden=nombre&direccion=desc">▼</a></th>
			<th><a href="empleados?opcion=listar&orden=sexo&direccion=asc">▲</a>
				Sexo <a href="empleados?opcion=listar&orden=sexo&direccion=desc">▼</a></th>
			<th><a
				href="empleados?opcion=listar&orden=categoria&direccion=asc">▲</a>
				Categoria <a
				href="empleados?opcion=listar&orden=categoria&direccion=desc">▼</a></th>
			<th><a href="empleados?opcion=listar&orden=anyos&direccion=asc">▲</a>
				Años Trabajados <a
				href="empleados?opcion=listar&orden=anyos&direccion=desc">▼</a></th>
			<th></th>
			<th></th>
		</tr>
		<c:forEach var="empleado" items="${lista}">
			<tr>
				<td><a href="empleados?opcion=meditar&dni=${empleado.dni}"><c:out
							value="${empleado.dni}" /></a></td>
				<td><c:out value="${empleado.nombre}" /></td>
				<td><c:out value="${empleado.sexo}" /></td>
				<td><c:out value="${empleado.categoria}" /></td>
				<td><c:out value="${empleado.anyos}" /></td>
				<td><a href="empleados?opcion=eliminar&dni=${empleado.dni}"
					onclick="return confirmarEliminacion('${empleado.dni}')"
					title="Eliminar"><i class="fas fa-trash"></i></a></td>
				<td><a href="empleados?opcion=meditar&dni=${empleado.dni}"
					title="Actualizar"><i class="fas fa-edit"></i></a></td>
			</tr>
		</c:forEach>
	</table>

	<button onclick="goBack()" class="btn-volver">
		<i class="fas fa-arrow-left"></i> Volver
	</button>
</body>
</html>
