<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Listar y Crear Productos</title>
<link rel="stylesheet" type="text/css" href="Style/style.css">
<script>
	// Función para alternar la clase .active en la caja del formulario
	function toggleForm() {
		var formBox = document.querySelector('.form-box');
		formBox.classList.toggle('active');
	}
</script>
</head>
<body>

	<!-- Formulario de creación de producto (desplegable) -->
	<div class="form-box">
		<h1 onclick="toggleForm()">Añadir Nuevo Producto</h1>

		<!-- Contenido del formulario que será desplegable -->
		<div class="form-content">
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
						<td><input type="text" name="nombre" size="50" value="${nombre != null ? nombre : ''}"></td>
					</tr>
					<tr>
						<td>Cantidad:</td>
						<td><input type="text" name="cantidad" size="50" value="${cantidad != null ? cantidad : ''}"></td>
					</tr>
					<tr>
						<td>Precio:</td>
						<td><input type="text" name="precio" size="50" value="${precio != null ? precio : ''}"></td>
					</tr>
				</table>
				<input type="submit" value="Guardar">
			</form>
		</div>
	</div>

	<h1>Listar Productos</h1>

	<!-- Tabla de productos -->
	<table>
		<tr>
			<th>Id</th>
			<th><a href="productos?opcion=listar&orden=nombre&direccion=asc">▲</a>
				Nombre <a href="productos?opcion=listar&orden=nombre&direccion=desc">▼</a>
			</th>
			<th><a
				href="productos?opcion=listar&orden=cantidad&direccion=asc">▲</a>
				Cantidad <a
				href="productos?opcion=listar&orden=cantidad&direccion=desc">▼</a></th>
			<th><a href="productos?opcion=listar&orden=precio&direccion=asc">▲</a>
				Precio <a href="productos?opcion=listar&orden=precio&direccion=desc">▼</a>
			</th>
			<th><a
				href="productos?opcion=listar&orden=fechaActualizar&direccion=asc">▲</a>
				Fecha Actualización <a
				href="productos?opcion=listar&orden=fechaActualizar&direccion=desc">▼</a>
			</th>
			<th><a
				href="productos?opcion=listar&orden=fechaCrear&direccion=asc">▲</a>
				Fecha Creación <a
				href="productos?opcion=listar&orden=fechaCrear&direccion=desc">▼</a>
			</th>
			<th>Acción</th>
			<th>Acción</th>
		</tr>
		<c:forEach var="producto" items="${lista}">
			<tr>
				<td><a href="productos?opcion=meditar&id=${producto.id}"> <c:out
							value="${producto.id}" />
				</a></td>
				<td><c:out value="${producto.nombre}" /></td>
				<td><c:out value="${producto.cantidad}" /></td>
				<td><c:out value="${producto.precio}" /></td>
				<td><fmt:formatDate value="${producto.fechaActualizar}"
						pattern="EEEE, d 'de' MMMM 'de' yyyy, h:mm:ss a" /></td>
				<td><fmt:formatDate value="${producto.fechaCrear}"
						pattern="EEEE, d 'de' MMMM 'de' yyyy, h:mm:ss a" /></td>
				<td><a href="productos?opcion=eliminar&id=${producto.id}">Eliminar</a>
				</td>
				<td><a href="productos?opcion=meditar&id=${producto.id}">Editar</a>
				</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
