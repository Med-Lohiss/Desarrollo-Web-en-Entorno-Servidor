<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Crear Producto</title>
<link rel="stylesheet" type="text/css" href="Style/style.css">
</head>
<body>
	<h1>Crear Producto</h1>

	<%
	String mensajeError = (String) request.getAttribute("mensajeError");
	String mensajeExito = (String) request.getAttribute("mensajeExito");
	%>

	<c:if test="${not empty mensajeError}">
		<div style="color: red;">${mensajeError}</div>
	</c:if>

	<c:if test="${not empty mensajeExito}">
		<div style="color: green;">${mensajeExito}</div>
	</c:if>

	<form action="productos" method="post">
		<input type="hidden" name="opcion" value="guardar">
		<table border="1">
			<tr>
				<td>Nombre:</td>
				<td><input type="text" name="nombre" size="50"
					value="${nombre}"></td>
			</tr>
			<tr>
				<td>Cantidad:</td>
				<td><input type="text" name="cantidad" size="50"
					value="${cantidad}"></td>
			</tr>
			<tr>
				<td>Precio:</td>
				<td><input type="text" name="precio" size="50"
					value="${precio}"></td>
			</tr>
		</table>
		<input type="submit" value="Guardar">
	</form>
</body>
</html>
