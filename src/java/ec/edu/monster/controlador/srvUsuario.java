package ec.edu.monster.controlador; // Ajusta este paquete según tu estructura

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
                        response.sendRedirect("identificar.jsp");
                }
            } else {
                response.sendRedirect("identificar.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Importante para ver errores en consola del servidor
            try {
                 // Si falla, redirigimos a identificar con mensaje de error
                 request.setAttribute("msj", "Error en el servidor: " + e.getMessage());
                 request.getRequestDispatcher("/identificar.jsp").forward(request, response);
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
        
        // 1. Capturamos los datos del formulario (txtUsu, txtPass)
        usuarioInput = this.obtenerDatos(request);

        try {
            dao = new DAOUSUARIO();
            // 2. Consultamos a la base de datos
            usuarioLogueado = dao.identificar(usuarioInput);

            if (usuarioLogueado != null) {
                // --- LOGIN EXITOSO ---
                
                sesion = request.getSession();
                sesion.setAttribute("usuario", usuarioLogueado);

                // Obtenemos la persona que está dentro del usuario para verificar su tipo
                Persona persona = usuarioLogueado.getPersona();

                // 3. Verificamos el ROL usando Polimorfismo (instanceof)
                if (persona instanceof Empleado) {
                    
                    // Es un EMPLEADO -> Redirigir a menú de empleado
                    request.setAttribute("msj", "Bienvenido Empleado: " + persona.getNombre());
                    request.getRequestDispatcher("/principalEmpleado.jsp").forward(request, response);

                } else if (persona instanceof Estudiante) {
                    
                    // Es un ESTUDIANTE -> Redirigir a menú de estudiante
                    request.setAttribute("msj", "Bienvenido Estudiante: " + persona.getNombre());
                    request.getRequestDispatcher("/principalEstudiante.jsp").forward(request, response);

                } 

            } else {
                // --- LOGIN FALLIDO ---
                request.setAttribute("msj", "Credenciales Incorrectas");
                request.getRequestDispatcher("/identificar.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("msj", "Error de Base de Datos: " + e.getMessage());
            request.getRequestDispatcher("/identificar.jsp").forward(request, response);
        }
    }

    private void cerrarsession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession sesion = request.getSession();
        sesion.removeAttribute("usuario");
        sesion.invalidate();
        response.sendRedirect("identificar.jsp");
    }

    // Este método es CLAVE: Adapta los inputs del JSP a la estructura que pide tu DAO
    private Usuario obtenerDatos(HttpServletRequest request) {
        Usuario u = new Usuario();
        
        // Tu DAO busca: user.getPersona().getCodigoPersona()
        // Por eso creamos una Persona y le seteamos el usuario del form
        Persona p = new Persona();
        p.setCodigoPersona(request.getParameter("txtUsu")); 
        
        // Asignamos la persona al usuario
        u.setPersona(p); 
        
        // Tu DAO busca: user.getPassword()
        u.setPassword(request.getParameter("txtPass")); 
        
        return u;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods">
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
    // </editor-fold>
}