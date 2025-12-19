package ec.edu.monster.controlador;

import ec.edu.monster.modelo.DAOEstudiante;
import ec.edu.monster.modelo.Estudiante;
import ec.edu.monster.modelo.Nota;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "ServletEstudiante", urlPatterns = {"/ServletEstudiante"})
public class ServletEstudiante extends HttpServlet {

    private static final String VISTA_REPORTE = "/ec.edu.monster.vista/Académico.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        DAOEstudiante dao = new DAOEstudiante();

        try {
            // 1. LISTAR
            if (accion == null || accion.equals("listar")) {
                List<Estudiante> lista = dao.listarEstudiantes();
                request.setAttribute("listaEstudiantes", lista);
                request.getRequestDispatcher(VISTA_REPORTE).forward(request, response);
                return;
            } 
            
            // 2. REPORTE PDF (Nativo)
            else if (accion.equals("reportePDF")) {
                generarPDFNativo(request, response, dao);
                return; 
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========================================================================
    // GENERADOR PDF NATIVO (SIN COLORES CONDICIONALES PARA EVITAR ERRORES)
    // ========================================================================
    private void generarPDFNativo(HttpServletRequest request, HttpServletResponse response, DAOEstudiante dao) {
        try {
            // 1. Obtener Datos
            String codEstu = request.getParameter("codEstu");
            Estudiante e = dao.obtenerEstudiante(codEstu);
            List<Nota> notas = dao.listarNotasEstudiante(codEstu);

            if (e == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Estudiante no encontrado");
                return;
            }

            // 2. Configurar Respuesta
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=Reporte_" + codEstu + ".pdf");

            // 3. Construir PDF
            StringBuilder pdf = new StringBuilder();
            List<Integer> objOffsets = new ArrayList<>();

            // --- CABECERA ---
            pdf.append("%PDF-1.4\n");

            // Objetos Base (Catálogo, Páginas, Fuentes)
            addObj(pdf, objOffsets); pdf.append("<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");
            addObj(pdf, objOffsets); pdf.append("<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");
            addObj(pdf, objOffsets); pdf.append("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Resources << /Font << /F1 4 0 R /F2 5 0 R >> >> /Contents 6 0 R >>\nendobj\n");
            addObj(pdf, objOffsets); pdf.append("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n");
            addObj(pdf, objOffsets); pdf.append("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold >>\nendobj\n");

            // --- OBJ 6: CONTENIDO GRÁFICO ---
            StringBuilder stream = new StringBuilder();
            
            // A. ENCABEZADO AZUL
            stream.append("0 0.2 0.4 rg\n"); // Azul
            stream.append("0 750 595 92 re f\n"); 

            // LOGO (El Ojo) - Vectorial
            stream.append("0.6 0.8 0.2 rg\n"); // Verde
            stream.append("50 780 m 50 791 59 800 70 800 c 81 800 90 791 90 780 c 90 769 81 760 70 760 c 59 760 50 769 50 780 c f\n");
            stream.append("1 1 1 rg\n"); // Blanco
            stream.append("60 780 m 60 785 65 790 70 790 c 75 790 80 785 80 780 c 80 775 75 770 70 770 c 65 770 60 775 60 780 c f\n");
            stream.append("0 0 0 rg\n"); // Negro
            stream.append("67 780 m 67 782 68 783 70 783 c 72 783 73 782 73 780 c 73 778 72 777 70 777 c 68 777 67 778 67 780 c f\n");

            // TÍTULOS
            stream.append("BT\n/F2 20 Tf\n1 1 1 rg\n110 785 Td\n(MONSTER UNIVERSITY) Tj\nET\n");
            stream.append("BT\n/F1 10 Tf\n1 1 1 rg\n110 770 Td\n(REPORTE ACADEMICO OFICIAL) Tj\nET\n");

            // B. DATOS ESTUDIANTE
            float y = 700;
            stream.append("0.95 0.95 0.95 rg\n"); // Gris fondo
            stream.append("40 620 515 100 re f\n");
            stream.append("0.7 0.7 0.7 RG\n1 w\n"); // Borde
            stream.append("40 620 515 100 re S\n");

            drawLabelValue(stream, "ESTUDIANTE:", e.getApellido() + " " + e.getNombre(), 60, y); y -= 20;
            drawLabelValue(stream, "CODIGO:", e.getCodigoEstu(), 60, y); y -= 20;
            String carrera = (e.getCodigoCarr() != null) ? e.getCodigoCarr().getNombreCarr() : "-";
            drawLabelValue(stream, "CARRERA:", carrera, 60, y); y -= 20;
            drawLabelValue(stream, "EMAIL:", e.getEmail(), 60, y); y -= 40;

            // C. TABLA DE NOTAS
            y = 580;
            
            // Encabezado Tabla
            stream.append("0 0.2 0.4 rg\n"); // Azul
            stream.append("40 ").append(y).append(" 515 20 re f\n");
            stream.append("BT\n/F2 10 Tf\n1 1 1 rg\n50 ").append(y + 6).append(" Td (ASIGNATURA) Tj\nET\n");
            stream.append("BT\n/F2 10 Tf\n1 1 1 rg\n450 ").append(y + 6).append(" Td (NOTA FINAL) Tj\nET\n");
            
            y -= 25;

            // Filas
            if (notas == null || notas.isEmpty()) {
                stream.append("BT\n/F1 10 Tf\n0 0 0 rg\n50 ").append(y).append(" Td (Sin notas) Tj\nET\n");
            } else {
                boolean gris = true;
                for (Nota n : notas) {
                    if (gris) { // Zebra
                        stream.append("0.95 0.95 0.95 rg\n");
                        stream.append("40 ").append(y - 5).append(" 515 18 re f\n");
                    }
                    gris = !gris;

                    String materia = (n.getMateria() != null) ? n.getMateria().getNombreMateria() : "---";
                    String notaStr = String.valueOf(n.getNotaFinal());

                    // --- AQUÍ ESTABA EL ERROR: HEMOS QUITADO LA LÓGICA DE COLOR ---
                    // Ahora imprimimos todo en NEGRO (0 0 0 rg) y listo.
                    stream.append("0 0 0 rg\n"); 

                    // Imprimir Nombre Materia
                    stream.append("BT\n/F1 10 Tf\n"); 
                    stream.append("50 ").append(y).append(" Td (").append(materia).append(") Tj\nET\n");
                    
                    // Imprimir Nota
                    stream.append("BT\n/F2 10 Tf\n"); // Negrita
                    stream.append("460 ").append(y).append(" Td (").append(notaStr).append(") Tj\nET\n");

                    y -= 20;
                }
            }
            
            stream.append("0 0.2 0.4 RG\n1 w\n40 ").append(y + 5).append(" 515 0 re S\n"); // Línea final

            // PIE DE PÁGINA
            String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
            stream.append("BT\n/F1 8 Tf\n0.5 0.5 0.5 rg\n40 30 Td (Generado el: ").append(fecha).append(") Tj\nET\n");

            // --- FIN STREAM ---
            addObj(pdf, objOffsets);
            pdf.append("<< /Length ").append(stream.length()).append(" >>\nstream\n");
            pdf.append(stream);
            pdf.append("\nendstream\nendobj\n");

            // --- XREF Y TRAILER ---
            long xrefPos = pdf.length();
            pdf.append("xref\n0 7\n0000000000 65535 f \n");
            for (int offset : objOffsets) pdf.append(String.format("%010d 00000 n \n", offset));
            pdf.append("trailer\n<< /Size 7 /Root 1 0 R >>\n");
            pdf.append("startxref\n").append(xrefPos).append("\n%%EOF");

            OutputStream out = response.getOutputStream();
            out.write(pdf.toString().getBytes("ISO-8859-1"));
            out.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void drawLabelValue(StringBuilder sb, String label, String val, float x, float y) {
        sb.append("BT\n/F2 10 Tf\n0 0.2 0.4 rg\n").append(x).append(" ").append(y).append(" Td (").append(label).append(") Tj\nET\n");
        sb.append("BT\n/F1 10 Tf\n0 0 0 rg\n").append(x + 100).append(" ").append(y).append(" Td (").append(val != null ? val : "-").append(") Tj\nET\n");
    }

    private void addObj(StringBuilder pdf, List<Integer> offsets) {
        offsets.add(pdf.length());
        pdf.append(offsets.size()).append(" 0 obj\n");
    }

    @Override protected void doPost(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException { doGet(r, s); }
}