
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>MU | Portal Docente</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilosEmpleados.css">
</head>
<body>

    <div class="id-badge-container">
        <div class="badge-hole"></div>
        
        <div class="badge-header bg-docente">
            <div class="mu-title">MONSTERS UNIVERSITY</div>
            <div class="role-title">DOCENTE</div>
        </div>

        <div class="badge-body">
            <div class="photo-frame">
                ${sessionScope.empleado.nombre.charAt(0)}
            </div>

            <div class="employee-name">${sessionScope.empleado.nombre} ${sessionScope.empleado.apellido}</div>
            <div class="employee-id">CÉDULA: ${sessionScope.empleado.codigoPersona}</div>

            <div class="details-grid">
                <div class="detail-item">
                    <strong>Facultad</strong>
                    DOCENCIA E INVESTIGACIÓN
                </div>
                <div class="detail-item">
                    <strong>Cod. Empleado</strong>
                    ${sessionScope.empleado.codigoEmple}
                </div>
                <div class="detail-item" style="grid-column: span 2; margin-top: 10px;">
                    <strong>Correo Institucional</strong>
                    ${sessionScope.empleado.email}
                </div>
            </div>
        </div>

    
    </div>

</body>
</html>