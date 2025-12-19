package ec.edu.monster.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAOUSUARIO extends Conexion {

    public Usuario identificar(Usuario user) throws Exception {
        Usuario usu = null;
        // Agregamos XEUSU_TOKEN_REC a la consulta
        String sql = "SELECT U.XEUSU_LOGIN, U.XEUSU_TOKEN_REC, P.PEPER_CODIGO, P.PEPER_NOMBRE, P.PEPER_APELLIDO, P.PEPER_EMAIL, "
                   + "E.PEEMP_CODIGO, E.PEROL_CODIGO, S.AEEST_CODIGO " 
                   + "FROM XEUSU_USUAR U "
                   + "INNER JOIN PEPER_PERS P ON U.PEPER_CODIGO = P.PEPER_CODIGO "
                   + "LEFT JOIN PEEMP_EMPLE E ON P.PEPER_CODIGO = E.PEPER_CODIGO "
                   + "LEFT JOIN AEEST_ESTU S ON P.PEPER_CODIGO = S.PEPER_CODIGO "
                   + "WHERE (U.XEUSU_LOGIN = ? OR P.PEPER_EMAIL = ? OR P.PEPER_CEDULA = ?) "
                   + "AND U.XEUSU_PASWD = ? AND U.XEEST_CODIGO = '1'"; 

        try (Connection cn = conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getLogin()); 
            ps.setString(3, user.getLogin()); 
            ps.setString(4, user.getPassword()); 

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usu = new Usuario();
                    usu.setLogin(rs.getString("XEUSU_LOGIN"));
                    // MAPEAR EL TOKEN
                    usu.setToken(rs.getString("XEUSU_TOKEN_REC")); 
                    
                    Persona p;
                    if (rs.getString("PEEMP_CODIGO") != null) {
                        Empleado emp = new Empleado();
                        emp.setCodigoEmple(rs.getString("PEEMP_CODIGO"));
                        Roles rol = new Roles();
                        rol.setCodigoRol(rs.getString("PEROL_CODIGO"));
                        emp.setCodigoRol(rol);
                        p = emp;
                    } else if (rs.getString("AEEST_CODIGO") != null) {
                        Estudiante est = new Estudiante();
                        est.setCodigoEstu(rs.getString("AEEST_CODIGO"));
                        p = est;
                    } else {
                        p = new Persona(); 
                    }
                    
                    p.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                    p.setNombre(rs.getString("PEPER_NOMBRE"));
                    p.setApellido(rs.getString("PEPER_APELLIDO"));
                    p.setEmail(rs.getString("PEPER_EMAIL"));
                    
                    usu.setPersona(p);
                }
            }
        }
        return usu;
    }

    public Usuario buscarPorEmail(String email) throws Exception {
        Usuario usu = null;
        String sql = "SELECT U.XEUSU_LOGIN FROM XEUSU_USUAR U "
                   + "INNER JOIN PEPER_PERS P ON U.PEPER_CODIGO = P.PEPER_CODIGO "
                   + "WHERE P.PEPER_EMAIL = ?";
        
        try (Connection cn = conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                usu = new Usuario();
                usu.setLogin(rs.getString("XEUSU_LOGIN"));
            }
        }
        return usu;
    }

    // ACTUALIZA PASS Y TOKEN (Para marcar si requiere cambio o no)
    public void actualizarPasswordYToken(String login, String newPassHash, String tokenState) throws Exception {
        String sql = "UPDATE XEUSU_USUAR SET XEUSU_PASWD = ?, XEUSU_TOKEN_REC = ? WHERE XEUSU_LOGIN = ?";
        try (Connection cn = conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, newPassHash);
            ps.setString(2, tokenState); // '1' = Obligatorio, '0' = Normal
            ps.setString(3, login);
            ps.executeUpdate();
        }
    }
    
    public List<Sistema> obtenerMenuPorPerfil(String codPerfil) throws Exception {
    List<Sistema> listaSistemas = new ArrayList<>();
    Connection cn = null;
    
    // USAMOS INNER JOIN: Solo trae las opciones que existen en la tabla de permisos (XEOXP_OPCPE)
    String sql = "SELECT S.XESIS_CODIGO, S.XESIS_DESCRI, " +
                 "       O.XEOPC_CODIGO, O.XEOPC_DESCRI " +
                 "FROM XESIS_SISTE S " +
                 "INNER JOIN XEOPC_OPCIO O ON S.XESIS_CODIGO = O.XESIS_CODIGO " +
                 "INNER JOIN XEOXP_OPCPE P ON O.XEOPC_CODIGO = P.XEOPC_CODIGO " +
                 "WHERE P.XEPER_CODIGO = ? " +
                 "ORDER BY S.XESIS_CODIGO, O.XEOPC_CODIGO";

    try {
        cn = conectar();
        PreparedStatement ps = cn.prepareStatement(sql);
        ps.setString(1, codPerfil); 
        ResultSet rs = ps.executeQuery();

        String codSisActual = "";
        Sistema sisTemp = null;

        while (rs.next()) {
            String sisCodigo = rs.getString("XESIS_CODIGO");
            String sisNombre = rs.getString("XESIS_DESCRI");
            String opcCodigo = rs.getString("XEOPC_CODIGO");
            String opcNombre = rs.getString("XEOPC_DESCRI");

            // 1. Control de ruptura (Agrupamiento por Sistema)
            if (!sisCodigo.equals(codSisActual)) {
                sisTemp = new Sistema();
                sisTemp.setCodigo(sisCodigo);
                sisTemp.setNombre(sisNombre);
                sisTemp.setOpciones(new ArrayList<>()); 
                
                listaSistemas.add(sisTemp);
                codSisActual = sisCodigo;
            }

            // 2. Crear la opción
            Opcion op = new Opcion();
            op.setCodigo(opcCodigo);
            op.setNombre(opcNombre);
            
            // 3. GENERAR URL DINÁMICA
            // Como tu base de datos no tiene columna URL, la inventamos aquí
            // Ejemplo: Si la opción es "Matricula", intentará ir a "Matricula.jsp"
            // O puedes redirigir todo a un servlet central.
            // Opción A: JSPs directos
             op.setUrl(opcNombre.replaceAll(" ", "") + ".jsp"); 
            
            // Opción B: Servlet Central (Descomenta si prefieres esto)
            // op.setUrl("srvGestion?accion=" + opcCodigo);

            if (sisTemp != null) {
                sisTemp.getOpciones().add(op);
            }
        }
        rs.close();
        ps.close();
    } finally {
        if (cn != null) cn.close();
    }
    return listaSistemas;
}
}