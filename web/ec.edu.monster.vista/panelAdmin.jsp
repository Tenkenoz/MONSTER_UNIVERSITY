<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ec.edu.monster.modelo.Empleado"%>
<%
    // VALIDACIÓN DE SESIÓN
    Empleado empleado = (Empleado) session.getAttribute("empleado");
    if (empleado == null) {
        response.sendRedirect(request.getContextPath() + "/ec.edu.monster.vista/login.jsp");
        return;
    }
    String codRol = (empleado.getCodigoRol() != null) ? empleado.getCodigoRol().getCodigoRol() : "";
    if (!"ADM".equals(codRol) && !"SUP".equals(codRol)) {
        session.invalidate();
        response.sendRedirect(request.getContextPath() + "/ec.edu.monster.vista/login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel Administrativo | Monster University</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    
    <style>
        :root {
            --mu-blue: #003366;
            --mu-light-blue: #00509E;
            --mu-gold: #FFD700;
            --text-white: #f4f4f4;
            --text-dark: #333;
            --bg-light: #f4f7f6;
            --nav-height: 60px;
        }

        * { margin: 0; padding: 0; box-sizing: border-box; font-family: 'Segoe UI', Tahoma, sans-serif; }
        
        body { 
            background-color: var(--bg-light); 
            height: 100vh; 
            display: flex; 
            flex-direction: column; 
            overflow: hidden; 
        }

        /* --- NAVBAR SUPERIOR --- */
        .navbar {
            height: var(--nav-height);
            background-color: var(--mu-blue);
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 0 20px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.2);
            z-index: 1000;
            border-bottom: 3px solid var(--mu-gold);
        }

        .navbar-brand {
            font-size: 1.3rem;
            font-weight: bold;
            color: var(--text-white);
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .navbar-brand i { color: var(--mu-gold); font-size: 1.5rem; }

        /* MENÚ LISTA */
        .nav-list {
            display: flex;
            list-style: none;
            height: 100%;
            margin-left: 20px;
        }

        .nav-item {
            position: relative;
            height: 100%;
            display: flex;
            align-items: center;
        }

        .nav-link {
            color: var(--text-white);
            text-decoration: none;
            padding: 0 15px;
            height: 100%;
            display: flex;
            align-items: center;
            font-size: 0.95rem;
            transition: background 0.3s;
            cursor: pointer;
        }

        .nav-link:hover {
            background-color: rgba(255,255,255,0.1);
            color: var(--mu-gold);
        }
        
        .nav-link i { margin-right: 8px; }
        .nav-link .caret { margin-left: 5px; font-size: 0.7rem; }

        /* DROPDOWN */
        .dropdown-menu {
            display: none;
            position: absolute;
            top: 100%;
            left: 0;
            background-color: white;
            min-width: 220px;
            box-shadow: 0 8px 16px rgba(0,0,0,0.15);
            border-radius: 0 0 4px 4px;
            z-index: 2000;
        }

        .nav-item:hover .dropdown-menu { display: block; }

        .dropdown-header {
            padding: 8px 20px;
            font-size: 0.75rem;
            text-transform: uppercase;
            color: var(--mu-blue);
            font-weight: bold;
            background-color: #f9f9f9;
            border-bottom: 1px solid #eee;
        }

        .dropdown-item {
            display: block;
            padding: 10px 20px;
            color: var(--text-dark);
            text-decoration: none;
            font-size: 0.9rem;
            transition: background 0.2s;
            border-bottom: 1px solid #f0f0f0;
        }

        .dropdown-item:hover { background-color: #f0f4ff; color: var(--mu-blue); }
        .dropdown-item i { width: 20px; color: var(--mu-light-blue); margin-right: 10px; text-align: center; }

        /* PERFIL USUARIO */
        .user-profile {
            display: flex;
            align-items: center;
            gap: 15px;
            color: var(--text-white);
        }
        
        .user-info { text-align: right; line-height: 1.2; }
        .user-name { font-weight: bold; font-size: 0.9rem; }
        .user-role { font-size: 0.75rem; opacity: 0.8; }
        
        .avatar {
            width: 35px; height: 35px;
            background-color: var(--mu-gold);
            color: var(--mu-blue);
            border-radius: 50%;
            display: flex; align-items: center; justify-content: center;
            font-weight: bold;
        }

        .logout-btn { color: #ff6b6b; font-size: 1.1rem; text-decoration: none; margin-left: 10px; }

        /* AREA CONTENIDO */
        #content-area {
            flex: 1;
            position: relative;
            background: white;
        }
        
        .content-frame {
            width: 100%;
            height: 100%;
            border: none;
            display: block;
        }

        .welcome-msg {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100%;
            color: #aaa;
        }
        .welcome-msg i { font-size: 5rem; color: var(--mu-blue); opacity: 0.1; margin-bottom: 20px; }
    </style>
</head>
<body>

    <nav class="navbar">
        <div class="navbar-brand">
            <i class="fas fa-university"></i> MONSTER UNIV
        </div>

        <ul class="nav-list">
            <li class="nav-item">
                <a class="nav-link">
                    <i class="fas fa-folder"></i> Archivo <i class="fas fa-chevron-down caret"></i>
                </a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" onclick="cargarVista('Importar')"><i class="fas fa-file-import"></i> 1.1 Importar datos</a>
                    <a class="dropdown-item" onclick="cargarVista('Exportar')"><i class="fas fa-file-export"></i> 1.2 Exportar datos</a>
                </div>
            </li>

            <li class="nav-item">
                <a class="nav-link">
                    <i class="fas fa-cogs"></i> Administración <i class="fas fa-chevron-down caret"></i>
                </a>
                <div class="dropdown-menu">
                    <div class="dropdown-header">Académico</div>
                    <a class="dropdown-item" onclick="cargarVista('Departamentos')"><i class="fas fa-building"></i> 2.1 Departamento</a>
                    <a class="dropdown-item" onclick="cargarVista('Cursos')"><i class="fas fa-book"></i> 2.2 Cursos</a>
                    <a class="dropdown-item" onclick="cargarVista('Asignar')"><i class="fas fa-tasks"></i> 2.3 Asignar</a>
                    
                    <div class="dropdown-header">Seguridad</div>
                    <a class="dropdown-item" onclick="cargarUsuariosCrud()"><i class="fas fa-users"></i> 2.4 Usuarios</a>
                    
                    <a class="dropdown-item" onclick="cargarRolesSeguridad()"><i class="fas fa-user-shield"></i> 2.5 Roles</a>
                    <a class="dropdown-item" onclick="cargarOpciones()"><i class="fas fa-user-shield"></i> 2.5 Opciones Perfil</a>
                </div>
            </li>

            <li class="nav-item">
                <a class="nav-link">
                    <i class="fas fa-sync"></i> Procesos <i class="fas fa-chevron-down caret"></i>
                </a>
                <div class="dropdown-menu">
                    <div class="dropdown-header">Académico</div>
                    <a class="dropdown-item" onclick="cargarVista('Matricula')"><i class="fas fa-graduation-cap"></i> 3.1 Matrícula</a>
                    <div class="dropdown-header">Seguridad</div>
                    <a class="dropdown-item" onclick="cargarVista('Perfil')"><i class="fas fa-id-badge"></i> 3.2 Asignar Perfil</a>
                    <a class="dropdown-item" onclick="cargarVista('Clave')"><i class="fas fa-key"></i> 3.3 Cambiar clave</a>
                </div>
            </li>

            <li class="nav-item">
                <a class="nav-link">
                    <i class="fas fa-chart-bar"></i> Reportes <i class="fas fa-chevron-down caret"></i>
                </a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" onclick="cargarReporteAcademico()">
                       <i class="fas fa-chart-line"></i> 4.1 Académico</a>
                    <a class="dropdown-item" onclick="cargarReporteSeguridad()"><i class="fas fa-shield-alt"></i> 4.2 Seguridad</a>
                </div>
            </li>
        </ul>

        <div class="user-profile">
            <div class="user-info">
                <div class="user-name"><%= empleado.getNombre() %> <%= empleado.getApellido() %></div>
                <div class="user-role"><%= "SUP".equals(codRol) ? "Super Admin" : "Administrador" %></div>
            </div>
            <div class="avatar"><%= empleado.getNombre().charAt(0) %></div>
            <a href="${pageContext.request.contextPath}/srvUsuario?accion=cerrar" class="logout-btn" title="Salir">
                <i class="fas fa-power-off"></i>
            </a>
        </div>
    </nav>

    <main id="content-area">
        <div class="welcome-msg">
            <i class="fas fa-university"></i>
            <h2>Bienvenido al Sistema Académico</h2>
            <p>Seleccione una opción del menú superior.</p>
        </div>
    </main>

    <script>
        function cargarUsuariosCrud() {
            // Carga la lista de usuarios (srvPersona -> UsuariosCrud.jsp)
            document.getElementById('content-area').innerHTML = 
                `<iframe src="${pageContext.request.contextPath}/srvPersona?accion=listar" class="content-frame"></iframe>`;
        }

        function cargarRolesSeguridad() {
            // Carga la gestión de roles (srvSeguridad -> seguridadAdmin.jsp)
            document.getElementById('content-area').innerHTML = 
                `<iframe src="${pageContext.request.contextPath}/srvSeguridad?accion=listarInvitados" class="content-frame"></iframe>`;
        }
        
       function cargarOpciones(){
           document.getElementById('content-area').innerHTML = 
                `<iframe src="${pageContext.request.contextPath}/srvSeguridad?accion=verOpciones" class="content-frame"></iframe>`;
       }
       
        function cargarReporteSeguridad() {
            // Carga la gestión de roles (srvSeguridad -> seguridadAdmin.jsp)
            document.getElementById('content-area').innerHTML = 
                `<iframe src="${pageContext.request.contextPath}/srvPersona?accion=listarReporte" class="content-frame"></iframe>`;
        }
        
      function cargarReporteAcademico() {
            // 4.1 Académico -> ServletEstudiante (Listar y PDF)
            // Se usa accion=listar para mostrar la tabla inicial
            document.getElementById('content-area').innerHTML = 
                `<iframe src="${pageContext.request.contextPath}/ServletEstudiante?accion=listar" class="content-frame"></iframe>`;
        }

        function cargarVista(nombre) {
            document.getElementById('content-area').innerHTML = `
                <div class="welcome-msg">
                    <i class="fas fa-tools" style="font-size:4rem; color:#f0ad4e; opacity:0.5;"></i>
                    <h2 style="color:#333; margin-top:20px;">En Construcción</h2>
                    <p>Módulo: <strong>${nombre}</strong></p>
                </div>`;
        }
    </script>
</body>
</html>