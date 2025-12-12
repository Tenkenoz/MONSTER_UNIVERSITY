<%-- 
    Document   : cambiarClaveObligatorio
    Created on : 11 dic 2025, 17:54:21
    Author     : erick
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Cambio de Contraseña</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
    <div class="login-card">
        <h2>Cambio Obligatorio</h2>
        <p style="text-align:center; color:#666;">Ha ingresado con una contraseña temporal. Por su seguridad, debe cambiarla ahora.</p>

        <% if(request.getAttribute("msjerror") != null) { %>
            <div class="error-msg"><%= request.getAttribute("msjerror") %></div>
        <% } %>

        <form action="${pageContext.request.contextPath}/srvUsuario" method="POST">
            <input type="hidden" name="accion" value="cambiarObligatorio">
            
            <input name="txtPass1" type="password" class="input-box" placeholder="Nueva Contraseña" required>
            <input name="txtPass2" type="password" class="input-box" placeholder="Confirmar Contraseña" required>
            
            <button type="submit" class="btn-login">GUARDAR Y CONTINUAR</button>
        </form>
    </div>
</body>
</html>