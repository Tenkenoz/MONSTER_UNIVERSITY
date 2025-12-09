<%-- 
    Document   : Invitados
    Created on : 5 dic 2025, 21:21:30
    Author     : erick
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>MU | Acceso de Invitados</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilosInvitado.css">
</head>
<body>

    <div class="visitor-pass">
        <div class="pass-clip"></div>

        <div class="header-section">
            <div class="mu-logo-small">MONSTERS UNIVERSITY</div>
            <h1 class="visitor-title">VISITOR</h1>
            <div class="subtitle">Pase Temporal de Acceso</div>
        </div>

        <div class="pass-body">
            <div class="guest-icon">
                ?
            </div>

            <div class="guest-name">${empleado.nombre} ${empleado.apellido}</div>
            
            <div class="guest-info">
                <strong>CEDULA:</strong> ${empleado.codigoPersona}
            </div>
            
            <div class="guest-info">
                ${empleado.email}
            </div>
            
            <div style="margin-top: 15px; color: #d32f2f; font-size: 0.8rem; font-weight: bold;">
                ACCESO RESTRINGIDO
                <br>NO INGRESAR A ÁREAS DE SUSTOS
            </div>

            <div class="barcode-strip"></div>
        </div>

        <div class="pass-footer">
            <br>
            <a href="${pageContext.request.contextPath}/srvUsuario?accion=cerrar" class="btn-leave">Cerrar Sesión</a>
        </div>
    </div>

</body>
</html>