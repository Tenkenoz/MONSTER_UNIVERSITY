package ec.edu.monster.controlador;

import ec.edu.monster.modelo.DAOPERSONA;
import ec.edu.monster.modelo.Persona;
import ec.edu.monster.modelo.Usuario;
import ec.edu.monster.modelo.Sexo;
import ec.edu.monster.modelo.EstadoCivil;
import ec.edu.monster.modelo.Encriptador; 
import java.io.IOException;
import java.text.SimpleDateFormat;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Properties;
import java.util.UUID;
// Imports de Correo
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
// Imports PDF
import org.faceless.pdf2.*;
import java.awt.Color;
import java.io.OutputStream;

@WebServlet(name = "srvPersona", urlPatterns = {"/srvPersona"})
public class srvPersona extends HttpServlet {

    private static final String VISTA_REGISTRAR = "/ec.edu.monster.vista/registrar.jsp";
    private static final String VISTA_CRUD = "/ec.edu.monster.vista/UsuariosCrud.jsp";
    private static final String VISTA_REPORTE = "/ec.edu.monster.vista/ReporteUsuarios.jsp";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8"); 
        response.setContentType("text/html;charset=UTF-8");
        String accion = request.getParameter("accion");

        try {
            DAOPERSONA dao = new DAOPERSONA();

            if (accion != null) {
                switch (accion) {
                    case "registrarModal": // VIENE DE TU JSP (UsuariosCrud.jsp)
                        registrar(request, response, true); 
                        break;
                        
                    case "registrar": // Registro Público
                        registrar(request, response, false);
                        break;

                    case "listar":
                        request.setAttribute("listaUsuarios", dao.listarUsuariosCompletos());
                        request.getRequestDispatcher(VISTA_CRUD).forward(request, response);
                        break;
                    
                    case "listarReporte":
                        request.setAttribute("listaUsuarios", dao.listarUsuariosCompletos());
                        request.getRequestDispatcher(VISTA_REPORTE).forward(request, response);
                        break;

                    case "reporteUsuario":
                        reporteUsuario(request, response);
                        break;
                        
                    case "eliminar":
                        String idElim = request.getParameter("id");
                        if(dao.eliminarTodo(idElim)) {
                            request.setAttribute("msj", "Usuario eliminado correctamente.");
                            request.setAttribute("tipo", "OK");
                        } else {
                            request.setAttribute("msj", "Error al eliminar.");
                            request.setAttribute("tipo", "ERROR");
                        }
                        request.setAttribute("listaUsuarios", dao.listarUsuariosCompletos());
                        request.getRequestDispatcher(VISTA_CRUD).forward(request, response);
                        break;
                        
                    case "editar":
                        editar(request, response, dao);
                        break;
                        
                    default:
                        response.sendRedirect(request.getContextPath() + VISTA_REGISTRAR);
                }
            } else {
                response.sendRedirect(request.getContextPath() + VISTA_REGISTRAR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msj", "Error servidor: " + e.getMessage());
            request.getRequestDispatcher(VISTA_CRUD).forward(request, response);
        }
    }

    private void registrar(HttpServletRequest request, HttpServletResponse response, boolean esAdmin) throws Exception {
        Persona p = new Persona();
        Usuario u = new Usuario(); 
        
        try {
            // --- 1. DATOS PERSONALES ---
            p.setCodigoPersona(request.getParameter("txtCedula"));
            p.setCedula(request.getParameter("txtCedula"));
            p.setNombre(request.getParameter("txtNombre"));
            p.setApellido(request.getParameter("txtApellido"));
            p.setEmail(request.getParameter("txtEmail"));
            
            String fechaStr = request.getParameter("txtFechaNac");
            if (fechaStr != null && !fechaStr.isEmpty()) {
                p.setFechaNaci(new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr));
            }
            try { p.setCargas(Integer.parseInt(request.getParameter("txtCargas"))); } catch (Exception e) { p.setCargas(0); }

            Sexo s = new Sexo(); s.setCodigoSexo(request.getParameter("cboSexo")); p.setCodigoSexo(s);
            EstadoCivil ec = new EstadoCivil(); ec.setCodigoEstciv(request.getParameter("cboEstadoCivil")); p.setCodigoEstciv(ec);
            p.setDireccion(request.getParameter("txtDireccion"));
            p.setCelular(request.getParameter("txtCelular"));
            p.setTelDom(request.getParameter("txtTelDom"));

            // --- 2. USUARIO ---
            String login = (p.getEmail() != null && p.getEmail().contains("@")) ? p.getEmail().split("@")[0] : p.getCedula();
            u.setLogin(login);
            u.setPersona(p);

            // --- 3. CLAVE Y TOKEN ---
            String clavePlana = "";

            if (esAdmin) {
                // Genera random (8 chars) y Token "1" (Obliga cambio)
                clavePlana = UUID.randomUUID().toString().substring(0, 8);
                u.setTokenRecuperacion("1"); 
            } else {
                // Público: Usa input y Token "0"
                clavePlana = request.getParameter("txtPassword");
                u.setTokenRecuperacion("0");
            }

            u.setPassword(Encriptador.sha256(clavePlana));

            // --- 4. GUARDAR ---
            DAOPERSONA dao = new DAOPERSONA();
            
            if (dao.registrarInvitado(u)) { 
                
                if (esAdmin) {
                    // Envía correo directo
                    enviarCorreoSMTP(p.getEmail(), clavePlana);
                    // ESTE ES EL MENSAJE QUE VERÁS EN EL JSP:
                    request.setAttribute("msj", "Registro Exitoso. Las credenciales fueron enviadas al correo: " + p.getEmail());
                } else {
                    request.setAttribute("msj", "Registro Exitoso.");
                }
                request.setAttribute("tipo", "OK"); // Esto pone la alerta en verde
            } else {
                request.setAttribute("msj", "Error: Ya existe cédula o usuario.");
                request.setAttribute("tipo", "ERROR"); // Esto pone la alerta en rojo
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("msj", "Error: " + e.getMessage());
            request.setAttribute("tipo", "ERROR");
        }

        // REDIRECCIONAR
        if (esAdmin) {
            DAOPERSONA dao = new DAOPERSONA();
            request.setAttribute("listaUsuarios", dao.listarUsuariosCompletos());
            request.getRequestDispatcher(VISTA_CRUD).forward(request, response);
        } else {
            request.getRequestDispatcher(VISTA_REGISTRAR).forward(request, response);
        }
    }

    private void editar(HttpServletRequest request, HttpServletResponse response, DAOPERSONA dao) throws Exception {
        Usuario uEdit = new Usuario();
        Persona pEdit = new Persona();
        pEdit.setCodigoPersona(request.getParameter("txtCodigoEdit"));
        pEdit.setNombre(request.getParameter("txtNombreEdit"));
        pEdit.setApellido(request.getParameter("txtApellidoEdit"));
        pEdit.setEmail(request.getParameter("txtEmailEdit"));
        pEdit.setCelular(request.getParameter("txtCelularEdit"));
        pEdit.setDireccion(request.getParameter("txtDireccionEdit"));
        try { pEdit.setCargas(Integer.parseInt(request.getParameter("txtCargasEdit"))); } catch(Exception e) { pEdit.setCargas(0); }
        uEdit.setLogin(request.getParameter("txtLoginEdit"));
        uEdit.setPersona(pEdit);

        if(dao.actualizarDatos(uEdit)) {
            request.setAttribute("msj", "Actualizado correctamente.");
            request.setAttribute("tipo", "OK");
        } else {
            request.setAttribute("msj", "Error al actualizar.");
            request.setAttribute("tipo", "ERROR");
        }
        request.setAttribute("listaUsuarios", dao.listarUsuariosCompletos());
        request.getRequestDispatcher(VISTA_CRUD).forward(request, response);
    }

    private void reporteUsuario(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=ReporteUsuario.pdf");

        try {
            String codigoPersona = request.getParameter("codigo");
            if (codigoPersona == null) return;

            DAOPERSONA dao = new DAOPERSONA();
            Usuario u = dao.obtenerUsuarioPorCodigo(codigoPersona);
            if (u == null) return;

            PDF pdf = new PDF();
            PDFPage page = pdf.newPage("A4");
            PDFStyle titulo = new PDFStyle();
            titulo.setFont(new StandardFont(StandardFont.HELVETICA), 18);
            titulo.setFillColor(Color.BLACK);
            PDFStyle texto = new PDFStyle();
            texto.setFont(new StandardFont(StandardFont.HELVETICA), 12);

            float y = page.getHeight() - 50;
            page.setStyle(titulo);
            page.drawText("REPORTE DEL USUARIO", 50, y);
            y -= 40;

            Persona p = u.getPersona();
            page.setStyle(texto);
            page.drawText("Código: " + p.getCodigoPersona(), 50, y); y -= 20;
            page.drawText("Cédula: " + p.getCedula(), 50, y); y -= 20;
            page.drawText("Nombre: " + p.getNombre() + " " + p.getApellido(), 50, y); y -= 20;
            page.drawText("Email: " + p.getEmail(), 50, y); y -= 20;
            page.drawText("Login: " + u.getLogin(), 50, y); y -= 20;

            OutputStream out = response.getOutputStream();
            pdf.render(out);
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // --- TU MÉTODO DE CORREO (Directo y con tus credenciales) ---
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
            message.setSubject("Bienvenido al Sistema - Credenciales");
            message.setText("Usuario: " + destino + "\nContraseña Temporal: " + password + "\n\nDebe cambiarla al ingresar.");

            Transport.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override protected void doGet(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException { processRequest(r, s); }
    @Override protected void doPost(HttpServletRequest r, HttpServletResponse s) throws ServletException, IOException { processRequest(r, s); }
}