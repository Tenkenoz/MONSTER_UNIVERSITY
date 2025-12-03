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

    public Usuario identificar(Usuario user) throws Exception {
        Usuario usu = null;
        Conexion con;
        Connection cn = null;
        Statement st = null;
        ResultSet rs = null;

        // SQL Optimizado: Agregamos R.PEROL_CODIGO para saber el tipo de empleado
        String sql = "SELECT "
                + "U.XEUSU_PASWD, U.XEEST_CODIGO, "                             // Datos Usuario
                + "P.PEPER_CODIGO, P.PEPER_NOMBRE, P.PEPER_APELLIDO, "          // Datos Persona
                + "EMP.PEEMP_CODIGO, "                                          // Datos Empleado
                + "R.PEROL_CODIGO, R.PEROL_DESCRI, "                            // <--- IMPORTANTE: Traemos el CÓDIGO del Rol
                + "EST.AEEST_CODIGO, C.AECAR_NOMBRE "                           // Datos Estudiante y Carrera
                + "FROM XEUSU_USUAR U "
                + "INNER JOIN PEPER_PERS P ON U.PEPER_CODIGO = P.PEPER_CODIGO "
                // Join para ver si es EMPLEADO (y obtener su Rol)
                + "LEFT JOIN PEEMP_EMPLE EMP ON P.PEPER_CODIGO = EMP.PEPER_CODIGO "
                + "LEFT JOIN PEROL_ROLES R ON EMP.PEROL_CODIGO = R.PEROL_CODIGO AND EMP.PEDEP_CODIGO = R.PEDEP_CODIGO "
                // Join para ver si es ESTUDIANTE (y obtener su Carrera)
                + "LEFT JOIN AEEST_ESTU EST ON P.PEPER_CODIGO = EST.PEPER_CODIGO "
                + "LEFT JOIN AECAR_CARR C ON EST.AECAR_CODIGO = C.AECAR_CODIGO "
                // Filtros de Login
                + "WHERE U.XEEST_CODIGO = '1' " 
                + "AND P.PEPER_CODIGO = '" + user.getPersona().getCodigoPersona() + "' " 
                + "AND U.XEUSU_PASWD = '" + user.getPassword() + "'";

        con = new Conexion();
        try {
            cn = con.conectar();
            st = cn.createStatement();
            rs = st.executeQuery(sql);

            if (rs.next() == true) {
                usu = new Usuario();

                // 1. Llenar datos básicos del Usuario
                usu.setPassword(rs.getString("XEUSU_PASWD"));

                // Llenamos el Estado
                usu.setCodEstado(new Estado());
                usu.getCodEstado().setCodEstado(rs.getString("XEEST_CODIGO"));

                // --- LÓGICA PARA DETERMINAR EL ROL (Polimorfismo) ---
                String codEmpleado = rs.getString("PEEMP_CODIGO");
                String codEstudiante = rs.getString("AEEST_CODIGO");

                if (codEmpleado != null) {
                    // === ES UN EMPLEADO ===
                    Empleado empleado = new Empleado();

                    // Datos de Persona
                    empleado.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                    empleado.setNombre(rs.getString("PEPER_NOMBRE"));
                    empleado.setApellido(rs.getString("PEPER_APELLIDO"));
                    empleado.setCodigoEmple(codEmpleado);

                    // Datos del Rol (Composición)
                    empleado.setCodigoRol(new Roles());
                    // AQUÍ ESTÁ LA CLAVE: Guardamos el código (ej: ADM) y la descripción
                    empleado.getCodigoRol().setCodigoRol(rs.getString("PEROL_CODIGO")); 
                    empleado.getCodigoRol().setDescriRol(rs.getString("PEROL_DESCRI"));

                    // Asignamos el Empleado al Usuario
                    usu.setPersona(empleado);

                } else if (codEstudiante != null) {
                    // === ES UN ESTUDIANTE ===
                    Estudiante estudiante = new Estudiante();

                    // Datos de Persona
                    estudiante.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                    estudiante.setNombre(rs.getString("PEPER_NOMBRE"));
                    estudiante.setApellido(rs.getString("PEPER_APELLIDO"));
                    estudiante.setCodigoEstu(codEstudiante);

                    // Datos de Carrera (Composición)
                    estudiante.setCodigoCarr(new Carrera());
                    estudiante.getCodigoCarr().setNombreCarr(rs.getString("AECAR_NOMBRE"));

                    // Asignamos el Estudiante al Usuario
                    usu.setPersona(estudiante);

                } else {
                    // Solo es Persona (Usuario sin rol específico)
                    Persona p = new Persona();
                    p.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                    p.setNombre(rs.getString("PEPER_NOMBRE"));
                    p.setApellido(rs.getString("PEPER_APELLIDO"));
                    usu.setPersona(p);
                }
            }
        } catch (Exception e) {
            System.out.println("Error en DAOUSUARIO: " + e.getMessage());
            throw e; // Recomendado: relanzar la excepción para que el Servlet sepa que falló
        } finally {
            if (rs != null && !rs.isClosed()) rs.close();
            if (st != null && !st.isClosed()) st.close();
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return usu;
    }
}