package ec.edu.monster.controlador;

import ec.edu.monster.modelo.DAOPERSONA;
import ec.edu.monster.modelo.Persona;
import ec.edu.monster.modelo.Sexo;
import ec.edu.monster.modelo.EstadoCivil;

import java.io.IOException;
import java.text.SimpleDateFormat; // Necesario para convertir el String de fecha del HTML
import java.util.Date;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "srvPersona", urlPatterns = {"/srvPersona"})
public class srvPersona extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        String accion = request.getParameter("accion");
        
        try {
            if (accion != null) {
                switch (accion) {
                    case "registrar":
                        registrar(request, response);
                        break;
                    // Aquí podrías agregar "listar", "eliminar", etc.
                    default:
                        response.sendRedirect("registroPersona.jsp"); // Tu página de registro
                }
            } else {
                response.sendRedirect("registroPersona.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msj", "Error grave: " + e.getMessage());
            request.getRequestDispatcher("/registroPersona.jsp").forward(request, response);
        }
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Persona p = new Persona();
            
            // 1. Capturar datos simples
            // Asumimos que el código es la misma cédula
            p.setCodigoPersona(request.getParameter("txtCedula")); 
            p.setCedula(request.getParameter("txtCedula"));
            p.setNombre(request.getParameter("txtNombre"));
            p.setApellido(request.getParameter("txtApellido"));
            p.setDireccion(request.getParameter("txtDireccion"));
            p.setCelular(request.getParameter("txtCelular"));
            p.setTelDom(request.getParameter("txtTelDom"));
            p.setEmail(request.getParameter("txtEmail"));
            
            // Convertir cargas a entero
            int cargas = 0;
            try {
                cargas = Integer.parseInt(request.getParameter("txtCargas"));
            } catch (NumberFormatException e) {
                cargas = 0; // Valor por defecto si falla
            }
            p.setCargas(cargas);

            // 2. Manejo de FECHA (HTML envía String 'yyyy-MM-dd')
            String fechaStr = request.getParameter("txtFechaNaci");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = sdf.parse(fechaStr);
            p.setFechaNaci(fecha);

            // 3. Manejo de OBJETOS RELACIONADOS (Sexo y Estado Civil)
            // SEXO

            Sexo sexo = new Sexo();
            sexo.setCodigoSexo(request.getParameter("cboSexo")); // El value del select (M o F)
            p.setCodigoSexo(sexo); // Composición
            
            // ESTADO CIVIL
            EstadoCivil ec = new EstadoCivil();
            ec.setCodigoEstciv(request.getParameter("cboEstadoCivil")); // El value del select (SOL, CAS, etc.)
            p.setCodigoEstciv(ec); // Composición

            // 4. Llamar al DAO
            DAOPERSONA dao = new DAOPERSONA();
            if (dao.registrar(p)) {
                request.setAttribute("msj", "Persona registrada correctamente");
            } else {
                request.setAttribute("msj", "No se pudo registrar la persona");
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msj", "Error al guardar: " + e.getMessage());
        }
        
        // Retornar a la página de registro con el mensaje
        request.getRequestDispatcher("/registroPersona.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}