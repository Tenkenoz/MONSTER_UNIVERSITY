<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Monster University - Gestión</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <style>
        /* == ESTILOS GLOBALES == */
        body { background-color: #f4f4f4; font-family: Arial, sans-serif; text-align: center; padding-top: 20px; }
        h2 { font-family: "Times New Roman", Times, serif; font-weight: bold; text-transform: uppercase; letter-spacing: 1px; margin-bottom: 25px; color: #000; }

        /* == MARCO PRINCIPAL == */
        .main-container { width: 800px; margin: 0 auto; border: 1px solid #a0a0a0; padding: 8px; background-color: #fcfcfc; border-radius: 4px; }

        /* == BARRAS METÁLICAS == */
        .metal-bar { background: linear-gradient(to bottom, #f2f2f2 0%, #ebebeb 40%, #cccccc 100%); border: 1px solid #999; border-radius: 4px; padding: 6px; color: #000; font-weight: bold; font-size: 13px; text-transform: uppercase; text-align: center; box-shadow: inset 0 1px 0 #fff; margin-bottom: 8px; }
        .content-box { border: 1px solid #aaa; background-color: #fff; padding: 5px; margin-bottom: 15px; border-radius: 2px; }

        /* == SELECT PERFILES == */
        #selectPerfiles { width: 100%; border: none; overflow-y: auto; font-family: Arial, sans-serif; font-size: 13px; text-align-last: center; outline: none; color: #333; }
        #selectPerfiles option { padding: 4px 0; }
        #selectPerfiles option:checked { background-color: #888; color: white; background: linear-gradient(to bottom, #999, #777); }

        /* == ACORDEÓN Y ARBOL == */
        .tree-container { height: 250px; overflow-y: auto; text-align: left; padding: 10px; }
        ul.tree-list { list-style: none; padding-left: 10px; margin: 0; }

        /* Fila del Sistema (Padre) */
        .sistema-row { cursor: pointer; padding: 2px 0; user-select: none; }
        .sistema-row:hover .badge-sistema { filter: brightness(0.9); }
        .badge-sistema { background: linear-gradient(to bottom, #999 0%, #777 100%); color: white; padding: 2px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; display: inline-block; box-shadow: 1px 1px 2px rgba(0,0,0,0.3); text-shadow: 0 1px 1px #333; }

        /* Lista de Hijos */
        .nested-options { display: none; list-style: none; padding-left: 0; margin-top: 2px; transition: all 0.3s ease; }
        .nested-options.active { display: block; }
        .item-opcion { padding-left: 25px; margin: 2px 0; font-size: 12px; color: #333; }

        /* Botón Guardar */
        .btn-guardar { background: linear-gradient(to bottom, #eeeeee 0%, #cccccc 100%); border: 1px solid #888; border-radius: 3px; padding: 5px 15px; font-weight: bold; color: #333; cursor: pointer; box-shadow: 1px 1px 2px rgba(0,0,0,0.1); }
        .btn-guardar:hover { background: #e0e0e0; }

        .mensaje-exito { color: #008000; font-weight: bold; font-size: 14px; margin-bottom: 10px; }
        .footer-action { width: 800px; margin: 10px auto; text-align: left; }
    </style>

    <script>
        // 1. Redirección al cambiar perfil
        function cambiarPerfil(selectObject) {
            var perfil = selectObject.value;
            if(perfil) {
                var ruta = "${pageContext.request.contextPath}/srvSeguridad?accion=verOpciones&perfilSeleccionado=" + encodeURIComponent(perfil);
                window.location.href = ruta;
            }
        }

        // 2. Función Acordeón
        function toggleAccordion(idSistema, iconId) {
            var listaHijos = document.getElementById(idSistema);
            var icono = document.getElementById(iconId);

            if (listaHijos.style.display === "block") {
                listaHijos.style.display = "none";
                icono.className = "fas fa-caret-right"; 
            } else {
                listaHijos.style.display = "block";
                icono.className = "fas fa-caret-down"; 
            }
        }

        // 3. Verificar padres automáticamente
        function actualizarPadres() {
            var listas = document.querySelectorAll('.nested-options');
            listas.forEach(function(ul) {
                var codigoSistema = ul.id.replace("sys_", "");
                var checkboxPadre = document.getElementById("chk_padre_" + codigoSistema);
                var hijosMarcados = ul.querySelectorAll('input[type="checkbox"]:checked').length;
                
                if(checkboxPadre) {
                    checkboxPadre.checked = (hijosMarcados > 0);
                }
            });
        }

        window.onload = function() {
            actualizarPadres();
            // Abrir automáticamente las carpetas con items seleccionados
            var listas = document.querySelectorAll('.nested-options');
            listas.forEach(function(ul) {
                if(ul.querySelectorAll('input[type="checkbox"]:checked').length > 0) {
                    ul.style.display = "block";
                    var codigo = ul.id.replace("sys_", "");
                    var icon = document.getElementById("icon_" + codigo);
                    if(icon) icon.className = "fas fa-caret-down";
                }
            });
        };
    </script>
</head>
<body>

    <h2>MONSTER UNIVERSITY</h2>

    <c:if test="${not empty mensaje}">
        <div class="mensaje-exito">${mensaje}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/srvSeguridad" method="POST" target="_self">
        <input type="hidden" name="accion" value="guardarPermisos">

        <div class="main-container">
            <div class="metal-bar">OPCIONES POR PERFIL</div>
            
            <div style="border: 1px solid #bbb; padding: 4px; border-radius: 3px; margin-bottom: 10px;">
                <div class="metal-bar" style="margin: 0; border-radius: 3px 3px 0 0; border-bottom: 1px solid #999;">PERFILES</div>
                <div class="content-box" style="margin: 0; border-top: none; border-radius: 0 0 3px 3px; height: 120px; overflow: hidden;">
                    
                    <select name="perfilSeleccionado" id="selectPerfiles" size="6" onchange="cambiarPerfil(this)">
                        <c:forEach var="p" items="${sessionScope.listaPerfiles}">
                            <option value="${p.cod_Perfil}" ${p.cod_Perfil == sessionScope.perfilActual ? 'selected' : ''}>
                                ${p.desc_Perfil}
                            </option>
                        </c:forEach>
                    </select>

                </div>
            </div>

            <div class="metal-bar">Actualizar Opciones</div>
            <div style="border: 1px solid #bbb; padding: 4px; border-radius: 3px;">
                <div class="metal-bar" style="margin: 0; border-radius: 3px 3px 0 0; border-bottom: 1px solid #999;">OPCIONES</div>
                
                <div class="content-box" style="margin: 0; border-top: none; border-radius: 0 0 3px 3px;">
                    <div class="tree-container">
                        <ul class="tree-list">
                            
                            <c:forEach var="sis" items="${sessionScope.arbolMenu}">
                                <li>
                                    <div class="sistema-row" onclick="toggleAccordion('sys_${sis.codigo}', 'icon_${sis.codigo}')">
                                        <i id="icon_${sis.codigo}" class="fas fa-caret-right" style="color:#666; font-size:12px; margin-right:5px; width:10px;"></i>
                                        <input type="checkbox" id="chk_padre_${sis.codigo}" disabled style="vertical-align: middle;">
                                        <span class="badge-sistema">${sis.nombre}</span>
                                    </div>

                                    <ul id="sys_${sis.codigo}" class="nested-options">
                                        <c:forEach var="opc" items="${sis.opciones}">
                                            <li class="item-opcion">
                                                <i class="fas fa-caret-right" style="color:#999; font-size:10px; margin-right: 5px; margin-left: 20px;"></i>
                                                <input type="checkbox" name="opcionesCheck" value="${opc.codigo}" 
                                                       ${opc.asignado ? 'checked' : ''} 
                                                       style="vertical-align: middle;"
                                                       onchange="actualizarPadres()">
                                                <span>${opc.nombre}</span>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </li>
                            </c:forEach>

                            <c:if test="${empty sessionScope.arbolMenu}">
                                <p style="text-align: center; color: #777; margin-top:20px;">
                                    <i class="fas fa-arrow-up"></i> Seleccione un perfil para cargar sus opciones.
                                </p>
                            </c:if>

                        </ul>
                    </div>
                </div>
            </div>
        </div>

        <div class="footer-action">
            <button type="submit" class="btn-guardar">
                <i class="fas fa-chevron-up" style="font-size:10px;"></i> Guardar
            </button>
        </div>
    </form>
</body>
</html>