<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Monsters University | Portal Estudiante</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Navbar.css"/>

</head>
<body>

    <nav class="mu-navbar">
        <ul>
            <li>
                <a href="${pageContext.request.contextPath}/ec.edu.monster.vista/Estudiante/Inicio.jsp" target="contenido-dinamico">INICIO</a>
            </li>

            <li>
                <a href="#">ACADÉMICO</a> <ul>
                    <li>
                        <a href="#">📚 PROCESO DE MATRÍCULA ▶</a>
                        <ul>
                            <li><a href="ver_asignaturas.jsp" target="contenido-dinamico">🔍 VER ASIGNATURAS DISPONIBLES</a></li>
                            <li><a href="ver_grupos.jsp" target="contenido-dinamico">👁️‍🗨️ VER GRUPOS Y HORARIOS</a></li>
                            <li><a href="validar_requisitos.jsp" target="contenido-dinamico">✅ VALIDAR PRERREQUISITOS</a></li>
                            <li><a href="seleccionar_grupos.jsp" target="contenido-dinamico">➕ SELECCIONAR GRUPOS</a></li>
                            <li><a href="confirmar_matricula.jsp" target="contenido-dinamico">📄 CONFIRMAR MATRÍCULA</a></li>
                            <li><a href="modificar_matricula.jsp" target="contenido-dinamico">🔄 MODIFICAR MATRÍCULA</a></li>
                            <li><a href="retirar_asignatura.jsp" target="contenido-dinamico">🚫 RETIRAR ASIGNATURAS</a></li>
                        </ul>
                    </li>

                    <li>
                        <a href="#">📋 INFORMACIÓN ACADÉMICA ▶</a>
                        <ul>
                            <li><a href="horario.jsp" target="contenido-dinamico">🗓️ VER MI HORARIO</a></li>
                            <li><a href="carga_academica.jsp" target="contenido-dinamico">📚 VER MI CARGA ACADÉMICA</a></li>
                            <li><a href="progreso.jsp" target="contenido-dinamico">📊 VER MI PROGRESO ACADÉMICO</a></li>
                            <li><a href="plan_estudios.jsp" target="contenido-dinamico">🎓 VER MI PLAN DE ESTUDIOS</a></li>
                        </ul>
                    </li>

                    <li>
                        <a href="#">📄 DOCUMENTOS ACADÉMICOS ▶</a>
                        <ul>
                            <li><a href="comprobante_matricula.jsp" target="contenido-dinamico">🧾 COMPROBANTE DE MATRÍCULA</a></li>
                            <li><a href="constancia_estudiante.jsp" target="contenido-dinamico">🎓 CONSTANCIA DE ESTUDIANTE</a></li>
                            <li><a href="constancia_matricula.jsp" target="contenido-dinamico">📜 CONSTANCIA DE MATRÍCULA</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">CALIFICACIONES</a>
                <ul>
                    <li>
                        <a href="#">📊 VER CALIFICACIONES ▶</a>
                        <ul>
                            <li><a href="notas_periodo.jsp" target="contenido-dinamico">📅 PERÍODO ACTUAL</a></li>
                            <li><a href="historial_completo.jsp" target="contenido-dinamico">📚 HISTORIAL COMPLETO</a></li>
                            <li><a href="notas_asignatura.jsp" target="contenido-dinamico">📝 POR ASIGNATURA</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">📈 REPORTES ACADÉMICOS ▶</a>
                        <ul>
                            <li><a href="boleta.jsp" target="contenido-dinamico">🎓 BOLETA DE CALIFICACIONES</a></li>
                            <li><a href="historial_reporte.jsp" target="contenido-dinamico">📋 HISTORIAL ACADÉMICO</a></li>
                            <li><a href="constancia_notas.jsp" target="contenido-dinamico">📜 CONSTANCIA DE NOTAS</a></li>
                        </ul>
                    </li>
                    <li><a href="estadisticas.jsp" target="contenido-dinamico">📊 ESTADÍSTICAS PERSONALES</a></li>
                </ul>
            </li>

            <li>
                <a href="#">PAGOS</a>
                <ul>
                    <li>
                        <a href="#">💰 FACTURAS PENDIENTES ▶</a>
                        <ul>
                            <li><a href="ver_facturas.jsp" target="contenido-dinamico">🧾 VER FACTURAS PENDIENTES</a></li>
                            <li><a href="historial_facturas.jsp" target="contenido-dinamico">📋 HISTORIAL DE FACTURAS</a></li>
                            <li><a href="estado_cuenta.jsp" target="contenido-dinamico">📊 ESTADO DE CUENTA</a></li>
                        </ul>
                    </li>
                    <li><a href="realizar_pago.jsp" target="contenido-dinamico">💳 REALIZAR PAGO</a></li>
                    <li>
                        <a href="#">📄 DOCUMENTOS FINANCIEROS ▶</a>
                        <ul>
                            <li><a href="comprobantes_pago.jsp" target="contenido-dinamico">🧾 COMPROBANTES DE PAGO</a></li>
                            <li><a href="estados_cuenta_doc.jsp" target="contenido-dinamico">📄 ESTADOS DE CUENTA</a></li>
                            <li><a href="solicitud_beca.jsp" target="contenido-dinamico">🎓 SOLICITUD DE BECAS</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">CONSULTAS</a>
                <ul>
                    <li><a href="consulta_plan.jsp" target="contenido-dinamico">📚 PLAN DE ESTUDIOS</a></li>
                    <li><a href="info_docentes.jsp" target="contenido-dinamico">👨‍🏫 INFORMACIÓN DE DOCENTES</a></li>
                    <li><a href="info_carrera.jsp" target="contenido-dinamico">🏛️ INFORMACIÓN DE CARRERA</a></li>
                </ul>
            </li>

            <li>
                <a href="#">PERFIL</a>
                <ul>
                    <li>
                        <a href="#">👤 DATOS PERSONALES ▶</a>
                        <ul>
                            <li><a href="editar_info.jsp" target="contenido-dinamico">✏️ EDITAR INFORMACIÓN</a></li>
                            <li><a href="cambiar_foto.jsp" target="contenido-dinamico">📸 CAMBIAR FOTO DE PERFIL</a></li>
                            <li><a href="config_notificaciones.jsp" target="contenido-dinamico">📱 CONFIG. NOTIFICACIONES</a></li>
                        </ul>
                    </li>
                    <li><a href="contacto.jsp" target="contenido-dinamico">📧 DATOS DE CONTACTO</a></li>
                    <li><a href="cambiar_pass.jsp" target="contenido-dinamico">🔐 CAMBIAR CONTRASEÑA</a></li>
                    
                    <li><a href="srvUsuario?accion=cerrar" style="color: #ff9999;">🚪 CERRAR SESIÓN</a></li>
                </ul>
            </li>
        </ul>
    </nav>

    <div class="workspace-container">
        <iframe 
            name="contenido-dinamico" 
            src="${pageContext.request.contextPath}/ec.edu.monster.vista/Estudiante/Inicio.jsp">
        </iframe>
    </div>

</body>
</html>