package ec.edu.monster.controlador;

import ec.edu.monster.modelo.DAOROLES;
import ec.edu.monster.modelo.Empleado;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "srvSeguridad", urlPatterns = {"/srvSeguridad"})
public class srvSeguridad extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String accion = request.getParameter("accion");
        DAOROLES dao = new DAOROLES();

        try {
            if ("listarInvitados".equals(accion)) { 
                // 1. CARGA INICIAL DE LA PÁGINA
                request.setAttribute("roles", dao.listarRoles());
                request.setAttribute("disponibles", dao.listarDisponibles());
                request.getRequestDispatcher("/ec.edu.monster.vista/seguridadAdmin.jsp").forward(request, response);
            
            } else if ("obtenerListas".equals(accion)) {
                // 2. AJAX: CUANDO CAMBIAS EL COMBO (Trae JSON)
                String rol = request.getParameter("rol");
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                
                List<Empleado> listaDisp = dao.listarDisponibles();
                List<Empleado> listaAsig = dao.listarAsignados(rol);
                
                // Construcción de JSON manual (Sin librería GSON)
                String json = "{\"disponibles\":" + listToJson(listaDisp) + ", \"asignados\":" + listToJson(listaAsig) + "}";
                out.print(json);
                
            } else if ("guardarCambios".equals(accion)) {
                // 3. AJAX: GUARDAR TODO (Recibe IDs separados por coma)
                String rol = request.getParameter("rol");
                String asignadosStr = request.getParameter("asignados");
                String removidosStr = request.getParameter("removidos");
                
                String[] idsAsignados = (asignadosStr != null && !asignadosStr.isEmpty()) ? asignadosStr.split(",") : null;
                String[] idsRemovidos = (removidosStr != null && !removidosStr.isEmpty()) ? removidosStr.split(",") : null;
                
                boolean ok = dao.guardarCambiosMasivos(rol, idsAsignados, idsRemovidos);
                response.getWriter().print(ok); // Retorna "true" o "false"
            }
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error fatal, responder false para que el JS avise
            if(!"listarInvitados".equals(accion)) response.getWriter().print("false");
        }
    }

    // Helper para convertir Lista Java -> JSON String
    private String listToJson(List<Empleado> lista) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < lista.size(); i++) {
            Empleado e = lista.get(i);
            sb.append("{")
              .append("\"codigoPersona\":\"").append(e.getCodigoPersona()).append("\",")
              .append("\"nombre\":\"").append(e.getNombre()).append("\",")
              .append("\"apellido\":\"").append(e.getApellido()).append("\"")
              .append("}");
            if (i < lista.size() - 1) sb.append(",");
        }
        sb.append("]");
        return sb.toString();
    }

    @Override protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException { processRequest(r, s); }
    @Override protected void doPost(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException { processRequest(r, s); }
}