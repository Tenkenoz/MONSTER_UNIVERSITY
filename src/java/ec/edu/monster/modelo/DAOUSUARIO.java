/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author erick
 */
public class DAOUSUARIO extends Conexion {

    public Usuario RecuperarContraseña(Usuario user) throws Exception {
    Usuario usu = null;
    Conexion con;
    Connection cn = null;
    Statement st = null;
    ResultSet rs = null;

    String inputEmail = user.getPersona().getEmail(); // SOLO se usa el email

    // SQL: SOLO busca por email
    String sql = "SELECT "
            + "U.XEUSU_LOGIN, U.XEUSU_PASWD, U.XEEST_CODIGO, "
            + "P.PEPER_CODIGO, P.PEPER_NOMBRE, P.PEPER_APELLIDO, P.PEPER_EMAIL "
            + "FROM XEUSU_USUAR U "
            + "INNER JOIN PEPER_PERS P ON U.PEPER_CODIGO = P.PEPER_CODIGO "
            + "WHERE U.XEEST_CODIGO = '1' "
            + "AND P.PEPER_EMAIL = '" + inputEmail + "'";  // ← SOLO CORREO

    try {
        con = new Conexion();
        cn = con.conectar();
        st = cn.createStatement();
        rs = st.executeQuery(sql);

        if (rs.next()) {
            usu = new Usuario();

            usu.setLogin(rs.getString("XEUSU_LOGIN"));
            usu.setPassword(rs.getString("XEUSU_PASWD")); // ← Aquí RECUPERA la contraseña
            usu.setCodEstado(new Estado());
            usu.getCodEstado().setCodEstado(rs.getString("XEEST_CODIGO"));

            Persona p = new Persona();
            p.setCodigoPersona(rs.getString("PEPER_CODIGO"));
            p.setNombre(rs.getString("PEPER_NOMBRE"));
            p.setApellido(rs.getString("PEPER_APELLIDO"));
            p.setEmail(rs.getString("PEPER_EMAIL"));

            usu.setPersona(p);
        }

    } catch (Exception e) {
        System.out.println("Error en RecuperarContraseña: " + e.getMessage());
        throw e;

    } finally {
        if (rs != null && !rs.isClosed()) rs.close();
        if (st != null && !st.isClosed()) st.close();
        if (cn != null && !cn.isClosed()) cn.close();
    }

    return usu;
}


    public Usuario identificar(Usuario user) throws Exception {
        Usuario usu = null;
        Conexion con;
        Connection cn = null;
        Statement st = null;
        ResultSet rs = null;

        // 1. Recuperamos estrictamente el EMAIL del objeto
        String inputEmail = user.getPersona().getEmail(); 
        String inputPassword = user.getPassword();

        // SQL: Solo busca coincidencia por EMAIL
        String sql = "SELECT "
                + "U.XEUSU_LOGIN, U.XEUSU_PASWD, U.XEEST_CODIGO, "              
                + "P.PEPER_CODIGO, P.PEPER_NOMBRE, P.PEPER_APELLIDO, P.PEPER_EMAIL, " 
                + "EMP.PEEMP_CODIGO, "                                              
                + "R.PEROL_CODIGO, R.PEROL_DESCRI, "                             
                + "EST.AEEST_CODIGO, C.AECAR_NOMBRE "                            
                + "FROM XEUSU_USUAR U "
                + "INNER JOIN PEPER_PERS P ON U.PEPER_CODIGO = P.PEPER_CODIGO "
                // Join para Empleados
                + "LEFT JOIN PEEMP_EMPLE EMP ON P.PEPER_CODIGO = EMP.PEPER_CODIGO "
                + "LEFT JOIN PEROL_ROLES R ON EMP.PEROL_CODIGO = R.PEROL_CODIGO AND EMP.PEDEP_CODIGO = R.PEDEP_CODIGO "
                // Join para Estudiantes
                + "LEFT JOIN AEEST_ESTU EST ON P.PEPER_CODIGO = EST.PEPER_CODIGO "
                + "LEFT JOIN AECAR_CARR C ON EST.AECAR_CODIGO = C.AECAR_CODIGO "
                // Filtros (SOLO EMAIL Y PASSWORD)
                + "WHERE U.XEEST_CODIGO = '1' " 
                + "AND P.PEPER_EMAIL = '" + inputEmail + "' "  // <--- CAMBIO: Solo busca por email
                + "AND U.XEUSU_PASWD = '" + inputPassword + "'";

        con = new Conexion();
        try {
            cn = con.conectar();
            st = cn.createStatement();
            rs = st.executeQuery(sql);

            if (rs.next() == true) {
                usu = new Usuario();

                // Datos básicos del Usuario
                usu.setPassword(rs.getString("XEUSU_PASWD"));
                usu.setCodEstado(new Estado());
                usu.getCodEstado().setCodEstado(rs.getString("XEEST_CODIGO"));

                // --- LÓGICA DE ROLES ---
                String codEmpleado = rs.getString("PEEMP_CODIGO");
                String codEstudiante = rs.getString("AEEST_CODIGO");

                if (codEmpleado != null) {
                    // === CASO 1: ES UN EMPLEADO ===
                    Empleado empleado = new Empleado();

                    empleado.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                    empleado.setNombre(rs.getString("PEPER_NOMBRE"));
                    empleado.setApellido(rs.getString("PEPER_APELLIDO"));
                    empleado.setEmail(rs.getString("PEPER_EMAIL"));
                    empleado.setCodigoEmple(codEmpleado);

                    // Rol
                    empleado.setCodigoRol(new Roles());
                    empleado.getCodigoRol().setCodigoRol(rs.getString("PEROL_CODIGO")); 
                    empleado.getCodigoRol().setDescriRol(rs.getString("PEROL_DESCRI"));

                    usu.setPersona(empleado);

                } else if (codEstudiante != null) {
                    // === CASO 2: ES UN ESTUDIANTE ===
                    Estudiante estudiante = new Estudiante();

                    estudiante.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                    estudiante.setNombre(rs.getString("PEPER_NOMBRE"));
                    estudiante.setApellido(rs.getString("PEPER_APELLIDO"));
                    estudiante.setEmail(rs.getString("PEPER_EMAIL"));
                    estudiante.setCodigoEstu(codEstudiante);

                    // Carrera
                    estudiante.setCodigoCarr(new Carrera());
                    estudiante.getCodigoCarr().setNombreCarr(rs.getString("AECAR_NOMBRE"));

                    usu.setPersona(estudiante);

                } else {
                    // === CASO 3: INVITADO (Sin rol específico en BD) ===
                    Empleado invitado = new Empleado();

                    invitado.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                    invitado.setNombre(rs.getString("PEPER_NOMBRE"));
                    invitado.setApellido(rs.getString("PEPER_APELLIDO"));
                    invitado.setEmail(rs.getString("PEPER_EMAIL"));
                    
                    invitado.setCodigoRol(new Roles());
                    invitado.getCodigoRol().setCodigoRol("INV");
                    invitado.getCodigoRol().setDescriRol("INVITADO");

                    usu.setPersona(invitado);
                }
            }
        } catch (Exception e) {
            System.out.println("Error en DAOUSUARIO: " + e.getMessage());
            throw e; 
        } finally {
            if (rs != null && !rs.isClosed()) rs.close();
            if (st != null && !st.isClosed()) st.close();
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return usu;
    }
}