<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Monsters University | Portal Administrador</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Navbar.css"/>
</head>
<body>

    <nav class="mu-navbar">
        <ul>
            <li>
                <a href="${pageContext.request.contextPath}/ec.edu.monster.vista/Administrador/Inicio.jsp" target="contenido-dinamico">Inicio</a>
            </li>

            <li>
                <a href="#">USUARIOS</a>
                <ul>
                    <li>
                        <a href="#">👤 GESTIÓN DE USUARIOS ▶</a>
                        <ul>
                            <li><a href="crear_usuario.jsp" target="contenido-dinamico">➕ CREAR USUARIO</a></li>
                            <li><a href="buscar_usuario.jsp" target="contenido-dinamico">🔍 BUSCAR USUARIO</a></li>
                            <li><a href="modificar_usuario.jsp" target="contenido-dinamico">✏️ MODIFICAR USUARIO</a></li>
                            <li><a href="desactivar_usuario.jsp" target="contenido-dinamico">🚫 DESACTIVAR USUARIO</a></li>
                            <li><a href="reporte_usuarios.jsp" target="contenido-dinamico">📊 REPORTE DE USUARIOS</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">🎭 ROLES Y PERMISOS ▶</a>
                        <ul>
                            <li><a href="crear_rol.jsp" target="contenido-dinamico">➕ CREAR NUEVO ROL</a></li>
                            <li><a href="modificar_rol.jsp" target="contenido-dinamico">✏️ MODIFICAR ROL</a></li>
                            <li><a href="asignar_permisos.jsp" target="contenido-dinamico">🔧 ASIGNAR PERMISOS</a></li>
                            <li><a href="lista_roles.jsp" target="contenido-dinamico">📋 LISTA DE ROLES</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">🔐 SEGURIDAD Y ACCESOS ▶</a>
                        <ul>
                            <li><a href="politicas_pass.jsp" target="contenido-dinamico">📋 POLÍTICAS DE CONTRASEÑA</a></li>
                            <li><a href="historial_accesos.jsp" target="contenido-dinamico">📊 HISTORIAL DE ACCESOS</a></li>
                            <li><a href="config_seguridad.jsp" target="contenido-dinamico">⚙️ CONFIGURACIÓN DE SEGURIDAD</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">MATRÍCULAS</a>
                <ul>
                    <li>
                        <a href="#">📅 PERÍODOS ACADÉMICOS ▶</a>
                        <ul>
                            <li><a href="crear_periodo.jsp" target="contenido-dinamico">➕ CREAR PERÍODO</a></li>
                            <li><a href="config_periodo.jsp" target="contenido-dinamico">✏️ CONFIGURAR PERÍODO</a></li>
                            <li><a href="calendario.jsp" target="contenido-dinamico">📅 CALENDARIO ACADÉMICO</a></li>
                            <li><a href="lista_periodos.jsp" target="contenido-dinamico">📋 LISTA DE PERÍODOS</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">👥 CREACIÓN DE GRUPOS ▶</a>
                        <ul>
                            <li><a href="crear_grupo.jsp" target="contenido-dinamico">➕ CREAR NUEVO GRUPO</a></li>
                            <li><a href="asignar_docente.jsp" target="contenido-dinamico">👨‍🏫 ASIGNAR DOCENTE</a></li>
                            <li><a href="asignar_asignatura.jsp" target="contenido-dinamico">📚 ASIGNAR ASIGNATURA</a></li>
                            <li><a href="definir_horario.jsp" target="contenido-dinamico">⏰ DEFINIR HORARIO</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">📊 CONTROL DE CUPOS ▶</a>
                        <ul>
                            <li><a href="gestionar_cupos.jsp" target="contenido-dinamico">📊 GESTIONAR CUPOS</a></li>
                            <li><a href="listas_espera.jsp" target="contenido-dinamico">📋 LISTAS DE ESPERA</a></li>
                            <li><a href="asignacion_auto.jsp" target="contenido-dinamico">🔄 ASIGNACIÓN AUTOMÁTICA</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">ACADÉMICO</a>
                <ul>
                    <li>
                        <a href="#">🎓 CARRERAS ▶</a>
                        <ul>
                            <li><a href="crear_carrera.jsp" target="contenido-dinamico">➕ CREAR CARRERA</a></li>
                            <li><a href="mod_carrera.jsp" target="contenido-dinamico">✏️ MODIFICAR CARRERA</a></li>
                            <li><a href="des_carrera.jsp" target="contenido-dinamico">🚫 DESACTIVAR CARRERA</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">📖 ASIGNATURAS ▶</a>
                        <ul>
                            <li><a href="reg_asignatura.jsp" target="contenido-dinamico">➕ REGISTRAR ASIGNATURA</a></li>
                            <li><a href="mod_asignatura.jsp" target="contenido-dinamico">✏️ MODIFICAR ASIGNATURA</a></li>
                            <li><a href="prerrequisitos.jsp" target="contenido-dinamico">📋 DEFINIR PRERREQUISITOS</a></li>
                            <li><a href="lista_asignaturas.jsp" target="contenido-dinamico">📚 LISTA DE ASIGNATURAS</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">📋 PLANES ACADÉMICOS ▶</a>
                        <ul>
                            <li><a href="crear_plan.jsp" target="contenido-dinamico">➕ CREAR PLAN</a></li>
                            <li><a href="mod_plan.jsp" target="contenido-dinamico">✏️ MODIFICAR PLAN</a></li>
                            <li><a href="asociar_carrera.jsp" target="contenido-dinamico">🎓 ASOCIAR A CARRERAS</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">FINANZAS</a>
                <ul>
                    <li>
                        <a href="#">💵 CONFIG. ARANCELES ▶</a>
                        <ul>
                            <li><a href="conf_aranceles.jsp" target="contenido-dinamico">💰 CONFIGURAR ARANCELES</a></li>
                            <li><a href="mod_costos.jsp" target="contenido-dinamico">✏️ MODIFICAR COSTOS</a></li>
                            <li><a href="descuentos.jsp" target="contenido-dinamico">📊 ESTABLECER DESCUENTOS</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">⛔ BLOQUEOS POR DEUDA ▶</a>
                        <ul>
                            <li><a href="conf_bloqueos.jsp" target="contenido-dinamico">⚙️ CONFIGURAR BLOQUEOS</a></li>
                            <li><a href="estados_pago.jsp" target="contenido-dinamico">📊 GESTIONAR ESTADOS</a></li>
                            <li><a href="desbloquear.jsp" target="contenido-dinamico">🔓 DESBLOQUEAR ESTUDIANTES</a></li>
                        </ul>
                    </li>
                    <li><a href="conf_becas.jsp" target="contenido-dinamico">🎓 CONFIGURACIÓN DE BECAS</a></li>
                </ul>
            </li>

            <li>
                <a href="#">REPORTES</a>
                <ul>
                    <li>
                        <a href="#">📊 SISTEMA ▶</a>
                        <ul>
                            <li><a href="rep_uso.jsp" target="contenido-dinamico">📊 REPORTES DE USO</a></li>
                            <li><a href="logs.jsp" target="contenido-dinamico">📜 LOGS DEL SISTEMA</a></li>
                            <li><a href="rep_rendimiento.jsp" target="contenido-dinamico">📈 RENDIMIENTO</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">📈 ESTADÍSTICAS ▶</a>
                        <ul>
                            <li><a href="analisis_datos.jsp" target="contenido-dinamico">📊 ANÁLISIS DE DATOS</a></li>
                            <li><a href="tendencias.jsp" target="contenido-dinamico">📈 TENDENCIAS ACADÉMICAS</a></li>
                            <li><a href="metricas.jsp" target="contenido-dinamico">📋 MÉTRICAS DE RENDIMIENTO</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">📋 AUDITORÍA ▶</a>
                        <ul>
                            <li><a href="auditoria_completa.jsp" target="contenido-dinamico">📋 AUDITORÍA COMPLETA</a></li>
                            <li><a href="trazabilidad.jsp" target="contenido-dinamico">👤 TRAZABILIDAD</a></li>
                            <li><a href="registros_actividad.jsp" target="contenido-dinamico">📄 REGISTROS DE ACTIVIDAD</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">MANTENIMIENTO</a>
                <ul>
                    <li>
                        <a href="#">💾 BASE DE DATOS ▶</a>
                        <ul>
                            <li><a href="respaldo.jsp" target="contenido-dinamico">💾 RESPALDO Y RECUPERACIÓN</a></li>
                            <li><a href="optimizacion.jsp" target="contenido-dinamico">⚡ OPTIMIZACIÓN</a></li>
                            <li><a href="limpieza.jsp" target="contenido-dinamico">🧹 LIMPIEZA DE DATOS</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">⚙️ CONFIG. TÉCNICA ▶</a>
                        <ul>
                            <li><a href="parametros.jsp" target="contenido-dinamico">⚙️ PARÁMETROS DEL SISTEMA</a></li>
                            <li><a href="mantenimiento_cont.jsp" target="contenido-dinamico">🔄 MANTENIMIENTO CONTINUO</a></li>
                            <li><a href="actualizaciones.jsp" target="contenido-dinamico">📦 ACTUALIZACIONES</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">🔧 HERRAMIENTAS ▶</a>
                        <ul>
                            <li><a href="import_export.jsp" target="contenido-dinamico">📥 IMPORTACIÓN/EXPORTACIÓN</a></li>
                            <li><a href="diagnostico.jsp" target="contenido-dinamico">🔍 DIAGNÓSTICO</a></li>
                            <li><a href="notificaciones.jsp" target="contenido-dinamico">🔔 NOTIFICACIONES</a></li>
                        </ul>
                    </li>
                    <li style="border-top: 1px solid #c0392b; margin-top:5px;">
                        <a href="srvUsuario?accion=cerrar" style="color: #ff9999;">🚪 SALIR DEL SISTEMA</a>
                    </li>
                </ul>
            </li>
        </ul>
    </nav>

    <div class="workspace-container">
        <iframe 
            name="contenido-dinamico" 
            src="${pageContext.request.contextPath}/ec.edu.monster.vista/Administrador/Inicio.jsp">
        </iframe>
    </div>

</body>
</html>