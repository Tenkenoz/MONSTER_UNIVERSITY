package ec.edu.monster.controlador;

import ec.edu.monster.modelo.DAOEstudiante;
import ec.edu.monster.modelo.Estudiante;
import ec.edu.monster.modelo.Nota;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.awt.Color;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// Librería BFO
import org.faceless.pdf2.*; 

@WebServlet(name = "ServletEstudiante", urlPatterns = {"/ServletEstudiante"})
public class ServletEstudiante extends HttpServlet {

    private static final String VISTA_REPORTE = "/ec.edu.monster.vista/ReporteAcademico.jsp";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        DAOEstudiante dao = new DAOEstudiante();

        try {
            // 1. LISTAR (Carga el JSP con la tabla)
            if (accion == null || accion.equals("listar")) {
                List<Estudiante> lista = dao.listarEstudiantes();
                request.setAttribute("listaEstudiantes", lista);
                request.getRequestDispatcher(VISTA_REPORTE).forward(request, response);
                return; // Importante salir aquí
            } 
            
            // 2. REPORTE PDF (Descarga directa)
            else if (accion.equals("reportePDF")) {
                generarPDF(request, response, dao);
                return; // Importante salir aquí
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generarPDF(HttpServletRequest request, HttpServletResponse response, DAOEstudiante dao) {
        // Configuración básica que SÍ funciona
        response.setContentType("application/pdf");
        
        try {
            String codEstu = request.getParameter("codEstu");
            
            // Nombre del archivo para descarga
            response.setHeader("Content-Disposition", "attachment; filename=Reporte_" + codEstu + ".pdf");

            // Obtener Datos
            Estudiante e = dao.obtenerEstudiante(codEstu);
            List<Nota> notas = dao.listarNotasEstudiante(codEstu);

            if (e == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Estudiante no encontrado");
                return;
            }

            // --- INICIO PDF (Lógica Simple) ---
            PDF pdf = new PDF();
            PDFPage page = pdf.newPage("A4");

            // Estilos Básicos
            PDFStyle titulo = new PDFStyle();
            titulo.setFont(new StandardFont(StandardFont.HELVETICA), 18);
            titulo.setFillColor(Color.BLACK);

            PDFStyle texto = new PDFStyle();
            texto.setFont(new StandardFont(StandardFont.HELVETICA), 12);
            
            // Coordenada Y inicial
            float y = page.getHeight() - 50;

            // Encabezado
            page.setStyle(titulo);
            page.drawText("REPORTE ACADEMICO", 50, y);
            y -= 40;

            // Datos del Estudiante
            page.setStyle(texto);
            page.drawText("Estudiante: " + e.getApellido() + " " + e.getNombre(), 50, y); y -= 20;
            page.drawText("Codigo: " + e.getCodigoEstu(), 50, y); y -= 20;
            page.drawText("Carrera: " + e.getCodigoCarr().getNombreCarr(), 50, y); y -= 20;
            page.drawText("Email: " + e.getEmail(), 50, y); y -= 40;

            // Tabla de Notas
            page.setStyle(titulo); // Usamos estilo título para encabezado tabla
            page.drawText("MATERIA", 50, y);
            page.drawText("NOTA", 350, y); // Columna Nota a la derecha
            y -= 25;

            page.setStyle(texto);

            if (notas.isEmpty()) {
                page.drawText("Sin notas registradas.", 50, y);
            } else {
                for (Nota n : notas) {
                    // Control de salto de página simple
                    if (y < 50) { 
                        page = pdf.newPage("A4");
                        y = page.getHeight() - 50;
                        page.setStyle(texto);
                    }
                    
                    String materia = n.getMateria().getNombreMateria();
                    // Formateo simple de nota
                    String notaStr = String.valueOf(n.getNotaFinal());
                    
                    page.drawText(materia, 50, y);
                    page.drawText(notaStr, 350, y); // Misma X que el encabezado
                    y -= 20;
                }
            }

            // ESCRITURA DIRECTA AL OUTPUT (Tu método probado)
            OutputStream out = response.getOutputStream();
            pdf.render(out);
            out.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override protected void doPost(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException { doGet(r, s); }
}