<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>


<script>
	// Función para alternar la clase .active en la caja del formulario
	function toggleForm() {
		var formBox = document.querySelector('.form-box');
		formBox.classList.toggle('active');
	}

	function confirmarEliminacion(dni) {
		var confirmacion = confirm("¿Estás seguro de que deseas eliminar al empleado con DNI "
				+ dni);
		return confirmacion;
	}

	function goBack() {
		window.history.back();
	}

	function showPopup(message) {
		document.getElementById('popupMessage').innerText = message;
		var popup = document.getElementById('successPopup');
		popup.style.display = "block";
		setTimeout(function() {
			popup.style.display = "none";
		}, 6000);
	}
</script>

<div id="successPopup" class="popup">
	<p id="popupMessage"></p>
</div>


<h1>Listado de Empleados</h1>

<c:if test="${not empty sessionScope.mensajeExito}">
	<script>
		showPopup("${sessionScope.mensajeExito}");
	</script>
	<c:remove var="mensajeExito" scope="session" />
</c:if>

<table>
	<tr>
		<th><a
			href="MainController?entidad=empleado&opcion=listar&orden=dni&direccion=asc">▲</a>
			DNI <a
			href="MainController?entidad=empleado&opcion=listar&orden=dni&direccion=desc">▼</a></th>
		<th><a
			href="MainController?entidad=empleado&opcion=listar&orden=nombre&direccion=asc">▲</a>
			Nombre <a
			href="MainController?entidad=empleado&opcion=listar&orden=nombre&direccion=desc">▼</a></th>
		<th><a
			href="MainController?entidad=empleado&opcion=listar&orden=sexo&direccion=asc">▲</a>
			Sexo <a
			href="MainController?entidad=empleado&opcion=listar&orden=sexo&direccion=desc">▼</a></th>
		<th><a
			href="MainController?entidad=empleado&opcion=listar&orden=categoria&direccion=asc">▲</a>
			Categoria <a
			href="MainController?entidad=empleado&opcion=listar&orden=categoria&direccion=desc">▼</a></th>
		<th><a
			href="MainController?entidad=empleado&opcion=listar&orden=anyos&direccion=asc">▲</a>
			Años Trabajados <a
			href="MainController?entidad=empleado&opcion=listar&orden=anyos&direccion=desc">▼</a></th>
		<th></th>
		<th></th>
	</tr>
	<c:forEach var="empleado" items="${lista}">
		<tr>
			<td><a
				href="MainController?entidad=empleado&opcion=meditar&dni=${empleado.dni}"><c:out
						value="${empleado.dni}" /></a></td>
			<td><c:out value="${empleado.nombre}" /></td>
			<td><c:out value="${empleado.sexo}" /></td>
			<td><c:out value="${empleado.categoria}" /></td>
			<td><c:out value="${empleado.anyos}" /></td>
			<td><a
				href="MainController?entidad=empleado&opcion=eliminar&dni=${empleado.dni}"
				onclick="return confirmarEliminacion('${empleado.dni}')"
				title="Eliminar"><i class="fas fa-trash"></i></a></td>
			<td><a
				href="MainController?entidad=empleado&opcion=meditar&dni=${empleado.dni}"
				title="Actualizar"><i class="fas fa-edit"></i></a></td>
		</tr>
	</c:forEach>
</table>

<button onclick="goBack()" class="btn-volver">
	<i class="fas fa-arrow-left"></i> Volver
</button>
