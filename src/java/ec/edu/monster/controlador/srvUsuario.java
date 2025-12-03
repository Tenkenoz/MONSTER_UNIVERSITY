package ec.edu.monster.controlador;

import ec.edu.monster.modelo.DAOUSUARIO;
import ec.edu.monster.modelo.Usuario;
import ec.edu.monster.modelo.Persona;
import ec.edu.monster.modelo.Empleado;
import ec.edu.monster.modelo.Estudiante;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(name = "srvUsuario", urlPatterns = {"/srvUsuario"})
public class srvUsuario extends HttpServlet {

    // DEFINIMOS LA RUTA BASE SEGÚN TU IMAGEN
    // Esto apunta a la carpeta "ec.edu.monster.vista" dentro de Web Pages
    private static final String CARPETA_VISTAS = "/ec.edu.monster.vista/";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String accion = request.getParameter("accion");
        
        try {
            if (accion != null) {
                switch (accion) {
                    case "verificar":
                        verificar(request, response);
                        break;
                    case "cerrar":
                        cerrarsession(request, response);
                        break;
                    default:
                        // CORRECCIÓN: Redirige a login.jsp en su carpeta correcta
                        response.sendRedirect(request.getContextPath() + CARPETA_VISTAS + "login.jsp");
                }
            } else {
                response.sendRedirect(request.getContextPath() + CARPETA_VISTAS + "login.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                 request.setAttribute("msj", "Error en el servidor: " + e.getMessage());
                 // CORRECCIÓN: Ruta exacta para el forward
                 request.getRequestDispatcher(CARPETA_VISTAS + "login.jsp").forward(request, response);
            } catch (Exception ex) {
                System.out.println("Error grave: " + ex.getMessage());
            }
        }
    }

    private void verificar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession sesion;
        DAOUSUARIO dao;
        Usuario usuarioInput;
        Usuario usuarioLogueado;
        
        usuarioInput = this.obtenerDatos(request);

        try {
            dao = new DAOUSUARIO();
            usuarioLogueado = dao.identificar(usuarioInput);

            if (usuarioLogueado != null) {
                // --- LOGIN EXITOSO ---
                sesion = request.getSession();
                sesion.setAttribute("usuario", usuarioLogueado);

                Persona persona = usuarioLogueado.getPersona();

                if (persona instanceof Empleado) {
                    Empleado emp = (Empleado) persona;
                    String codigoRol = "";
                    
                    if(emp.getCodigoRol() != null) {
                        codigoRol = emp.getCodigoRol().getCodigoRol(); 
                    }

                    // REDIRECCIONES SEGÚN TU ESTRUCTURA DE ARCHIVOS REAL
                    switch (codigoRol) {
                        case "ADM": // Administrador
                             request.setAttribute("msj", "Bienvenido Admin: " + emp.getNombre());
                             // OJO: No vi un "Admin.jsp" en tu foto, redirijo a Docentes por ahora o crea el archivo
                             request.getRequestDispatcher(CARPETA_VISTAS + "Docentes.jsp").forward(request, response);
                             break;
                        case "DOC": // Docente
                             request.setAttribute("msj", "Bienvenido Docente: " + emp.getNombre());
                             // CORRECCIÓN: Nombre exacto según tu foto
                             request.getRequestDispatcher(CARPETA_VISTAS + "Docentes.jsp").forward(request, response);
                             break;
                        case "SEC": // Secretaria
                             request.setAttribute("msj", "Bienvenida Secretaria: " + emp.getNombre());
                             // CORRECCIÓN: Nombre exacto según tu foto
                             request.getRequestDispatcher(CARPETA_VISTAS + "Secretaria.jsp").forward(request, response);
                             break;
                        default: 
                             request.setAttribute("msj", "Bienvenido Empleado: " + emp.getNombre());
                             // Redirige a una página por defecto si no coincide el rol
                             request.getRequestDispatcher(CARPETA_VISTAS + "login.jsp").forward(request, response);
                             break;
                    }

                } else if (persona instanceof Estudiante) {
                    request.setAttribute("msj", "Bienvenido Estudiante: " + persona.getNombre());
                    // CORRECCIÓN: Nombre exacto según tu foto
                    request.getRequestDispatcher(CARPETA_VISTAS + "Estudiantes.jsp").forward(request, response);

                } else {
                    request.getRequestDispatcher(CARPETA_VISTAS + "login.jsp").forward(request, response);
                }

            } else {
                // --- LOGIN FALLIDO ---
                request.setAttribute("msj", "Credenciales Incorrectas");
                request.getRequestDispatcher(CARPETA_VISTAS + "login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msj", "Error de Base de Datos: " + e.getMessage());
            request.getRequestDispatcher(CARPETA_VISTAS + "login.jsp").forward(request, response);
        }
    }

    private void cerrarsession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession sesion = request.getSession();
        sesion.removeAttribute("usuario");
        sesion.invalidate();
        // Redirigir al login correcto
        response.sendRedirect(request.getContextPath() + CARPETA_VISTAS + "login.jsp");
    }

    private Usuario obtenerDatos(HttpServletRequest request) {
        Usuario u = new Usuario();
        Persona p = new Persona();
        p.setCodigoPersona(request.getParameter("usuario")); 
        u.setPersona(p); 
        u.setPassword(request.getParameter("password")); 
        return u;
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