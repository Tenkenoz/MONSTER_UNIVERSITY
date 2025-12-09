<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%-- Asegúrate de que el objeto 'estudiante' esté en sessionScope --%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Inicio</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/InEstudiante.css">
</head>
<body>

    <div class="mu-card">
        <div class="mu-header">
            <h1 class="mu-logo-text">Monsters University</h1>
            <span style="font-size: 0.9rem; letter-spacing: 2px; text-transform: uppercase;">Expediente Académico</span>
        </div>

        <div class="mu-body">
            <div class="profile-section">
                <div class="avatar-circle">
                    ${sessionScope.estudiante.nombre.charAt(0)}
                </div>
                <div class="role-badge">ESTUDIANTE ACTIVO</div>
            </div>

            <div class="info-section">
                <h2 style="color: #003366; margin-top: 0;">Bienvenido, ${sessionScope.estudiante.nombre}</h2>

                <div class="data-grid">
                    <div class="label">Nombre Completo</div>
                    <div class="value">${sessionScope.estudiante.nombre} ${sessionScope.estudiante.apellido}</div>

                    <div class="label">Cédula / ID</div>
                    <div class="value">${sessionScope.estudiante.codigoPersona}</div>

                    <div class="label">Matrícula</div>
                    <div class="value" style="color: #005a9c; font-weight: bold;">${sessionScope.estudiante.codigoEstu}</div>

                    <div class="label">Carrera</div>
                    <div class="value">${sessionScope.estudiante.codigoCarr.nombreCarr}</div>
                    
                    <div class="label">Email Inst.</div>
                    <div class="value">${sessionScope.estudiante.email}</div>
                </div>
            </div>
        </div>
    </div>

</body>
</html>