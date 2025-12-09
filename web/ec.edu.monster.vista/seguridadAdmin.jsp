<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Asignación de Roles | Monster Univ</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        /* ESTILOS MONSTER UNIVERSITY (Clean UI) */
        :root { --mu-blue: #003366; --mu-gold: #FFD700; --bg-gray: #f4f6f9; --success: #28a745; }
        body { font-family: 'Segoe UI', Tahoma, sans-serif; background: var(--bg-gray); padding: 20px; }
        
        .panel-container { 
            background: white; padding: 30px; border-radius: 8px; 
            box-shadow: 0 4px 15px rgba(0,0,0,0.1); max-width: 950px; margin: 0 auto; 
            border-top: 5px solid var(--mu-gold);
        }
        .header { 
            display: flex; justify-content: space-between; align-items: center; 
            border-bottom: 2px solid #eee; padding-bottom: 15px; margin-bottom: 25px; 
        }
        .header h2 { color: var(--mu-blue); margin: 0; display: flex; align-items: center; gap: 10px; }
        
        /* SELECTOR */
        .role-selector { 
            background: #eef2f7; padding: 20px; border-radius: 8px; text-align: center; margin-bottom: 30px; 
            border: 1px solid #dbeafe; display: flex; justify-content: center; align-items: center; gap: 15px;
        }
        .role-selector label { font-weight: bold; color: var(--mu-blue); font-size: 1.1rem; }
        .role-selector select { 
            padding: 10px; border-radius: 5px; border: 1px solid #003366; width: 350px; font-size: 1rem; 
            cursor: pointer; background: white; font-weight: bold; color: #333;
        }

        /* AREA DE TRANSFERENCIA */
        .transfer-area { display: flex; justify-content: space-between; align-items: flex-start; gap: 15px; }
        
        .list-box { width: 45%; }
        .list-header { 
            background: var(--mu-blue); color: white; padding: 12px; text-align: center; 
            border-radius: 5px 5px 0 0; font-weight: bold; font-size: 0.95rem;
        }
        .list-header.right { background: var(--mu-gold); color: var(--mu-blue); }
        
        .select-list { 
            width: 100%; height: 400px; border: 1px solid #ccc; border-top: none;
            border-radius: 0 0 5px 5px; padding: 5px; outline: none; font-size: 0.9rem; background: #fff;
        }
        .select-list option { padding: 8px 10px; border-bottom: 1px solid #f1f1f1; cursor: pointer; transition: 0.2s; }
        .select-list option:hover { background-color: #f0f8ff; color: var(--mu-blue); }

        /* CONTROLES */
        .controls { 
            display: flex; flex-direction: column; gap: 20px; justify-content: center; height: 400px; padding-top: 40px; 
        }
        .btn-move {
            background: white; border: 2px solid var(--mu-blue); color: var(--mu-blue); 
            width: 50px; height: 50px; border-radius: 50%; cursor: pointer; 
            font-size: 1.2rem; transition: 0.3s; display: flex; align-items: center; justify-content: center;
            box-shadow: 0 2px 5px rgba(0,0,0,0.1);
        }
        .btn-move:hover { background: var(--mu-blue); color: white; transform: scale(1.1); }

        /* BOTÓN GUARDAR */
        .footer-actions { text-align: center; padding-top: 30px; border-top: 1px solid #eee; margin-top: 20px; }
        .btn-save {
            background-color: var(--success); color: white; border: none; padding: 15px 50px;
            font-size: 1.1rem; font-weight: bold; border-radius: 5px; cursor: pointer;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1); transition: 0.3s;
            display: inline-flex; align-items: center; gap: 10px;
        }
        .btn-save:hover { background-color: #218838; transform: translateY(-2px); }
        .btn-save:disabled { background-color: #ccc; cursor: not-allowed; transform: none; }
    </style>
</head>
<body>

    <div class="panel-container">
        <div class="header">
            <h2><i class="fas fa-users-cog"></i> Gestión de Roles y Estudiantes</h2>
        </div>

        <div class="role-selector">
            <label><i class="fas fa-filter"></i> Gestionar:</label>
            <select id="cboRol" onchange="cargarListas()">
                <option value="" disabled selected>-- Seleccione Rol o Estudiante --</option>
                <c:forEach var="r" items="${roles}">
                    <option value="${r.codigoRol}">${r.descriRol}</option>
                </c:forEach>
            </select>
        </div>

        <div class="transfer-area">
            
            <div class="list-box">
                <div class="list-header"><i class="fas fa-user-clock"></i> Disponibles (Invitados)</div>
                <select id="listaIzq" class="select-list" multiple>
                    </select>
            </div>

            <div class="controls">
                <button class="btn-move" onclick="moverDerecha()" title="Asignar"><i class="fas fa-chevron-right"></i></button>
                <button class="btn-move" onclick="moverIzquierda()" title="Quitar"><i class="fas fa-chevron-left"></i></button>
            </div>

            <div class="list-box">
                <div class="list-header right"><i class="fas fa-user-check"></i> Asignados / En Rol</div>
                <select id="listaDer" class="select-list" multiple>
                    <option disabled style="text-align:center; padding-top:20px; color:#999;">&larr; Seleccione una opción arriba</option>
                </select>
            </div>
        </div>

        <div class="footer-actions">
            <button id="btnGuardar" class="btn-save" onclick="guardarCambios()" disabled>
                <i class="fas fa-save"></i> GUARDAR CAMBIOS
            </button>
        </div>
    </div>

    <script>
        // Array para recordar quién estaba asignado al principio y calcular diferencias
        let asignadosIniciales = []; 
        
        // 1. CARGAR LISTAS (AJAX)
        function cargarListas() {
            let rol = document.getElementById("cboRol").value;
            if(!rol) return;
            
            // UI Feedback
            let btn = document.getElementById("btnGuardar");
            btn.disabled = true;
            btn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> CARGANDO...';

            fetch('${pageContext.request.contextPath}/srvSeguridad?accion=obtenerListas&rol=' + rol)
                .then(r => r.json())
                .then(data => {
                    llenar(document.getElementById("listaIzq"), data.disponibles);
                    llenar(document.getElementById("listaDer"), data.asignados);
                    
                    // Guardamos snapshot inicial
                    asignadosIniciales = data.asignados.map(u => u.codigoPersona);
                    
                    btn.disabled = false;
                    btn.innerHTML = '<i class="fas fa-save"></i> GUARDAR CAMBIOS';
                })
                .catch(e => {
                    console.error(e);
                    alert("Error al cargar datos. Revise la consola.");
                });
        }

        function llenar(select, datos) {
            select.innerHTML = "";
            datos.forEach(p => {
                let opt = document.createElement("option");
                opt.value = p.codigoPersona; 
                opt.text = p.apellido + " " + p.nombre;
                select.add(opt);
            });
        }

        // 2. MOVER ELEMENTOS VISUALMENTE
        function moverDerecha() {
            moverOpciones(document.getElementById("listaIzq"), document.getElementById("listaDer"));
        }

        function moverIzquierda() {
            moverOpciones(document.getElementById("listaDer"), document.getElementById("listaIzq"));
        }

        function moverOpciones(origen, destino) {
            let selected = Array.from(origen.selectedOptions);
            if(selected.length === 0) return;

            // Limpiar mensaje placeholder si existe
            if(destino.options.length > 0 && destino.options[0].disabled) destino.innerHTML = "";
            
            selected.forEach(opt => {
                // Quitamos de origen y ponemos en destino
                destino.add(opt); 
            });
        }

        // 3. GUARDAR CAMBIOS (Lógica de Deltas)
        function guardarCambios() {
            let rol = document.getElementById("cboRol").value;
            if(!rol) return;

            let listDer = document.getElementById("listaDer");
            // Obtener los IDs que terminaron en la derecha
            let idsFinales = Array.from(listDer.options).map(opt => opt.value);

            // A. NUEVOS: Están en Final pero NO estaban al Inicio
            let nuevos = idsFinales.filter(id => !asignadosIniciales.includes(id));
            
            // B. REMOVIDOS: Estaban al Inicio pero YA NO están en Final
            let removidos = asignadosIniciales.filter(id => !idsFinales.includes(id));

            if(nuevos.length === 0 && removidos.length === 0) {
                alert("No has realizado cambios.");
                return;
            }

            if(!confirm("CONFIRMAR CAMBIOS:\n\n- Se asignarán: " + nuevos.length + " usuarios.\n- Se removerán: " + removidos.length + " usuarios.\n\n¿Está seguro?")) return;

            // Enviar al Servlet
            let params = new URLSearchParams();
            params.append('accion', 'guardarCambios');
            params.append('rol', rol);
            params.append('asignados', nuevos.join(","));
            params.append('removidos', removidos.join(","));

            fetch('${pageContext.request.contextPath}/srvSeguridad', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: params
            })
            .then(res => res.text())
            .then(ok => {
                if(ok.trim() === 'true') {
                    alert("¡Cambios guardados exitosamente!");
                    cargarListas(); // Recargar para sincronizar
                } else {
                    alert("Error al guardar en la base de datos.");
                }
            })
            .catch(e => {
                console.error(e);
                alert("Error de conexión.");
            });
        }
    </script>
</body>
</html>