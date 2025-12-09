<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Monsters University | Portal Docente</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
     <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Navbar.css"/>
</head>
<body>

    <nav class="mu-navbar">
        <ul>
            <li>
                <a href="${pageContext.request.contextPath}/ec.edu.monster.vista/Docente/Inicio.jsp" target="contenido-dinamico">INICIO</a>
            </li>

            <li>
                <a href="#">MIS ASIGNATURAS</a>
                <ul>
                    <li>
                        <a href="#">📚 VER ASIGNATURAS ASIGNADAS ▶</a>
                        <ul>
                            <li><a href="${pageContext.request.contextPath}/ec.edu.monster.vista/Secretaria/Inicio.jsp" target="contenido-dinamico">📋 LISTA DE ASIGNATURAS</a></li>
                            <li><a href="grupos_asignados.jsp" target="contenido-dinamico">👥 GRUPOS ASIGNADOS</a></li>
                            <li><a href="aulas_asignadas.jsp" target="contenido-dinamico">🏫 AULAS ASIGNADAS</a></li>
                            <li><a href="horarios_consulta.jsp" target="contenido-dinamico">📅 HORARIOS DE CONSULTA</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">🗓️ GESTIÓN DE HORARIOS ▶</a>
                        <ul>
                            <li><a href="calendario_semanal.jsp" target="contenido-dinamico">🗓️ CALENDARIO SEMANAL</a></li>
                            <li><a href="horarios_asignados.jsp" target="contenido-dinamico">⏰ HORARIOS ASIGNADOS</a></li>
                            <li><a href="disponibilidad.jsp" target="contenido-dinamico">📋 DISPONIBILIDAD</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">📅 PLANIFICACIÓN ACADÉMICA ▶</a>
                        <ul>
                            <li><a href="plan_clases.jsp" target="contenido-dinamico">📝 PLAN DE CLASES</a></li>
                            <li><a href="cronograma_aca.jsp" target="contenido-dinamico">📅 CRONOGRAMA ACADÉMICO</a></li>
                            <li><a href="materiales_apoyo.jsp" target="contenido-dinamico">📚 MATERIALES DE APOYO</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">ESTUDIANTES</a>
                <ul>
                    <li>
                        <a href="#">👥 LISTAS DE ESTUDIANTES ▶</a>
                        <ul>
                            <li><a href="lista_por_grupo.jsp" target="contenido-dinamico">📋 POR GRUPO/ASIGNATURA</a></li>
                            <li><a href="buscar_estudiante.jsp" target="contenido-dinamico">🔍 BUSCAR ESTUDIANTE</a></li>
                            <li><a href="info_completa_est.jsp" target="contenido-dinamico">👁️ VER INFORMACIÓN COMPLETA</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">✅ REGISTRO DE ASISTENCIA ▶</a>
                        <ul>
                            <li><a href="registrar_asistencia.jsp" target="contenido-dinamico">📅 REGISTRAR POR FECHA</a></li>
                            <li><a href="ver_registros_asist.jsp" target="contenido-dinamico">📋 VER REGISTROS</a></li>
                            <li><a href="reportes_asistencia.jsp" target="contenido-dinamico">📄 REPORTES DE ASISTENCIA</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">📞 CONTACTO CON ESTUDIANTES ▶</a>
                        <ul>
                            <li><a href="enviar_correos.jsp" target="contenido-dinamico">📧 ENVIAR CORREOS</a></li>
                            <li><a href="notificaciones_est.jsp" target="contenido-dinamico">🔔 NOTIFICACIONES</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">CALIFICACIONES</a>
                <ul>
                    <li>
                        <a href="#">📝 INGRESAR CALIFICACIONES ▶</a>
                        <ul>
                            <li><a href="ingreso_grupo.jsp" target="contenido-dinamico">📋 POR ASIGNATURA Y GRUPO</a></li>
                            <li><a href="ingreso_parcial.jsp" target="contenido-dinamico">📝 POR PARCIAL/EVALUACIÓN</a></li>
                            <li><a href="guardar_calif.jsp" target="contenido-dinamico">💾 GUARDAR Y CONFIRMAR</a></li>
                        </ul>
                    </li>
                    <li><a href="modificar_calif.jsp" target="contenido-dinamico">✏️ MODIFICAR CALIFICACIONES</a></li>
                    <li>
                        <a href="#">📊 VER CALIFICACIONES ▶</a>
                        <ul>
                            <li><a href="consultar_calif.jsp" target="contenido-dinamico">📊 CONSULTAR CALIFICACIONES</a></li>
                            <li><a href="historial_calif.jsp" target="contenido-dinamico">📄 HISTORIAL DE CALIFICACIONES</a></li>
                            <li><a href="reporte_calif.jsp" target="contenido-dinamico">📋 REPORTE DE CALIFICACIONES</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">REPORTES</a>
                <ul>
                    <li>
                        <a href="#">📊 REPORTE ACADÉMICO ▶</a>
                        <ul>
                            <li><a href="rep_grupo.jsp" target="contenido-dinamico">📋 POR GRUPO</a></li>
                            <li><a href="rep_asignatura.jsp" target="contenido-dinamico">📊 POR ASIGNATURA</a></li>
                            <li><a href="rep_periodo.jsp" target="contenido-dinamico">📈 POR PERÍODO ACADÉMICO</a></li>
                        </ul>
                    </li>
                    <li><a href="estadisticas_grupo.jsp" target="contenido-dinamico">📈 ESTADÍSTICAS DE GRUPO</a></li>
                    <li>
                        <a href="#">📄 GENERAR LISTADOS ▶</a>
                        <ul>
                            <li><a href="imprimir_rep.jsp" target="contenido-dinamico">🖨️ IMPRIMIR REPORTES</a></li>
                            <li><a href="exportar_rep.jsp" target="contenido-dinamico">📧 EXPORTAR REPORTES</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">PERFIL</a>
                <ul>
                    <li>
                        <a href="#">👤 DATOS PROFESIONALES ▶</a>
                        <ul>
                            <li><a href="editar_info_prof.jsp" target="contenido-dinamico">✏️ EDITAR INFORMACIÓN</a></li>
                            <li><a href="titulos.jsp" target="contenido-dinamico">🎓 TÍTULOS Y ESPECIALIZACIONES</a></li>
                            <li><a href="disp_horaria.jsp" target="contenido-dinamico">📋 DISPONIBILIDAD HORARIA</a></li>
                        </ul>
                    </li>
                    <li><a href="contacto_prof.jsp" target="contenido-dinamico">📧 DATOS DE CONTACTO</a></li>
                    <li><a href="cambiar_pass_prof.jsp" target="contenido-dinamico">🔐 CAMBIAR CONTRASEÑA</a></li>
                    
                    <li style="border-top: 1px solid #43a047; margin-top:5px;">
                        <a href="srvUsuario?accion=cerrar" style="color: #ff9999;">🚪 CERRAR SESIÓN</a>
                    </li>
                </ul>
            </li>
        </ul>
    </nav>

    <div class="workspace-container">
        <iframe 
            name="contenido-dinamico" 
            src="${pageContext.request.contextPath}/ec.edu.monster.vista/Docente/Inicio.jsp">
        </iframe>
    </div>

</body>
</html>