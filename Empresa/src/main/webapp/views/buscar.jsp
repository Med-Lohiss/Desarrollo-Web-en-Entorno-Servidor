<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Buscar Empleados</title>
<link rel="stylesheet" type="text/css" href="styles/style.css">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<script>
	// Función para confirmar la eliminación de un empleado
	function confirmarEliminacion(dni) {
		return confirm("¿Estás seguro de que deseas eliminar al empleado con DNI "
				+ dni);
	}

	function toggleForm() {
		var formBox = document.querySelector('.form-buscar');
		formBox.classList.toggle('active');
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
	<div class="form-buscar">
		<h1 onclick="toggleForm()">Buscar Empleado</h1>
		<div class="form-content">
			<form method="GET" action="empleados">
				<input type="hidden" name="opcion" value="buscar">
				<table border="1">
					<tr>
						<td><label for="sexo">Sexo:</label></td>
						<td><select name="sexo" id="sexo"
							onchange="filtrarEmpleados()">
								<option value="">--Seleccione--</option>
								<option value="M">Masculino</option>
								<option value="F">Femenino</option>
						</select></td>
					</tr>
					<tr>
						<td><label for="nombre">Nombre:</label></td>
						<td><input type="text" name="nombre" id="nombre"
							onkeyup="filtrarEmpleados()" size="50"></td>
					</tr>
					<tr>
						<td><label for="categoria">Categoría:</label></td>
						<td><input type="number" name="categoria" id="categoria"
							onkeyup="filtrarEmpleados()" size="50"></td>
					</tr>
					<tr>
						<td><label for="anyos">Años Trabajados:</label></td>
						<td><input type="number" name="anyos" id="anyos"
							onkeyup="filtrarEmpleados()" size="50"></td>
					</tr>
				</table>
				<input type="submit" value="Buscar"> <input type="reset"
					value="Borrar">
			</form>
		</div>
	</div>

	<h1>Listado de Empleados</h1>

	<table>
		<tr>
			<th>DNI</th>
			<th>Nombre</th>
			<th>Sexo</th>
			<th>Categoria</th>
			<th>Años Trabajados</th>
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
	<script>
		function goBack() {
			window.history.back();
		}
	</script>
</body>
</html>
