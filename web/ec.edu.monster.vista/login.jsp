<%-- 
    Document   : login
    Created on : 28 nov 2025
    Author     : MEGAPC / Modificado
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Iniciar Sesión</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
    </head>

    <body>

        <div class="login-card">
            
            <div class="logo-circle">
                <img src="${pageContext.request.contextPath}/resource/logo.jpg" alt="Logo" onerror="this.style.display='none'">
            </div>

            <h2>Bienvenido</h2>

            <% if (request.getAttribute("msjerror") != null && request.getAttribute("modalOpen") == null) { %>
                <div class="error-msg">
                    <%= request.getAttribute("msjerror") %>
                </div>
            <% } %>

            <% if (request.getAttribute("msj") != null) { %>
                <div class="success-msg">
                    <%= request.getAttribute("msj") %>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/srvUsuario" method="POST">
                <input type="hidden" name="accion" value="verificar">

                <input name="txtEmail" type="text" class="input-box" placeholder="Email / Usuario / Cédula" required>
                <input name="txtPassword" type="password" class="input-box" placeholder="Contraseña" required>

                <button type="submit" class="btn-login">INGRESAR</button>

                <div class="links">
                    <a href="javascript:void(0);" onclick="abrirModal()">¿Olvidaste tu contraseña?</a>
                    
                    <a href="${pageContext.request.contextPath}/ec.edu.monster.vista/registrar.jsp">Registrarse</a>
                </div>
            </form>
        </div>

        <div id="modalRecuperar" class="modal">
            <div class="modal-content">
                <span class="close" onclick="cerrarModal()">&times;</span>

                <h3>Recuperar Contraseña</h3>

                <% if (request.getAttribute("msjerror") != null && request.getAttribute("modalOpen") != null) { %>
                    <div class="error-msg">
                        <%= request.getAttribute("msjerror") %>
                    </div>
                    <script>
                        document.addEventListener("DOMContentLoaded", function() {
                            abrirModal();
                        });
                    </script>
                <% } %>

                <form action="${pageContext.request.contextPath}/srvUsuario" method="POST">
                    <input type="hidden" name="accion" value="recuperar">
                    
                    <input name="txtEmailRecuperar" type="email" class="input-box" placeholder="Ingrese su correo registrado" required>
                    
                    <button type="submit" class="btn-login">Enviar Solicitud</button>
                </form>
            </div>
        </div>

        <script>
            function abrirModal() {
                document.getElementById("modalRecuperar").style.display = "block";
            }

            function cerrarModal() {
                document.getElementById("modalRecuperar").style.display = "none";
            }

            // Cerrar modal si se hace clic fuera del contenido
            window.onclick = function(event) {
                var modal = document.getElementById("modalRecuperar");
                if (event.target == modal) {
                    modal.style.display = "none";
                }
            }
        </script>

    </body>
</html>