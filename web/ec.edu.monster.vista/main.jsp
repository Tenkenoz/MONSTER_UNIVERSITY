<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- CONFIGURACIÓN DE RUTAS --%>
<c:set var="pathBase" value="ec.edu.monster.vista/" /> 

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Panel Principal - Monster University</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    
    <style>
        /* == ESTILOS (Sin cambios) == */
        body, html { height: 100%; margin: 0; font-family: 'Segoe UI', sans-serif; overflow: hidden; display: flex; flex-direction: column; background-color: #f4f4f4;}
        
        .header {
            height: 60px; flex-shrink: 0;
            background: linear-gradient(to right, #2c3e50, #4ca1af);
            color: white; display: flex; align-items: center; justify-content: space-between; padding: 0 20px; z-index: 20;
        }
        .logo-area { font-size: 22px; font-weight: bold; }
        .user-area { display: flex; align-items: center; gap: 15px; }
        .user-info-text { display: flex; flex-direction: column; text-align: right; line-height: 1.2; }
        .ui-nombre { font-size: 14px; font-weight: bold; }
        .ui-rol { font-size: 11px; color: #dcdcdc; text-transform: uppercase; }
        .btn-logout { background: rgba(255,255,255,0.2); color: white; padding: 6px 12px; border-radius: 4px; text-decoration: none; }
        .btn-logout:hover { background: #c0392b; }

        .navbar { height: 45px; background-color: #34495e; display: flex; align-items: center; padding: 0 20px; z-index: 15; flex-shrink: 0; }
        .nav-list { list-style: none; padding: 0; margin: 0; display: flex; height: 100%; }
        .nav-item { position: relative; height: 100%; display: flex; align-items: center; }
        .nav-link-main { padding: 0 20px; color: #ecf0f1; cursor: pointer; height: 100%; display: flex; align-items: center; font-size: 14px; text-decoration: none; transition: 0.3s; }
        .nav-link-main:hover { background-color: #2c3e50; }
        
        .dropdown-menu { display: none; position: absolute; top: 45px; left: 0; background: white; min-width: 200px; box-shadow: 0 5px 15px rgba(0,0,0,0.2); list-style: none; padding: 0; border-top: 3px solid #e67e22; }
        .nav-item:hover .dropdown-menu { display: block; }
        .dropdown-item a { display: block; padding: 10px 15px; color: #333; text-decoration: none; border-bottom: 1px solid #eee; font-size: 13px; cursor: pointer;}
        .dropdown-item a:hover { background: #f8f9fa; color: #e67e22; padding-left: 20px; transition: 0.2s; }

        .content-wrapper { flex-grow: 1; background: white; width: 100%; height: 100%; }
        iframe { width: 100%; height: 100%; border: none; display: block; }
        .welcome-msg { text-align: center; margin-top: 50px; }
    </style>
</head>
<body>

    <div class="header">
        <div class="logo-area"><i class="fas fa-graduation-cap"></i> MONSTER UNIV</div>
        <div class="user-area">
            <div class="user-info-text">
                <span class="ui-nombre">${sessionScope.usuario.persona.nombre} ${sessionScope.usuario.persona.apellido}</span>
                <span class="ui-rol">${sessionScope.nombrePerfil}</span>
            </div>
            <a href="${pageContext.request.contextPath}/srvUsuario?accion=cerrar" class="btn-logout"><i class="fas fa-power-off"></i></a>
        </div>
    </div>

    <div class="navbar">
        <ul class="nav-list">
            <%-- MENÚ DINÁMICO --%>
            <c:forEach var="sis" items="${sessionScope.menuDinamico}">
                <li class="nav-item">
                    <div class="nav-link-main">
                        ${sis.nombre} <i class="fas fa-caret-down" style="margin-left:5px; font-size:10px;"></i>
                    </div>
                    <ul class="dropdown-menu">
                        <c:forEach var="opc" items="${sis.opciones}">
                            <li class="dropdown-item">
                                <%-- 
                                    AQUÍ ESTÁ LA MAGIA:
                                    En lugar de un href directo, llamamos a la función JS 'enrutarMenu'
                                    pasándole el nombre y la url para que decida qué función ejecutar.
                                --%>
                                <a onclick="enrutarMenu('${opc.nombre}', '${opc.url}')">
                                    <i class="fas fa-angle-right"></i> ${opc.nombre}
                                </a>
                            </li>
                        </c:forEach>
                    </ul>
                </li>
            </c:forEach>
            
            <c:if test="${empty sessionScope.menuDinamico}">
                <li class="nav-item"><div class="nav-link-main" style="color:#aaa;">Sin accesos</div></li>
            </c:if>
        </ul>
    </div>

    <div class="content-wrapper" id="content-area">
        <iframe name="contenidoFrame" id="contenidoFrame" src=""></iframe>
    </div>

<script>
    // -------------------------------------------------------------------------
    // 1. TUS FUNCIONES ESPECÍFICAS (Tal cual las pediste)
    // -------------------------------------------------------------------------
    
    function cargarUsuariosCrud() {
        // Carga la lista de usuarios (srvPersona)
        document.getElementById('content-area').innerHTML = 
            `<iframe src="${pageContext.request.contextPath}/srvPersona?accion=listar" class="content-frame" style="width:100%;height:100%;border:none;"></iframe>`;
    }

    function cargarRolesSeguridad() {
        // Carga la gestión de roles (srvSeguridad)
        document.getElementById('content-area').innerHTML = 
            `<iframe src="${pageContext.request.contextPath}/srvSeguridad?accion=listarInvitados" class="content-frame" style="width:100%;height:100%;border:none;"></iframe>`;
    }
    
    function cargarOpciones(){
        // Carga opciones de perfil
       document.getElementById('content-area').innerHTML = 
            `<iframe src="${pageContext.request.contextPath}/srvSeguridad?accion=verOpciones" class="content-frame" style="width:100%;height:100%;border:none;"></iframe>`;
    }
    
    function cargarReporteSeguridad() {
        // Carga reporte seguridad
        document.getElementById('content-area').innerHTML = 
            `<iframe src="${pageContext.request.contextPath}/srvPersona?accion=listarReporte" class="content-frame" style="width:100%;height:100%;border:none;"></iframe>`;
    }
    
    function cargarReporteAcademico() {
        // Carga reporte académico
        document.getElementById('content-area').innerHTML = 
            `<iframe src="${pageContext.request.contextPath}/ServletEstudiante?accion=listar" class="content-frame" style="width:100%;height:100%;border:none;"></iframe>`;
    }

    // -------------------------------------------------------------------------
    // 2. FUNCIÓN ENRUTADORA (EL CEREBRO DEL MENÚ)
    // Recibe el click del menú y decide a qué función llamar
    // -------------------------------------------------------------------------
    function enrutarMenu(nombre, urlOriginal) {
        console.log("Click en menú: " + nombre);

        // AQUI ES DONDE SE CUMPLE TU REQUERIMIENTO:
        // Si el nombre coincide, ejecuta TU función específica.
        if (nombre === 'Usuarios') {
            cargarUsuariosCrud();
        } 
        else if (nombre === 'Roles') {
            cargarRolesSeguridad();
        } 
        else if (nombre === 'Opciones_Perfil' || nombre === 'Opciones Perfil') {
            cargarOpciones();
        }
        else if (nombre === 'Seguridad') { // Reporte de seguridad
            cargarReporteSeguridad();
        }
        else if (nombre === 'Académico' || nombre === 'Academico') { // Reporte academico
            cargarReporteAcademico();
        }
        else {
            // SI NO ES NINGUNA DE LAS ANTERIORES, CARGA NORMALMENTE
            // Esta parte mantiene funcionando el resto de opciones que no tienen función especial
            var contextPath = "${pageContext.request.contextPath}";
            var pathBase = "${pathBase}";
            var urlFinal = "";

            if(urlOriginal.startsWith('srv') || urlOriginal.startsWith('Servlet')) {
                urlFinal = contextPath + "/" + urlOriginal;
            } else {
                urlFinal = contextPath + "/" + pathBase + urlOriginal;
            }
            
            document.getElementById('content-area').innerHTML = 
                `<iframe src="` + urlFinal + `" class="content-frame" style="width:100%;height:100%;border:none;"></iframe>`;
        }
    }

    // -------------------------------------------------------------------------
    // 3. CARGA INICIAL (DASHBOARD)
    // -------------------------------------------------------------------------
    document.addEventListener("DOMContentLoaded", function() {
        var codPerfil = "${sessionScope.codPerfil}"; 
        var pathBase = "${pathBase}";
        var contextPath = "${pageContext.request.contextPath}";
        var paginaDashboard = "Inicio.jsp";

        switch(codPerfil) {
            case 'ADM': paginaDashboard = "Administrador/Inicio.jsp"; break;
            case 'EST': paginaDashboard = "Estudiante/Inicio.jsp"; break;
            case 'DOC': paginaDashboard = "Docente/Inicio.jsp"; break;
            case 'SEC': paginaDashboard = "Secretaria/Inicio.jsp"; break;
            case 'SUP': paginaDashboard = "panelAdmin.jsp"; break;
            case 'INV': paginaDashboard = "Invitados.jsp"; break;
        }

        var rutaCompleta = contextPath + "/" + pathBase + paginaDashboard;
        var iframe = document.getElementById("contenidoFrame");
        if(iframe) iframe.src = rutaCompleta;
    });
</script>

</body>
</html>