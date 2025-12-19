<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Gestión de Usuarios</title>
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

        /* Botones Acción */
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
            <h2><i class="fas fa-users"></i> Gestión de Usuarios</h2>
        </div>
        <button onclick="abrirRegistrar()" class="btn-new">
            <i class="fas fa-user-plus"></i> Nuevo Usuario
        </button>
    </div>

    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Cédula</th>
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
                            <button class="btn-action btn-edit" title="Editar" 
                                    onclick="editar('${u.persona.codigoPersona}', '${u.persona.nombre}', '${u.persona.apellido}', 
                                                    '${u.login}', '${u.persona.email}', '${u.persona.celular}', 
                                                    '${u.persona.direccion}', '${u.persona.cargas}')">
                                <i class="fas fa-pen"></i>
                            </button>
                            <button class="btn-action btn-del" title="Eliminar" onclick="eliminar('${u.persona.codigoPersona}')">
                                <i class="fas fa-trash-alt"></i>
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty listaUsuarios}">
                    <tr><td colspan="6" style="text-align:center; padding:20px;">No hay datos.</td></tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <div id="modalRegistrar" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 style="margin:0;">Nuevo Usuario</h4>
                <span class="close" onclick="cerrarModals()">&times;</span>
            </div>
            <form action="${pageContext.request.contextPath}/srvPersona" method="POST">
                <input type="hidden" name="accion" value="registrarModal"> <div class="modal-body">
                    <div class="section-title">1. Datos de Identificación</div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Cédula:</label>
                            <input type="text" name="txtCedula" class="form-control" required pattern="[0-9]{10}" title="10 dígitos">
                        </div>
                        <div class="form-group">
                            <label>Email:</label>
                            <input type="email" name="txtEmail" class="form-control" required>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Nombres:</label>
                            <input type="text" name="txtNombre" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Apellidos:</label>
                            <input type="text" name="txtApellido" class="form-control" required>
                        </div>
                    </div>

                    <div class="section-title">2. Información Personal</div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Fecha Nacimiento:</label>
                            <input type="date" name="txtFechaNac" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Cargas Familiares:</label>
                            <input type="number" name="txtCargas" class="form-control" value="0" min="0">
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Género:</label>
                            <select name="cboSexo" class="form-control" required>
                                <option value="M">Masculino</option>
                                <option value="F">Femenino</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label>Estado Civil:</label>
                            <select name="cboEstadoCivil" class="form-control" required>
                                <option value="SOL">Soltero</option>
                                <option value="CAS">Casado</option>
                                <option value="DIV">Divorciado</option>
                                <option value="VIU">Viudo</option>
                            </select>
                        </div>
                    </div>

                    <div class="section-title">3. Seguridad y Contacto</div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Dirección:</label>
                            <input type="text" name="txtDireccion" class="form-control" required>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Celular:</label>
                            <input type="text" name="txtCelular" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Teléfono Fijo:</label>
                            <input type="text" name="txtTelDom" class="form-control" required>
                        </div>
                    </div>
                    
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn-cancel" onclick="cerrarModals()">Cancelar</button>
                    <button type="submit" class="btn-save">Registrar</button>
                </div>
            </form>
        </div>
    </div>

    <div id="modalEditar" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <h4 style="margin:0;">Editar Usuario</h4>
                <span class="close" onclick="cerrarModals()">&times;</span>
            </div>
            <form action="${pageContext.request.contextPath}/srvPersona" method="POST">
                <input type="hidden" name="accion" value="editar">
                <input type="hidden" name="txtCodigoEdit" id="txtCodigoEdit">
                
                <div class="modal-body">
                    <div class="form-row">
                        <div class="form-group">
                            <label>Login:</label>
                            <input type="text" name="txtLoginEdit" id="txtLoginEdit" class="form-control" readonly>
                        </div>
                        <div class="form-group">
                            <label>Email:</label>
                            <input type="email" name="txtEmailEdit" id="txtEmailEdit" class="form-control" required>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Nombres:</label>
                            <input type="text" name="txtNombreEdit" id="txtNombreEdit" class="form-control" required>
                        </div>
                        <div class="form-group">
                            <label>Apellidos:</label>
                            <input type="text" name="txtApellidoEdit" id="txtApellidoEdit" class="form-control" required>
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group">
                            <label>Celular:</label>
                            <input type="text" name="txtCelularEdit" id="txtCelularEdit" class="form-control">
                        </div>
                        <div class="form-group">
                            <label>Cargas:</label>
                            <input type="number" name="txtCargasEdit" id="txtCargasEdit" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label>Dirección:</label>
                        <input type="text" name="txtDireccionEdit" id="txtDireccionEdit" class="form-control">
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn-cancel" onclick="cerrarModals()">Cancelar</button>
                    <button type="submit" class="btn-save">Guardar Cambios</button>
                </div>
            </form>
        </div>
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
                        <label>Cédula:</label>
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
                    <label>Dirección:</label>
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
            if(confirm('¿Eliminar PERMANENTEMENTE a este usuario? Se borrará todo.')) {
                window.location.href = '${pageContext.request.contextPath}/srvPersona?accion=eliminar&id=' + codigo;
            }
        }

        function abrirRegistrar() {
            document.getElementById('modalRegistrar').style.display = 'flex';
        }

        function editar(codigo, nombre, apellido, login, email, celular, direccion, cargas) {
            document.getElementById('txtCodigoEdit').value = codigo;
            document.getElementById('txtNombreEdit').value = nombre;
            document.getElementById('txtApellidoEdit').value = apellido;
            document.getElementById('txtLoginEdit').value = login;
            document.getElementById('txtEmailEdit').value = email;
            document.getElementById('txtCelularEdit').value = celular;
            document.getElementById('txtDireccionEdit').value = direccion;
            document.getElementById('txtCargasEdit').value = cargas;
            document.getElementById('modalEditar').style.display = 'flex';
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
            document.getElementById('modalRegistrar').style.display = 'none';
            document.getElementById('modalEditar').style.display = 'none';
            document.getElementById('modalVisualizar').style.display = 'none';
        }
        
        window.onclick = function(e) {
            if (e.target.className === 'modal') {
                cerrarModals();
            }
        }
    </script>
</body>
</html>