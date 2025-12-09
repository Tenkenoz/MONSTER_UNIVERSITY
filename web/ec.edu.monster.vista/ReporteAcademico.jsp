<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Reporte Académico</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <style>
        :root { --mu-blue: #003366; --mu-gold: #FFD700; --bg-gray: #f4f6f9; --danger: #dc3545; }
        body { font-family: 'Segoe UI', Tahoma, sans-serif; background: var(--bg-gray); padding: 20px; margin: 0; }
        .card-header { background: var(--mu-blue); color: white; padding: 15px; border-left: 5px solid var(--mu-gold); border-radius: 5px 5px 0 0; display: flex; justify-content: space-between; align-items: center; }
        .header-title { font-size: 1.2rem; font-weight: bold; gap: 10px; display: flex; align-items: center; }
        .table-wrap { background: white; border-radius: 0 0 5px 5px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); overflow: hidden; }
        table { width: 100%; border-collapse: collapse; }
        th { background: #f8f9fa; padding: 15px; text-align: left; font-size: 0.85rem; color: #555; text-transform: uppercase; border-bottom: 2px solid #ddd; }
        td { padding: 12px 15px; border-bottom: 1px solid #eee; font-size: 0.95rem; color: #333; vertical-align: middle; }
        tbody tr:hover { background-color: #f1f3f5; }
        .btn-pdf { background: var(--danger); color: white; border: none; padding: 8px 15px; border-radius: 4px; text-decoration: none; font-size: 0.9rem; display: inline-flex; align-items: center; gap: 6px; transition: 0.3s; }
        .btn-pdf:hover { background: #b02a37; }
    </style>
</head>
<body>

    <div class="card-header">
        <div class="header-title"><i class="fas fa-graduation-cap"></i> Reporte Académico General</div>
    </div>

    <div class="table-wrap">
        <table>
            <thead>
                <tr>
                    <th>Código</th>
                    <th>Estudiante</th>
                    <th>Carrera</th>
                    <th style="text-align: center;">Acción</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty listaEstudiantes}">
                        <c:forEach var="e" items="${listaEstudiantes}">
                            <tr>
                                <td style="font-weight: bold; color: var(--mu-blue);">${e.codigoEstu}</td>
                                <td>${e.apellido} ${e.nombre}<br><small style="color:#777;">${e.email}</small></td>
                                <td>${e.codigoCarr.nombreCarr}</td>
                                <td style="text-align: center;">
                                    <a href="${pageContext.request.contextPath}/ServletEstudiante?accion=reportePDF&codEstu=${e.codigoEstu}" 
                                       class="btn-pdf" target="_blank" title="Descargar Notas">
                                        <i class="fas fa-file-pdf"></i> PDF
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr><td colspan="4" style="text-align:center; padding:30px; color:#999;">No hay estudiantes.</td></tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>
</body>
</html>