<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

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

<div class="form-buscar">
	<h1 onclick="toggleForm()">Buscar Empleado</h1>
	<div class="form-content">
		<!-- Actualizamos el formulario para enviar los datos a MainController -->
		<form method="GET" action="MainController">
			<input type="hidden" name="entidad" value="empleado"> <input
				type="hidden" name="opcion" value="buscar">
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
		<th>Categoría</th>
		<th>Años Trabajados</th>
		<th>Acciones</th>
	</tr>

	<!-- Aquí iteramos sobre la lista de empleados obtenida en el controlador -->
	<c:forEach var="empleado" items="${lista}">
		<tr>
			<td><a href="#"><c:out value="${empleado.dni}" /></a></td>
			<td><c:out value="${empleado.nombre}" /></td>
			<td><c:out value="${empleado.sexo}" /></td>
			<td><c:out value="${empleado.categoria}" /></td>
			<td><c:out value="${empleado.anyos}" /></td>
			<td>
				<!-- Acción de eliminar --> <a
				href="MainController?entidad=empleado&opcion=eliminar&dni=${empleado.dni}"
				onclick="return confirmarEliminacion('${empleado.dni}')"
				title="Eliminar"><i class="fas fa-trash"></i></a> <!-- Acción de editar -->
				<a
				href="MainController?entidad=empleado&opcion=editar&dni=${empleado.dni}"
				title="Editar"><i class="fas fa-edit"></i></a>
			</td>
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

