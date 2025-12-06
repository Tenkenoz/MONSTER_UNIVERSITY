<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- Importamos JSTL si fuera necesario, pero usaremos Expression Language puro --%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Monsters University | Perfil de Estudiante</title>
    <link href="https://fonts.googleapis.com/css2?family=Graduate&family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estudiante.css">
</head>
<body>

    <div class="mu-card">
        <div class="mu-header">
            <h1 class="mu-logo-text">Monsters University</h1>
            <span style="font-size: 0.9rem; letter-spacing: 1px;">SISTEMA ACADÉMICO INTEGRAL</span>
        </div>

        <div class="mu-body">
            <div class="profile-section">
                <div class="avatar-circle">
                    ${estudiante.nombre.charAt(0)}
                </div>
                
                <div class="role-badge">
                    ESTUDIANTE
                </div>
            </div>

            <div class="info-section">
                <h2 class="welcome-title">${msj}</h2>

                <div class="data-grid">
                    <div class="label">Nombre Completo:</div>
                    <div class="value">${estudiante.nombre} ${estudiante.apellido}</div>

                    <div class="label">Cédula / ID:</div>
                    <div class="value">${estudiante.codigoPersona}</div>

                    <div class="label">Matrícula:</div>
                    <div class="value" style="color: #005a9c; font-weight: bold;">${estudiante.codigoEstu}</div>

                    <div class="label">Carrera:</div>
                    <div class="value">${estudiante.codigoCarr.nombreCarr}</div>

                    <div class="label">Correo:</div>
                    <div class="value">${estudiante.email}</div>
                </div>
            </div>
        </div>

        <div class="mu-footer">
            <a href="srvUsuario?accion=cerrar" class="btn-logout">Cerrar Sesión</a>
        </div>
    </div>

</body>
</html>