<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<h1>Dar de alta a empleado</h1>

<!-- Mostrar mensajes de error si los hay -->
<c:if test="${not empty mensajeError}">
	<div style="color: red;">${mensajeError}</div>
</c:if>

<!-- Modificamos el formulario para que use MainController -->
<form action="MainController" method="post">
	<input type="hidden" name="entidad" value="empleado"> <input
		type="hidden" name="opcion" value="guardar">

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
			<td>Categoría:</td>
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

