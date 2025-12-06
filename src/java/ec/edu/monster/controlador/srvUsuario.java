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
    
                sesion = request.getSession();
                sesion.setAttribute("usuario", usuarioLogueado);

                Persona persona = usuarioLogueado.getPersona();

                if (persona instanceof Empleado) {
                    Empleado emp = (Empleado) persona;
                    request.setAttribute("empleado",emp);
                    String codigoRol = "";
                    
                    if(emp.getCodigoRol() != null) {
                        codigoRol = emp.getCodigoRol().getCodigoRol(); 
                    }
                            
                    // REDIRECCIONES SEGÚN TU ESTRUCTURA DE ARCHIVOS REAL
                    switch (codigoRol) {
                        case "ADM": 
                             request.setAttribute("msj", "Bienvenid@ Administrador: " + emp.getNombre());
                             
                             this.getServletConfig().getServletContext().getRequestDispatcher(CARPETA_VISTAS+ "Administradores.jsp").forward(request, response);
                             break;
                        case "DOC": 
                             request.setAttribute("msj", "Bienvenid@ Docente: " + emp.getNombre());
                             this.getServletConfig().getServletContext().getRequestDispatcher(CARPETA_VISTAS+ "Docentes.jsp").forward(request, response);
                             break;
                        case "SEC": 
                             request.setAttribute("msj", "Bienvenid@ Secretaria: " + emp.getNombre());
                             this.getServletConfig().getServletContext().getRequestDispatcher(CARPETA_VISTAS+ "Secretaria.jsp").forward(request, response);
                             break;
                        case "INV":
                             request.setAttribute("msj", "Bienvenid@ invitado: " + emp.getNombre());
                             this.getServletConfig().getServletContext().getRequestDispatcher(CARPETA_VISTAS+ "Invitados.jsp").forward(request, response);
                             break;
                        default: 
                             request.setAttribute("msj", "Bienvenid@ invitado: " + emp.getNombre());
                             this.getServletConfig().getServletContext().getRequestDispatcher(CARPETA_VISTAS+ "Invitados.jsp").forward(request, response);
                             break;
                    }

                } else if (persona instanceof Estudiante) {
                    Estudiante est = (Estudiante) persona;
                    request.setAttribute("estudiante",est);
                    request.setAttribute("msj", "Bienvenido Estudiante: " + persona.getNombre());
                    this.getServletConfig().getServletContext().getRequestDispatcher(CARPETA_VISTAS+ "Estudiantes.jsp").forward(request, response);
                             

                } else {
                    this.getServletConfig().getServletContext().getRequestDispatcher(CARPETA_VISTAS+ "login.jsp").forward(request, response);
                    request.setAttribute("msjerror", "Usuario no encontrado");
                    
                }

            } else {

                request.setAttribute("msjerror", "Credenciales Incorrectas");
                request.getRequestDispatcher(CARPETA_VISTAS + "login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msjerror", "Error de Base de Datos: " + e.getMessage());
            request.getRequestDispatcher(CARPETA_VISTAS + "login.jsp").forward(request, response);
        }
    }

    private void cerrarsession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession sesion = request.getSession();
        sesion.removeAttribute("usuario");
        sesion.invalidate();
        response.sendRedirect(request.getContextPath() + CARPETA_VISTAS + "login.jsp");
    }

    private Usuario obtenerDatos(HttpServletRequest request) {
        Usuario u = new Usuario();
        Persona p = new Persona();
        p.setEmail(request.getParameter("txtEmail")); 
        u.setPersona(p); 
        u.setPassword(request.getParameter("txtPassword")); 
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