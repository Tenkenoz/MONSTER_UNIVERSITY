<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Monsters University | Secretaría Académica</title>
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/Navbar.css"/>
</head>
<body>

    <nav class="mu-navbar">
        <ul>
            <li>
                <a href="${pageContext.request.contextPath}/ec.edu.monster.vista/Secretaria/Inicio.jsp" target="contenido-dinamico">INICIO</a>
            </li>

            <li>
                <a href="#">ESTUDIANTES</a>
                <ul>
                    <li>
                        <a href="#">➕ REGISTRO DE ESTUDIANTES ▶</a>
                        <ul>
                            <li><a href="form_estudiante.jsp" target="contenido-dinamico">📝 FORMULARIO DE INGRESO</a></li>
                            <li><a href="validar_datos_est.jsp" target="contenido-dinamico">✅ VALIDACIÓN DE DATOS</a></li>
                            <li><a href="gen_credencial_est.jsp" target="contenido-dinamico">🎓 GENERACIÓN DE CREDENCIALES</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">🔍 GESTIÓN DE ESTUDIANTES ▶</a>
                        <ul>
                            <li><a href="mod_estudiante.jsp" target="contenido-dinamico">✏️ MODIFICAR DATOS</a></li>
                            <li><a href="consultar_estudiante.jsp" target="contenido-dinamico">👁️ CONSULTAR INFORMACIÓN</a></li>
                            <li><a href="eliminar_estudiante.jsp" target="contenido-dinamico">🚫 ELIMINAR REGISTRO</a></li>
                            <li><a href="listado_estudiantes.jsp" target="contenido-dinamico">📋 LISTADOS COMPLETOS</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">📄 DOCUMENTOS Y CONSTANCIAS ▶</a>
                        <ul>
                            <li><a href="constancia_estudios.jsp" target="contenido-dinamico">🎓 CONSTANCIA DE ESTUDIOS</a></li>
                            <li><a href="certificado_notas.jsp" target="contenido-dinamico">📜 CERTIFICADO DE NOTAS</a></li>
                            <li><a href="otros_docs.jsp" target="contenido-dinamico">📋 OTROS DOCUMENTOS</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">DOCENTES</a>
                <ul>
                    <li>
                        <a href="#">➕ REGISTRO DE DOCENTES ▶</a>
                        <ul>
                            <li><a href="form_docente.jsp" target="contenido-dinamico">📝 FORMULARIO DE INGRESO</a></li>
                            <li><a href="validar_datos_doc.jsp" target="contenido-dinamico">✅ VALIDACIÓN DE DATOS</a></li>
                            <li><a href="gen_credencial_doc.jsp" target="contenido-dinamico">🎓 GENERACIÓN DE CREDENCIALES</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">🔍 GESTIÓN DE DOCENTES ▶</a>
                        <ul>
                            <li><a href="mod_docente.jsp" target="contenido-dinamico">✏️ MODIFICAR DATOS</a></li>
                            <li><a href="consultar_docente.jsp" target="contenido-dinamico">👁️ CONSULTAR INFORMACIÓN</a></li>
                            <li><a href="listado_docentes.jsp" target="contenido-dinamico">📋 LISTADO DE DOCENTES</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">📅 ASIGNACIÓN DE HORARIOS ▶</a>
                        <ul>
                            <li><a href="asignar_grupos_doc.jsp" target="contenido-dinamico">👥 ASIGNAR A GRUPOS</a></li>
                            <li><a href="gestionar_horarios_doc.jsp" target="contenido-dinamico">📅 GESTIONAR HORARIOS</a></li>
                            <li><a href="rep_asignaciones.jsp" target="contenido-dinamico">📄 REPORTE DE ASIGNACIONES</a></li>
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
                            <li><a href="mod_periodo.jsp" target="contenido-dinamico">✏️ MODIFICAR PERÍODO</a></li>
                            <li><a href="lista_periodos.jsp" target="contenido-dinamico">📋 LISTA DE PERÍODOS</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">👥 GESTIÓN DE GRUPOS ▶</a>
                        <ul>
                            <li><a href="crear_grupo.jsp" target="contenido-dinamico">➕ CREAR NUEVO GRUPO</a></li>
                            <li><a href="mod_grupo.jsp" target="contenido-dinamico">✏️ MODIFICAR GRUPO</a></li>
                            <li><a href="asignar_doc_grupo.jsp" target="contenido-dinamico">👨‍🏫 ASIGNAR DOCENTES</a></li>
                            <li><a href="control_cupos.jsp" target="contenido-dinamico">📊 CONTROL DE CUPOS</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">✅ VALIDACIÓN DE MATRÍCULAS ▶</a>
                        <ul>
                            <li><a href="validar_matricula.jsp" target="contenido-dinamico">✅ VALIDAR MATRÍCULAS</a></li>
                            <li><a href="revisar_excepciones.jsp" target="contenido-dinamico">⚠️ REVISAR EXCEPCIONES</a></li>
                            <li><a href="listas_espera.jsp" target="contenido-dinamico">📋 LISTAS DE ESPERA</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">🔄 MODIFICACIONES DE MATRÍCULA ▶</a>
                        <ul>
                            <li><a href="mod_matricula_ind.jsp" target="contenido-dinamico">🔄 MODIFICAR MATRÍCULAS</a></li>
                            <li><a href="autorizar_retiros.jsp" target="contenido-dinamico">🚫 AUTORIZAR RETIROS</a></li>
                            <li><a href="historial_cambios_mat.jsp" target="contenido-dinamico">📄 HISTORIAL DE CAMBIOS</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">FINANZAS</a>
                <ul>
                    <li>
                        <a href="#">💰 GESTIÓN DE PAGOS ▶</a>
                        <ul>
                            <li><a href="registrar_pago.jsp" target="contenido-dinamico">🧾 REGISTRAR PAGOS</a></li>
                            <li><a href="anular_pago.jsp" target="contenido-dinamico">❌ ANULAR PAGOS</a></li>
                            <li><a href="reportes_financieros.jsp" target="contenido-dinamico">📊 REPORTES FINANCIEROS</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">🎓 GESTIÓN DE BECAS ▶</a>
                        <ul>
                            <li><a href="aplicar_beca.jsp" target="contenido-dinamico">➕ APLICAR BECAS</a></li>
                            <li><a href="mod_beca.jsp" target="contenido-dinamico">✏️ MODIFICAR BECAS</a></li>
                            <li><a href="listado_becados.jsp" target="contenido-dinamico">📋 LISTADO DE BECADOS</a></li>
                        </ul>
                    </li>
                    <li><a href="facturacion.jsp" target="contenido-dinamico">📄 FACTURACIÓN</a></li>
                </ul>
            </li>

            <li>
                <a href="#">REPORTES</a>
                <ul>
                    <li>
                        <a href="#">📊 REPORTES ACADÉMICOS ▶</a>
                        <ul>
                            <li><a href="listados_gen_est.jsp" target="contenido-dinamico">📋 LISTADOS DE ESTUDIANTES</a></li>
                            <li><a href="estadisticas_mat.jsp" target="contenido-dinamico">📊 ESTADÍSTICAS DE MATRÍCULA</a></li>
                            <li><a href="reportes_carrera.jsp" target="contenido-dinamico">📄 REPORTES POR CARRERA</a></li>
                        </ul>
                    </li>
                    <li><a href="estadisticas_inst.jsp" target="contenido-dinamico">📈 ESTADÍSTICAS INSTITUCIONALES</a></li>
                    <li>
                        <a href="#">📄 DOCUMENTOS OFICIALES ▶</a>
                        <ul>
                            <li><a href="imprimir_docs.jsp" target="contenido-dinamico">🖨️ IMPRIMIR DOCUMENTOS</a></li>
                            <li><a href="exportar_datos.jsp" target="contenido-dinamico">📧 EXPORTAR DATOS</a></li>
                        </ul>
                    </li>
                </ul>
            </li>

            <li>
                <a href="#">SISTEMA</a>
                <ul>
                    <li>
                        <a href="#">⚙️ CONFIGURACIÓN ▶</a>
                        <ul>
                            <li><a href="params_inst.jsp" target="contenido-dinamico">🏛️ PARÁMETROS INSTITUCIONALES</a></li>
                            <li><a href="config_periodos.jsp" target="contenido-dinamico">📅 CONFIGURACIÓN DE PERÍODOS</a></li>
                            <li><a href="config_seguridad_sis.jsp" target="contenido-dinamico">🔐 CONFIGURACIÓN DE SEGURIDAD</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">🔧 HERRAMIENTAS ▶</a>
                        <ul>
                            <li><a href="importar_datos.jsp" target="contenido-dinamico">📥 IMPORTAR DATOS</a></li>
                            <li><a href="exportar_datos_sis.jsp" target="contenido-dinamico">📤 EXPORTAR DATOS</a></li>
                            <li><a href="busqueda_avanzada.jsp" target="contenido-dinamico">🔍 BÚSQUEDA AVANZADA</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="#">📋 MANTENIMIENTO ▶</a>
                        <ul>
                            <li><a href="respaldo_datos.jsp" target="contenido-dinamico">💾 RESPALDO DE DATOS</a></li>
                            <li><a href="limpieza_datos.jsp" target="contenido-dinamico">🧹 LIMPIEZA DE DATOS</a></li>
                            <li><a href="registros_sistema.jsp" target="contenido-dinamico">📋 REGISTROS DEL SISTEMA</a></li>
                        </ul>
                    </li>
                    
                    <li style="border-top: 1px solid #76c043; margin-top:5px;">
                        <a href="srvUsuario?accion=cerrar" style="color: #ff9999;">🚪 CERRAR SESIÓN</a>
                    </li>
                </ul>
            </li>

        </ul>
    </nav>

    <div class="workspace-container">
        <iframe 
            name="contenido-dinamico" 
            src="${pageContext.request.contextPath}/ec.edu.monster.vista/Secretaria/Inicio.jsp">
        </iframe>
    </div>

</body>
</html>