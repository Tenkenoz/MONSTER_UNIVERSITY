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
    private static final String VISTA_CRUD = "/ec.edu.monster.vista/Usuarios.jsp";
    private static final String VISTA_REPORTE = "/ec.edu.monster.vista/Seguridad.jsp";

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

// ---------------------------------------------------------------
    // GENERADOR DE PDF NATIVO (SIN LIBRERÍAS EXTERNAS)
    // ESTILO: MONSTER UNIVERSITY
    // ---------------------------------------------------------------
    private void reporteUsuario(HttpServletRequest request, HttpServletResponse response) {
        // 1. Obtener Datos
        String codigoPersona = request.getParameter("codigo");
        if (codigoPersona == null) return;

        DAOPERSONA dao = new DAOPERSONA();
        Usuario u = null;
        try {
            u = dao.obtenerUsuarioPorCodigo(codigoPersona);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        if (u == null) return;

        // 2. Configurar Respuesta HTTP
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=Ficha_" + u.getLogin() + ".pdf");

        try (java.io.OutputStream out = response.getOutputStream()) {
            // Construcción del PDF en memoria
            StringBuilder pdf = new StringBuilder();
            java.util.List<Integer> objOffsets = new java.util.ArrayList<>();

            // --- CABECERA PDF ---
            pdf.append("%PDF-1.4\n");
            
            // --- OBJETOS ---
            
            // OBJ 1: Catálogo
            addObj(pdf, objOffsets);
            pdf.append("<< /Type /Catalog /Pages 2 0 R >>\nendobj\n");

            // OBJ 2: Árbol de Páginas
            addObj(pdf, objOffsets);
            pdf.append("<< /Type /Pages /Kids [3 0 R] /Count 1 >>\nendobj\n");

            // OBJ 3: La Página
            addObj(pdf, objOffsets);
            pdf.append("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Resources << /Font << /F1 4 0 R /F2 5 0 R >> >> /Contents 6 0 R >>\nendobj\n");

            // OBJ 4: Fuente Helvetica (Normal)
            addObj(pdf, objOffsets);
            pdf.append("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n");

            // OBJ 5: Fuente Helvetica Bold (Negrita)
            addObj(pdf, objOffsets);
            pdf.append("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold >>\nendobj\n");

            // --- CONTENIDO GRÁFICO (AQUÍ DIBUJAMOS) ---
            StringBuilder stream = new StringBuilder();
            
            // 1. ENCABEZADO AZUL (Monster Inc Blue #003366 -> RGB: 0, 0.2, 0.4)
            stream.append("0 0.2 0.4 rg\n"); // Color relleno azul
            stream.append("0 742 595 100 re f\n"); // Rectángulo superior (x, y, w, h)
            
            // 2. LOGO "M" (Dibujado vectorial para no depender de imágenes externas)
            // Circulo Verde (#99cc33 -> RGB: 0.6, 0.8, 0.2)
            stream.append("0.6 0.8 0.2 rg\n"); 
            // Dibujar circulo (aproximado con curvas Bezier) en (50, 792) radio 20
            stream.append("50 772 m 50 783 59 792 70 792 c 81 792 90 783 90 772 c 90 761 81 752 70 752 c 59 752 50 761 50 772 c f\n");
            
            // Ojo del Logo (Blanco y Azul)
            stream.append("1 1 1 rg\n"); // Blanco
            stream.append("60 772 m 60 777 65 782 70 782 c 75 782 80 777 80 772 c 80 767 75 762 70 762 c 65 762 60 767 60 772 c f\n");
            stream.append("0 0 0 rg\n"); // Negro (Pupila)
            stream.append("67 772 m 67 774 68 775 70 775 c 72 775 73 774 73 772 c 73 770 72 769 70 769 c 68 769 67 770 67 772 c f\n");

            // 3. TÍTULO UNIVERSIDAD (Blanco)
            stream.append("BT\n/F2 24 Tf\n1 1 1 rg\n110 765 Td\n(MONSTER UNIVERSITY) Tj\nET\n");
            
            // 4. SUBTÍTULO REPORTE (Blanco pequeño)
            stream.append("BT\n/F1 10 Tf\n1 1 1 rg\n110 752 Td\n(SISTEMA DE GESTION ACADEMICA - FICHA DE USUARIO) Tj\nET\n");

            // 5. CAJA DE DATOS (Fondo Gris muy claro)
            stream.append("0.95 0.95 0.95 rg\n");
            stream.append("40 400 515 300 re f\n"); // Caja de fondo
            stream.append("0.8 0.8 0.8 RG\n1 w\n"); // Borde gris
            stream.append("40 400 515 300 re S\n"); // Pintar borde

            // 6. DATOS DEL USUARIO
            Persona p = u.getPersona();
            int yText = 670; // Posición inicial Y
            
            // Título de sección
            stream.append("BT\n/F2 14 Tf\n0 0.2 0.4 rg\n50 ").append(yText).append(" Td\n(INFORMACION DEL USUARIO) Tj\nET\n");
            // Línea separadora azul
            stream.append("0 0.2 0.4 RG\n2 w\n50 ").append(yText - 10).append(" m 545 ").append(yText - 10).append(" l S\n");
            
            yText -= 40;

            // Filas de datos
            drawRow(stream, "CODIGO:", p.getCodigoPersona(), yText); yText -= 25;
            drawRow(stream, "CEDULA:", p.getCedula(), yText); yText -= 25;
            drawRow(stream, "NOMBRES:", p.getNombre() + " " + p.getApellido(), yText); yText -= 25;
            drawRow(stream, "EMAIL:", p.getEmail(), yText); yText -= 25;
            drawRow(stream, "TELEFONO:", p.getCelular(), yText); yText -= 25;
            drawRow(stream, "DIRECCION:", p.getDireccion(), yText); yText -= 25;
            drawRow(stream, "LOGIN:", u.getLogin(), yText); yText -= 25;
            
            String estado = (u.getCodEstado() != null && "1".equals(u.getCodEstado().getCodEstado())) ? "ACTIVO" : "INACTIVO";
            // Dibujar estado con color (Verde si activo, Rojo si no)
            stream.append("BT\n/F2 10 Tf\n0.3 0.3 0.3 rg\n60 ").append(yText).append(" Td\n(ESTADO DE CUENTA:) Tj\nET\n");
            if("ACTIVO".equals(estado)) stream.append("0 0.6 0 rg\n"); else stream.append("0.8 0 0 rg\n");
            stream.append("BT\n/F2 10 Tf\n200 ").append(yText).append(" Td\n(").append(estado).append(") Tj\nET\n");

            // 7. PIE DE PÁGINA
            String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
            stream.append("BT\n/F1 8 Tf\n0.5 0.5 0.5 rg\n40 30 Td\n(Generado el: ").append(fecha).append(") Tj\nET\n");
            stream.append("BT\n/F1 8 Tf\n0.5 0.5 0.5 rg\n450 30 Td\n(Monster Inc. System) Tj\nET\n");

            // OBJ 6: El Stream de Contenido
            addObj(pdf, objOffsets);
            pdf.append("<< /Length ").append(stream.length()).append(" >>\nstream\n");
            pdf.append(stream);
            pdf.append("\nendstream\nendobj\n");

            // --- TABLA DE REFERENCIAS CRUZADAS (XREF) ---
            long xrefPos = pdf.length();
            pdf.append("xref\n0 7\n0000000000 65535 f \n");
            for (int offset : objOffsets) {
                pdf.append(String.format("%010d 00000 n \n", offset));
            }

            // --- TRAILER ---
            pdf.append("trailer\n<< /Size 7 /Root 1 0 R >>\n");
            pdf.append("startxref\n").append(xrefPos).append("\n%%EOF");

            // Escribir al cliente
            out.write(pdf.toString().getBytes("ISO-8859-1")); // Codificación estándar PDF
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Método auxiliar para registrar la posición de los objetos en el PDF
    private void addObj(StringBuilder pdf, java.util.List<Integer> offsets) {
        offsets.add(pdf.length());
        pdf.append(offsets.size()).append(" 0 obj\n");
    }

    // Método auxiliar para dibujar filas de texto en el PDF
    private void drawRow(StringBuilder sb, String label, String value, int y) {
        // Etiqueta (Gris oscuro, Negrita)
        sb.append("BT\n/F2 10 Tf\n0.3 0.3 0.3 rg\n60 ").append(y).append(" Td\n(").append(label).append(") Tj\nET\n");
        // Valor (Negro, Normal)
        sb.append("BT\n/F1 10 Tf\n0 0 0 rg\n200 ").append(y).append(" Td\n(").append(value != null ? value : "-").append(") Tj\nET\n");
        // Línea punteada debajo
        sb.append("0.8 0.8 0.8 RG\n[1 2] 0 d\n1 w\n60 ").append(y - 5).append(" m 540 ").append(y - 5).append(" l S\n[] 0 d\n");
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