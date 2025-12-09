<%-- 
    Document   : ReporteUsuarios
    Created on : 8 dic 2025, 20:51:18
    Author     : erick
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>GestiÃ³n de Usuarios</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        
        :root { --mu-blue: #003366; --mu-gold: #FFD700; --bg-gray: #f4f6f9; --success: #28a745; --danger: #dc3545; }
        body { font-family: 'Segoe UI', Tahoma, sans-serif; background: var(--bg-gray); padding: 20px; margin: 0; }
        
        /* Header */
        .page-header { background: #fff; padding: 15px 20px; border-radius: 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); display: flex; justify-content: space-between; align-items: center; border-left: 5px solid var(--mu-blue); margin-bottom: 20px; }
        .page-title h2 { margin: 0; color: var(--mu-blue); font-size: 1.2rem; }
        
        .btn-new { background: var(--success); color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; font-weight: bold; display: flex; align-items: center; gap: 5px; text-decoration: none; }
        .btn-new:hover { background: #218838; }

        /* Tabla */
        .table-wrap { background: #fff; border-radius: 5px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); overflow: hidden; }
        table { width: 100%; border-collapse: collapse; }
        th { background: #f8f9fa; text-align: left; padding: 12px 15px; font-size: 0.85rem; font-weight: 700; color: #555; border-bottom: 2px solid #ddd; text-transform: uppercase; }
        td { padding: 12px 15px; border-bottom: 1px solid #eee; font-size: 0.9rem; color: #333; vertical-align: middle; }
        tbody tr:hover { background: #f1f3f5; }

        /* Badges */
        .badge { padding: 4px 8px; border-radius: 4px; font-size: 0.75rem; font-weight: 700; color: white; }
        .bg-act { background: var(--success); } .bg-inact { background: #6c757d; }

        /* Botones AcciÃ³n */
        .btn-action { border: none; width: 30px; height: 30px; border-radius: 4px; cursor: pointer; color: white; margin-right: 2px; }
        .btn-view { background: #17a2b8; } .btn-edit { background: #007bff; } .btn-del { background: var(--danger); }

        /* MODALES */
        .modal { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 999; justify-content: center; padding-top: 20px; overflow-y: auto; }
        .modal-content { background: white; width: 650px; max-width: 95%; border-radius: 5px; box-shadow: 0 5px 15px rgba(0,0,0,0.3); margin-bottom: 50px; animation: slide 0.3s; }
        @keyframes slide { from { transform: translateY(-20px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
        
        .modal-header { background: var(--mu-blue); color: white; padding: 12px 20px; display: flex; justify-content: space-between; align-items: center; border-radius: 5px 5px 0 0; }
        .modal-body { padding: 20px; }
        .modal-footer { padding: 15px 20px; background: #f8f9fa; text-align: right; border-top: 1px solid #eee; border-radius: 0 0 5px 5px; }
        
        .close { cursor: pointer; font-size: 1.5rem; }
        .form-row { display: flex; gap: 15px; margin-bottom: 12px; } .form-group { flex: 1; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: 600; font-size: 0.85rem; color: #555; }
        .form-control { width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; }
        .form-control:read-only { background: #e9ecef; }
        .section-title { color: var(--mu-blue); font-weight: bold; border-bottom: 2px solid #eee; padding-bottom: 5px; margin: 15px 0 10px 0; font-size: 0.95rem; }

        .btn-save { background: var(--mu-blue); color: white; padding: 8px 20px; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; }
        .btn-cancel { background: #6c757d; color: white; padding: 8px 15px; border: none; border-radius: 4px; cursor: pointer; margin-right: 10px; }
        .alert { padding: 10px 15px; margin-bottom: 15px; border-radius: 4px; font-weight: 500; font-size: 0.9rem; }
        .alert-ok { background: #d4edda; color: #155724; } .alert-err { background: #f8d7da; color: #721c24; }
    /* Botón PDF */
.btn-pdf {
    background: #e63946; /* Rojo más vivo */
    color: white;
    font-size: 1rem;
    border: none;
    width: 35px;
    height: 35px;
    border-radius: 6px;
    cursor: pointer;
    margin-left: 4px;
    display: inline-flex;
    align-items: center;
    justify-content: center;
}

.btn-pdf:hover {
    background: #b2232f; /* Más oscuro al pasar el mouse */
}

    </style>
</head>
<body>

    <c:if test="${not empty msj}">
        <div class="alert ${tipo == 'OK' ? 'alert-ok' : 'alert-err'}">
            ${msj}
        </div>
    </c:if>

    <div class="page-header">
        <div class="page-title">
            <h2><i class="fas fa-users"></i> Reporte de Usuarios</h2>
        </div>
    </div>

    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>CÃ©dula</th>
                    <th>Nombres y Apellidos</th>
                    <th>Usuario / Email</th>
                    <th>Fecha</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${listaUsuarios}">
                    <tr>
                        <td style="font-weight: bold;">${u.persona.cedula}</td>
                        <td>${u.persona.apellido} ${u.persona.nombre}</td>
                        <td>
                            <div style="font-weight: 600; color: var(--mu-blue);">${u.login}</div>
                            <div style="font-size: 0.8rem; color: #777;">${u.persona.email}</div>
                        </td>
                        <td><fmt:formatDate value="${u.fechaCrea}" pattern="dd/MM/yyyy" /></td>
                        <td>
                            <c:choose>
                                <c:when test="${u.codEstado.codEstado eq '1'}"><span class="badge bg-act">Activo</span></c:when>
                                <c:otherwise><span class="badge bg-inact">Inactivo</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <button class="btn-action btn-view" title="Ver Detalles"
                                    onclick="visualizar('${u.persona.cedula}', '${u.persona.nombre}', '${u.persona.apellido}', 
                                            '${u.persona.email}', '${u.persona.celular}', '${u.persona.direccion}', 
                                            '${u.persona.cargas}', '${u.login}', '${u.fechaCrea}')">
                                <i class="fas fa-eye"></i>
                            </button>
                                
                                <a href="${pageContext.request.contextPath}/srvPersona?accion=reporteUsuario&codigo=${u.persona.codigoPersona}"
     
         class="btn-pdf"
       title="Generar PDF">
       <i class="fa-solid fa-file-pdf"></i>
    </a>

                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty listaUsuarios}">
                    <tr><td colspan="6" style="text-align:center; padding:20px;">No hay datos.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>
 
    <div id="modalVisualizar" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 style="margin:0;"><i class="fas fa-eye"></i> Detalle de Usuario</h4>
                <span class="close" onclick="cerrarModals()">&times;</span>
            </div>
            <div class="modal-body">
                <div class="form-row">
                    <div class="form-group">
                        <label>CÃ©dula:</label>
                        <input type="text" id="viewCedula" class="form-control" readonly>
                    </div>
                    <div class="form-group">
                        <label>Fecha Registro:</label>
                        <input type="text" id="viewFecha" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Nombres:</label>
                        <input type="text" id="viewNombre" class="form-control" readonly>
                    </div>
                    <div class="form-group">
                        <label>Apellidos:</label>
                        <input type="text" id="viewApellido" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Usuario (Login):</label>
                        <input type="text" id="viewLogin" class="form-control" readonly style="font-weight:bold; color:#003366;">
                    </div>
                    <div class="form-group">
                        <label>Email:</label>
                        <input type="text" id="viewEmail" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Celular:</label>
                        <input type="text" id="viewCelular" class="form-control" readonly>
                    </div>
                    <div class="form-group">
                        <label>Cargas:</label>
                        <input type="text" id="viewCargas" class="form-control" readonly>
                    </div>
                </div>
                <div class="form-group">
                    <label>DirecciÃ³n:</label>
                    <input type="text" id="viewDireccion" class="form-control" readonly>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn-save" onclick="cerrarModals()">Cerrar</button>
            </div>
            

        </div>
        
        
        
    </div>

    <script>
        function eliminar(codigo) {
            if(confirm('Â¿Eliminar PERMANENTEMENTE a este usuario? Se borrarÃ¡ todo.')) {
                window.location.href = '${pageContext.request.contextPath}/srvPersona?accion=eliminar&id=' + codigo;
            }
        }
        
     


        function visualizar(cedula, nombre, apellido, email, celular, direccion, cargas, login, fecha) {
            document.getElementById('viewCedula').value = cedula;
            document.getElementById('viewNombre').value = nombre;
            document.getElementById('viewApellido').value = apellido;
            document.getElementById('viewEmail').value = email;
            document.getElementById('viewCelular').value = celular;
            document.getElementById('viewDireccion').value = direccion;
            document.getElementById('viewCargas').value = cargas;
            document.getElementById('viewLogin').value = login;
            document.getElementById('viewFecha').value = fecha;
            document.getElementById('modalVisualizar').style.display = 'flex';
        }

        function cerrarModals() {
            document.getElementById('modalVisualizar').style.display = 'none';
        }
        
        window.onclick = function(e) {
            if (e.target.className === 'modal') {
                cerrarModals();
            }
        };
    </script>
</body>
</html>