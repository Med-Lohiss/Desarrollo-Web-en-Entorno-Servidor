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
<script>
	function showPopup(message) {
		document.getElementById('popupMessage').innerText = message;
		var popup = document.getElementById('successPopup');
		popup.style.display = "block";
		setTimeout(function() {
			popup.style.display = "none";
		}, 6000);
	}
</script>
</head>
<body>

	<jsp:include page="header.jsp" />

	<h1>Actualizar Empleado</h1>

	<!-- Mostrar mensajes de error si los hay -->
	<c:if test="${not empty mensajeError}">
		<div style="color: red;">${mensajeError}</div>
	</c:if>


	<!-- Modificamos el action del formulario para que utilice MainController -->
	<form action="MainController" method="post">
		<input type="hidden" name="entidad" value="empleado"> <input
			type="hidden" name="opcion" value="editar"> <input
			type="hidden" name="dni" value="${empleado.dni}">

		<table>
			<tr>
				<td>Nombre:</td>
				<td><input type="text" name="nombre" size="50"
					value="${not empty nombre ? nombre : empleado.nombre}"></td>
			</tr>
			<tr>
				<td>Sexo:</td>
				<td><input type="text" name="sexo" size="50"
					value="${not empty sexo ? sexo : empleado.sexo}"></td>
			</tr>
			<tr>
				<td>Categoría:</td>
				<td><input type="text" name="categoria" size="50"
					value="${not empty categoria ? categoria : empleado.categoria}">
				</td>
			</tr>
			<tr>
				<td>Años trabajados:</td>
				<td><input type="text" name="anyos" size="50"
					value="${not empty anyos ? anyos : empleado.anyos}"></td>
			</tr>
		</table>

		<input type="submit" value="Guardar">
	</form>
	<div id="successPopup" class="popup">
		<p id="popupMessage"></p>
	</div>

	<button onclick="goBack()" class="btn-volver">
		<i class="fas fa-arrow-left"></i> Volver
	</button>

	<script>
		function goBack() {
			window.history.back();
		}
	</script>
	<c:if test="${not empty sessionScope.mensajeExito}">
		<script>
			showPopup("${sessionScope.mensajeExito}");
		</script>
		<c:remove var="mensajeExito" scope="session" />
	</c:if>
</body>
</html>
