package ec.edu.monster.controlador;

import ec.edu.monster.modelo.DAOUSUARIO;
import ec.edu.monster.modelo.Usuario;
import ec.edu.monster.modelo.Persona;
import ec.edu.monster.modelo.Empleado;
import ec.edu.monster.modelo.Estudiante;
import ec.edu.monster.modelo.Encriptador; 
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.UUID; 
// Imports de correo
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

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
                    case "recuperar": 
                        recuperar(request, response); 
                        break;
                    case "cambiarObligatorio": 
                        // ESTE ES EL CASO QUE RECIBE TU JSP
                        cambiarObligatorio(request, response); 
                        break; 
                    default: 
                        response.sendRedirect(request.getContextPath() + CARPETA_VISTAS + "login.jsp");
                }
            } else {
                response.sendRedirect(request.getContextPath() + CARPETA_VISTAS + "login.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + CARPETA_VISTAS + "login.jsp");
        }
    }

    private void verificar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Usuario usuarioInput = new Usuario();
        usuarioInput.setLogin(request.getParameter("txtEmail"));
        usuarioInput.setPassword(request.getParameter("txtPassword"));
        
        // Encriptar lo que ingresó el usuario para comparar
        String passHash = Encriptador.sha256(usuarioInput.getPassword());
        usuarioInput.setPassword(passHash);

        DAOUSUARIO dao = new DAOUSUARIO();
        Usuario usuarioLogueado = dao.identificar(usuarioInput);

        if (usuarioLogueado != null) {
            HttpSession sesion = request.getSession();
            sesion.setAttribute("usuario", usuarioLogueado);

            // --- AQUÍ CONECTAMOS CON TU JSP ---
            // Si el token es "1", lo mandamos a cambiar clave obligatoriamente
            // (Asumo que en tu Modelo Usuario mapeaste XEUSU_TOKEN_REC al atributo 'token')
            if ("1".equals(usuarioLogueado.getToken())) { 
                request.setAttribute("msjerror", "Por seguridad, cambie su contraseña temporal.");
                // Redirige a TU archivo
                request.getRequestDispatcher(CARPETA_VISTAS + "cambiarClaveObligatorio.jsp").forward(request, response);
                return; 
            }
            // ----------------------------------

            Persona persona = usuarioLogueado.getPersona();
            if (persona instanceof Empleado) {
                Empleado emp = (Empleado) persona;
                sesion.setAttribute("empleado", emp);
                String rol = (emp.getCodigoRol() != null) ? emp.getCodigoRol().getCodigoRol() : "INV";
                
                String destino = "Invitados.jsp";
                if("SUP".equals(rol)) destino = "panelAdmin.jsp";
                else if("ADM".equals(rol)) destino = "Administradores.jsp";
                else if("DOC".equals(rol)) destino = "Docentes.jsp";
                else if("SEC".equals(rol)) destino = "Secretaria.jsp";
                
                request.getRequestDispatcher(CARPETA_VISTAS + destino).forward(request, response);

            } else if (persona instanceof Estudiante) {
                sesion.setAttribute("estudiante", (Estudiante) persona);
                request.getRequestDispatcher(CARPETA_VISTAS + "Estudiantes.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher(CARPETA_VISTAS + "login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("msjerror", "Credenciales Incorrectas");
            request.getRequestDispatcher(CARPETA_VISTAS + "login.jsp").forward(request, response);
        }
    }

    // --- AQUÍ PROCESAMOS LOS DATOS DE TU JSP ---
    private void cambiarObligatorio(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession sesion = request.getSession();
        Usuario u = (Usuario) sesion.getAttribute("usuario");
        
        // Si por alguna razón no hay usuario en sesión, al login
        if(u == null) {
            response.sendRedirect(request.getContextPath() + CARPETA_VISTAS + "login.jsp");
            return;
        }

        // Recibimos los inputs de TU formulario
        String p1 = request.getParameter("txtPass1");
        String p2 = request.getParameter("txtPass2");

        if(!p1.equals(p2)) {
            request.setAttribute("msjerror", "Las contraseñas no coinciden.");
            // Volvemos a mostrar tu JSP con el error
            request.getRequestDispatcher(CARPETA_VISTAS + "cambiarClaveObligatorio.jsp").forward(request, response);
            return;
        }

        // Si coinciden: Encriptamos y guardamos
        String hash = Encriptador.sha256(p1);
        DAOUSUARIO dao = new DAOUSUARIO();
        
        // Actualizamos password y ponemos el token en "0" para liberar al usuario
        dao.actualizarPasswordYToken(u.getLogin(), hash, "0");
        
        // Cerramos sesión para obligarlo a entrar con la nueva clave limpia
        sesion.invalidate();
        request.setAttribute("msj", "Contraseña actualizada. Ingrese nuevamente.");
        request.getRequestDispatcher(CARPETA_VISTAS + "login.jsp").forward(request, response);
    }

    private void recuperar(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String email = request.getParameter("txtEmailRecuperar");
        DAOUSUARIO dao = new DAOUSUARIO();
        Usuario usuarioEncontrado = dao.buscarPorEmail(email);

        if (usuarioEncontrado != null) {
            String nuevaPass = UUID.randomUUID().toString().substring(0, 8); 
            String hashNueva = Encriptador.sha256(nuevaPass);
            
            // Si recupera contraseña, también le obligamos a cambiarla al entrar ("1")
            dao.actualizarPasswordYToken(usuarioEncontrado.getLogin(), hashNueva, "1");

            enviarCorreoSMTP(email, nuevaPass);
            request.setAttribute("msj", "Se envió una contraseña temporal a su correo.");
        } else {
            request.setAttribute("msjerror", "Correo no registrado.");
        }
        request.setAttribute("modalOpen", "true"); 
        request.getRequestDispatcher(CARPETA_VISTAS + "login.jsp").forward(request, response);
    }

    private void cerrarsession(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + CARPETA_VISTAS + "login.jsp");
    }

    private void enviarCorreoSMTP(String destino, String password) {
         try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

            final String correoEmisor = "obanderick@gmail.com";
            final String claveCorreo = "wkcbeiirqchurtft"; 

            Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
                @Override protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correoEmisor, claveCorreo);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoEmisor));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));
            message.setSubject("Recuperación de Contraseña");
            message.setText("Contraseña Temporal: " + password + "\n\nCámbiela al ingresar.");

            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException { processRequest(r, s); }
    @Override protected void doPost(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException { processRequest(r, s); }
}