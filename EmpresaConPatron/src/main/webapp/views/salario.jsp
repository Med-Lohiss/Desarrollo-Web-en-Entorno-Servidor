<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.aprendec.model.Empleado"%>
<%@ page import="java.text.NumberFormat"%>
<%@ page import="java.util.Locale"%>

<h1>Obtener Salario de Empleado</h1>

<%-- Mostrar mensaje de error si existe --%>
<%
String mensajeError = (String) request.getAttribute("mensajeError");
if (mensajeError != null) {
	
%> <div style="color: red;">
	<strong><%=mensajeError%></strong>
   </div>
<%

}
%>

<div class="busqueda">
	<!-- Formulario de búsqueda -->
	<form action="MainController" method="get">
		<input type="hidden" name="entidad" value="empleado"> <input
			type="hidden" name="opcion" value="salario">
		<table>
			<tr>
				<td><label for="dni">DNI del empleado:</label></td>
				<td><input type="text" id="dni" name="dni" required></td>
			</tr>
		</table>
		<button type="submit" class="btn-submit">Buscar</button>
	</form>

	<hr>

	<%-- Mostrar información del empleado solo si está presente y no hay mensaje de error --%>
	<%
	Empleado empleado = (Empleado) request.getAttribute("empleado");
	Integer salario = (Integer) request.getAttribute("salario");

	if (empleado != null && salario != null) {
		// Crear un formato de número para mostrar el salario con separadores de miles y símbolo de euro
		NumberFormat formatoNumero = NumberFormat.getCurrencyInstance(new Locale("es", "ES"));
		String salarioFormateado = formatoNumero.format(salario);
	%>
	<h3>Información del Empleado</h3>
	<p>
		<strong>DNI:</strong>
		<%=empleado.getDni()%>
	</p>
	<p>
		<strong>Nombre:</strong>
		<%=empleado.getNombre()%>
	</p>
	<p>
		<strong>Salario:</strong>
		<%=salarioFormateado%>
	</p>
	<%
	}
	%>
</div>

<button onclick="goBack()" class="btn-volver">
	<i class="fas fa-arrow-left"></i> Volver
</button>

<script>
	function goBack() {
		window.history.back();
	}
</script>
