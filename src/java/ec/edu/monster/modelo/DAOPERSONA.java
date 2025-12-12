package ec.edu.monster.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAOPERSONA extends Conexion {

    public Usuario obtenerUsuarioPorCodigo(String codigo) throws Exception {
    Usuario u = null;
    String sql = "SELECT P.PEPER_CODIGO, P.PEPER_CEDULA, P.PEPER_NOMBRE, P.PEPER_APELLIDO, "
               + "P.PEPER_EMAIL, P.PEPER_CELULAR, P.PEPER_DIRECCION, P.PEPER_CARGAS, P.PEPER_FECHANACI, "
               + "U.XEUSU_LOGIN, U.XEUSU_FECCRE, U.XEEST_CODIGO "
               + "FROM PEPER_PERS P "
               + "LEFT JOIN XEUSU_USUAR U ON P.PEPER_CODIGO = U.PEPER_CODIGO "
               + "WHERE P.PEPER_CODIGO = ?";

    try (Connection cn = conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
        ps.setString(1, codigo);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                u = new Usuario();
                u.setLogin(rs.getString("XEUSU_LOGIN"));
                u.setFechaCrea(rs.getDate("XEUSU_FECCRE"));

                Estado est = new Estado();
                est.setCodEstado(rs.getString("XEEST_CODIGO"));
                u.setCodEstado(est);

                Persona p = new Persona();
                p.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                p.setCedula(rs.getString("PEPER_CEDULA"));
                p.setNombre(rs.getString("PEPER_NOMBRE"));
                p.setApellido(rs.getString("PEPER_APELLIDO"));
                p.setEmail(rs.getString("PEPER_EMAIL"));
                p.setCelular(rs.getString("PEPER_CELULAR"));
                p.setDireccion(rs.getString("PEPER_DIRECCION"));
                p.setCargas(rs.getInt("PEPER_CARGAS"));
                p.setFechaNaci(rs.getDate("PEPER_FECHANACI"));

                u.setPersona(p);
            }
        }
    }
    return u;
}

    
    
    // --- 1. LISTAR PARA LA TABLA (JOIN COMPLETO) ---
    public List<Usuario> listarUsuariosCompletos() throws Exception {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT P.PEPER_CODIGO, P.PEPER_CEDULA, P.PEPER_NOMBRE, P.PEPER_APELLIDO, "
                   + "P.PEPER_EMAIL, P.PEPER_CELULAR, P.PEPER_DIRECCION, P.PEPER_CARGAS, P.PEPER_FECHANACI, "
                   + "U.XEUSU_LOGIN, U.XEUSU_FECCRE, U.XEEST_CODIGO "
                   + "FROM PEPER_PERS P "
                   + "LEFT JOIN XEUSU_USUAR U ON P.PEPER_CODIGO = U.PEPER_CODIGO "
                   + "ORDER BY P.PEPER_APELLIDO";

        try (Connection cn = conectar(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario u = new Usuario();
                u.setLogin(rs.getString("XEUSU_LOGIN"));
                u.setFechaCrea(rs.getDate("XEUSU_FECCRE"));
                
                Estado est = new Estado();
                est.setCodEstado(rs.getString("XEEST_CODIGO"));
                u.setCodEstado(est);

                Persona p = new Persona();
                p.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                p.setCedula(rs.getString("PEPER_CEDULA"));
                p.setNombre(rs.getString("PEPER_NOMBRE"));
                p.setApellido(rs.getString("PEPER_APELLIDO"));
                p.setEmail(rs.getString("PEPER_EMAIL"));
                p.setCelular(rs.getString("PEPER_CELULAR"));
                p.setDireccion(rs.getString("PEPER_DIRECCION"));
                p.setCargas(rs.getInt("PEPER_CARGAS"));
                p.setFechaNaci(rs.getDate("PEPER_FECHANACI")); // Importante para visualizar
                
                u.setPersona(p);
                lista.add(u);
            }
        }
        return lista;
    }

    // --- 2. REGISTRAR (Lógica base) ---
public boolean registrarInvitado(Usuario u) throws Exception {
    boolean registrado = false;
    Connection cn = null;
    
    // SQLs
    String sqlPersona = "INSERT INTO PEPER_PERS (PEPER_CODIGO, PSEX_CODIGO, PEESC_CODIGO, PEPER_NOMBRE, PEPER_APELLIDO, PEPER_CEDULA, PEPER_FECHANACI, PEPER_CARGAS, PEPER_DIRECCION, PEPER_CELULAR, PEPER_TELDOM, PEPER_EMAIL) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    String sqlEmple = "INSERT INTO PEEMP_EMPLE (PEEMP_CODIGO, PEDEP_CODIGO, PEROL_CODIGO, PEPER_CODIGO) VALUES (?, 'INV', 'INV', ?)";
    
    // ¡OJO AQUÍ! AGREGAMOS EL CAMPO XEUSU_TOKEN_REC
    String sqlUsuario = "INSERT INTO XEUSU_USUAR (XEUSU_LOGIN, XEUSU_PASWD, XEEST_CODIGO, PEPER_CODIGO, XEUSU_PIEFIR, XEUSU_FECCRE, XEUSU_FECMOD, XEUSU_TOKEN_REC) VALUES (?, ?, '1', ?, 'FIRMA_INV', NOW(), NOW(), ?)";

    PreparedStatement pstPersona = null, pstEmple = null, pstUsuario = null;
    try {
        cn = conectar(); cn.setAutoCommit(false); 
        Persona p = u.getPersona();
        
        // 1. Insertar Persona (Igual que antes)
        pstPersona = cn.prepareStatement(sqlPersona);
        pstPersona.setString(1, p.getCodigoPersona());
        pstPersona.setString(2, p.getCodigoSexo().getCodigoSexo());
        pstPersona.setString(3, p.getCodigoEstciv().getCodigoEstciv());
        pstPersona.setString(4, p.getNombre());
        pstPersona.setString(5, p.getApellido());
        pstPersona.setString(6, p.getCedula());
        if (p.getFechaNaci() != null) pstPersona.setDate(7, new java.sql.Date(p.getFechaNaci().getTime()));
        else pstPersona.setNull(7, java.sql.Types.DATE);
        pstPersona.setInt(8, p.getCargas());
        pstPersona.setString(9, p.getDireccion());
        pstPersona.setString(10, p.getCelular());
        pstPersona.setString(11, p.getTelDom());
        pstPersona.setString(12, p.getEmail());
        pstPersona.executeUpdate();

        // 2. Insertar Empleado (Igual que antes)
        pstEmple = cn.prepareStatement(sqlEmple);
        String codigoEmp = "E" + p.getCedula();
        if(codigoEmp.length() > 10) codigoEmp = codigoEmp.substring(0, 10);
        pstEmple.setString(1, codigoEmp);
        pstEmple.setString(2, p.getCodigoPersona());
        pstEmple.executeUpdate();

        // 3. Insertar Usuario (CON EL TOKEN)
        pstUsuario = cn.prepareStatement(sqlUsuario);
        pstUsuario.setString(1, u.getLogin());
        pstUsuario.setString(2, u.getPassword()); // Ya viene encriptada desde el Servlet
        pstUsuario.setString(3, p.getCodigoPersona());
        
        // AQUÍ GUARDAMOS LA BANDERA "1" (O "0")
        pstUsuario.setString(4, u.getTokenRecuperacion()); 
        
        pstUsuario.executeUpdate();

        cn.commit();
        registrado = true;
    } catch (SQLException e) {
        if (cn != null) cn.rollback(); throw e;
    } finally {
        if (cn != null) cn.close();
    }
    return registrado;
}
    // --- 3. ACTUALIZAR ---
    public boolean actualizarDatos(Usuario u) throws Exception {
        // ... (Mismo código de update anterior) ...
        String sql = "UPDATE PEPER_PERS SET PEPER_NOMBRE=?, PEPER_APELLIDO=?, PEPER_EMAIL=?, PEPER_CELULAR=?, PEPER_DIRECCION=?, PEPER_CARGAS=? WHERE PEPER_CODIGO=?";
        try (Connection cn = conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, u.getPersona().getNombre());
            ps.setString(2, u.getPersona().getApellido());
            ps.setString(3, u.getPersona().getEmail());
            ps.setString(4, u.getPersona().getCelular());
            ps.setString(5, u.getPersona().getDireccion());
            ps.setInt(6, u.getPersona().getCargas());
            ps.setString(7, u.getPersona().getCodigoPersona());
            return ps.executeUpdate() > 0;
        }
    }

 
    
    // --- 2. ELIMINAR TODO (Cascada Manual Correcta) ---
    public boolean eliminarTodo(String codigoPersona) throws Exception {
        boolean eliminado = false;
        Connection cn = null;
        try {
            cn = conectar();
            cn.setAutoCommit(false); // Iniciar Transacción

            // 1. Eliminar Usuario (Login/Contraseña)
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM XEUSU_USUAR WHERE PEPER_CODIGO=?")) {
                ps.setString(1, codigoPersona);
                ps.executeUpdate();
            }

            // 2. Eliminar Empleado (Si tiene rol administrativo/docente)
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM PEEMP_EMPLE WHERE PEPER_CODIGO=?")) {
                ps.setString(1, codigoPersona);
                ps.executeUpdate();
            }

            // 3. Eliminar Estudiante (¡ESTO FALTABA!)
            // Si la persona fue registrada como estudiante alguna vez, hay que borrarla de aquí primero
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM AEEST_ESTU WHERE PEPER_CODIGO=?")) {
                ps.setString(1, codigoPersona);
                ps.executeUpdate();
            }
            
            /* NOTA: Si tienes más tablas (como Notas o Matrículas), 
               tendrías que borrarlas aquí antes de borrar a la Persona. */

            // 4. Eliminar Persona (Ahora sí dejará borrar porque no tiene hijos)
            try (PreparedStatement ps = cn.prepareStatement("DELETE FROM PEPER_PERS WHERE PEPER_CODIGO=?")) {
                ps.setString(1, codigoPersona);
                if(ps.executeUpdate() > 0) eliminado = true;
            }

            cn.commit();
        } catch (SQLException e) {
            if (cn != null) cn.rollback();
            System.err.println("Error al eliminar: " + e.getMessage());
            throw e; // Lanza la excepción para verla en el servidor si falla algo más
        } finally {
            if (cn != null) cn.close();
        }
        return eliminado;
    }
}