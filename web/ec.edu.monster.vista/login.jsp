<%-- 
    Document   : login
    Created on : 28 nov 2025
    Author     : MEGAPC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Iniciar Sesión</title>
        <link rel="stylesheet" href="../dist/estilos/login.css">
        <style>
            .error-msg {
                color: red;
                text-align: center;
                margin-bottom: 10px;
                font-weight: bold;
            }
        </style>
    </head>

    <body>

        <div class="login-card">

            <h2>Bienvenido</h2>
            
            <%-- Mostrar mensaje de error si existe --%>
            <% 
                if(request.getAttribute("msj") != null) { 
            %>
                <div class="error-msg">
                    <%= request.getAttribute("msj") %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/srvUsuario" method="POST">
                
                <input type="hidden" name="accion" value="verificar">

                <input name="usuario" type="text" class="input-box" placeholder="Usuario / Cédula" required>
                
                <input name="password" type="password" class="input-box" placeholder="Contraseña" required>

                <button type="submit" class="btn-login">INGRESAR</button>

                <div class="links">
                    <a href="#">¿Olvidaste tu contraseña?</a>
                    <a href="#">Registrarse</a>
                </div>

            </form>

        </div>

    </body>
</html>

