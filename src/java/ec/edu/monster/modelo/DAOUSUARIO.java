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

        // NOTA: Asumimos que el 'NombreUsuario' es el PEPER_CODIGO (Cédula o ID)
        // y la 'Clave' es XEUSU_PASWD, según tu DDL.
        
        String sql = "SELECT "
                + "U.XEUSU_PASWD, U.XEEST_CODIGO, " // Datos Usuario
                + "P.PEPER_CODIGO, P.PEPER_NOMBRE, P.PEPER_APELLIDO, " // Datos Persona
                + "EMP.PEEMP_CODIGO, R.PEROL_DESCRI, " // Datos Empleado y Rol (Si existen)
                + "EST.AEEST_CODIGO, C.AECAR_NOMBRE "   // Datos Estudiante y Carrera (Si existen)
                + "FROM XEUSU_USUAR U "
                + "INNER JOIN PEPER_PERS P ON U.PEPER_CODIGO = P.PEPER_CODIGO "
                // Join para ver si es EMPLEADO (y obtener su Rol)
                + "LEFT JOIN PEEMP_EMPLE EMP ON P.PEPER_CODIGO = EMP.PEPER_CODIGO "
                + "LEFT JOIN PEROL_ROLES R ON EMP.PEROL_CODIGO = R.PEROL_CODIGO AND EMP.PEDEP_CODIGO = R.PEDEP_CODIGO "
                // Join para ver si es ESTUDIANTE (y obtener su Carrera)
                + "LEFT JOIN AEEST_ESTU EST ON P.PEPER_CODIGO = EST.PEPER_CODIGO "
                + "LEFT JOIN AECAR_CARR C ON EST.AECAR_CODIGO = C.AECAR_CODIGO "
                // Filtros de Login
                + "WHERE U.XEEST_CODIGO = '1' " // Asumiendo '1' es Activo
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
                
                // Llenamos el Estado (Objeto)
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
                    
                } 
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (rs != null && rs.isClosed() == false) {
                rs.close();
            }
            rs = null;
            if (st != null && st.isClosed() == false) {
                st.close();
            }
            st = null;
            if (cn != null & cn.isClosed() == false) {
                cn.close();
            }
            cn = null;
        }
        return usu;
    }
}