<%-- 
    Document   : Secretaria
    Created on : 30 nov 2025, 15:55:35
    Author     : erick
--%>


<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>MU | Secretaría</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilosEmpleados.css">
</head>
<body>

    <div class="id-badge-container">
        <div class="badge-hole"></div>
        
        <div class="badge-header bg-secretaria">
            <div class="mu-title">MONSTERS UNIVERSITY</div>
            <div class="role-title">SECRETARÍA</div>
        </div>

        <div class="badge-body">
            <div class="photo-frame">
                ${empleado.nombre.charAt(0)}
            </div>

            <div class="employee-name">${empleado.nombre} ${empleado.apellido}</div>
            <div class="employee-id">ID: ${empleado.codigoPersona}</div>

            <div class="details-grid">
                <div class="detail-item">
                    <strong>Departamento</strong>
                    ADMINISTRACIÓN / SEC
                </div>
                <div class="detail-item">
                    <strong>Cod. Empleado</strong>
                    ${empleado.codigoEmple}
                </div>
                <div class="detail-item" style="grid-column: span 2; margin-top: 10px;">
                    <strong>Correo Institucional</strong>
                    ${empleado.email}
                </div>
            </div>
        </div>

        <div class="badge-footer">
            <a href="${pageContext.request.contextPath}/srvUsuario?accion=cerrar" class="btn-exit">Cerrar Sistema</a>
        </div>
    </div>

</body>
</html>