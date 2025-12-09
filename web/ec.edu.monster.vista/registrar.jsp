<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Monsters Inc. | Registro Completo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/registrar.css">
    <style>
        .error-msg { color: #721c24; background: #f8d7da; padding: 10px; border-radius: 5px; margin-bottom: 10px; text-align: center;}
        .success-msg { color: #155724; background: #d4edda; padding: 10px; border-radius: 5px; margin-bottom: 10px; text-align: center;}
        .register-card { width: 600px; max-width: 95%; margin: 20px auto; padding: 20px; border: 1px solid #ccc; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        .section-title { width: 100%; text-align: left; color: #0b3f91; font-size: 0.9rem; margin-top: 10px; border-bottom: 1px solid #ccc; padding-bottom: 5px; margin-bottom: 10px; font-weight: bold;}
        .row { display: flex; justify-content: space-between; margin-bottom: 10px; }
        .input-box, .select-box { width: 48%; padding: 8px; box-sizing: border-box; }
        .btn-register { width: 100%; padding: 10px; background: #0b3f91; color: white; border: none; cursor: pointer; }
        .links { text-align: center; margin-top: 10px; }
    </style>
</head>

<body>
    <div class="register-card">
        <h2>REGISTRO DE USUARIO</h2>

        <% if(request.getAttribute("msj") != null) { %>
            <div class="<%= request.getAttribute("tipo") != null && request.getAttribute("tipo").equals("OK") ? "success-msg" : "error-msg" %>">
                <%= request.getAttribute("msj") %>
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/srvPersona" method="POST">
            <input type="hidden" name="accion" value="registrar">

            <div class="section-title">1. Datos de Identificación</div>
            <div class="row">
                <input name="txtCedula" type="text" class="input-box" placeholder="Cédula (10 dígitos)" required pattern="[0-9]{10}" title="La cédula debe tener 10 números">
                <input name="txtEmail" type="email" class="input-box" placeholder="Correo (Usuario)" required>
            </div>

            <div class="row">
                <input name="txtNombre" type="text" class="input-box" placeholder="Nombres Completos" required>
                <input name="txtApellido" type="text" class="input-box" placeholder="Apellidos Completos" required>
            </div>

            <div class="section-title">2. Información Personal</div>
            <div class="row">
                <div style="width: 48%;">
                    <label style="font-size: 12px; color:#555; margin-left: 10px;">Fecha de Nacimiento:</label>
                    <input name="txtFechaNac" type="date" class="input-box" required>
                </div>
                <div style="width: 48%;">
                    <label style="font-size: 12px; color:#555; margin-left: 10px;">Cargas Familiares:</label>
                    <input name="txtCargas" type="number" class="input-box" placeholder="Ej: 0" min="0" required>
                </div>
            </div>

            <div class="row">
                <select name="cboSexo" class="select-box" required>
                    <option value="" disabled selected>Género</option>
                    <option value="M">Masculino</option>
                    <option value="F">Femenino</option>
                </select>

                <select name="cboEstadoCivil" class="select-box" required>
                    <option value="" disabled selected>Estado Civil</option>
                    <option value="SOL">Soltero(a)</option>
                    <option value="CAS">Casado(a)</option>
                    <option value="DIV">Divorciado(a)</option>
                    <option value="VIU">Viudo(a)</option>
                </select>
            </div>

            <div class="section-title">3. Datos de Contacto</div>
            <div class="row">
                <input name="txtDireccion" type="text" class="input-box" placeholder="Dirección Domiciliaria" required style="width: 100%;">
            </div>
            <div class="row">
                <input name="txtCelular" type="tel" class="input-box" placeholder="Celular (09...)" required pattern="[0-9]{10}">
                <input name="txtTelDom" type="tel" class="input-box" placeholder="Teléfono Fijo" required>
            </div>

            <div class="section-title">4. Seguridad de la Cuenta</div>
            <div class="row">
                <input name="txtPassword" type="password" class="input-box" placeholder="Contraseña" required>
                <input name="txtPassword2" type="password" class="input-box" placeholder="Confirmar Contraseña" required>
            </div>

            <button type="submit" class="btn-register">REGISTRARSE</button>

            <div class="links">
                <a href="${pageContext.request.contextPath}/ec.edu.monster.vista/login.jsp">← Volver al Inicio de Sesión</a>
            </div>
        </form>
    </div>
</body>
</html>