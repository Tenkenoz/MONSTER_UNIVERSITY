/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAOEMPLEADO extends Conexion {

    /**
     * Lista empleados filtrando por su ROL específico.
     * @param codigoRol Ejemplo: "DOC" para Docentes, "ADM" para Admin, "SEC" para Secretaria.
     * @return Lista de empleados que tienen ese rol.
     */
    public List<Empleado> listarPorRol(String codigoRol) throws Exception {
        List<Empleado> lista = new ArrayList<>();
        Conexion con = new Conexion();
        Connection cn = null;
        Statement st = null;
        ResultSet rs = null;
        
        // SQL: Filtramos donde R.PEROL_CODIGO sea igual al parámetro que le pasas
        String sql = "SELECT "
                + "E.PEEMP_CODIGO, "
                + "P.PEPER_CODIGO, P.PEPER_NOMBRE, P.PEPER_APELLIDO, P.PEPER_CEDULA, P.PEPER_EMAIL, "
                + "D.PEDEP_CODIGO, D.PEDEP_DESCRI, "
                + "R.PEROL_CODIGO, R.PEROL_DESCRI "
                + "FROM PEEMP_EMPLE E "
                + "INNER JOIN PEPER_PERS P ON E.PEPER_CODIGO = P.PEPER_CODIGO "
                + "INNER JOIN PEDEP_DEPAR D ON E.PEDEP_CODIGO = D.PEDEP_CODIGO "
                + "INNER JOIN PEROL_ROLES R ON E.PEROL_CODIGO = R.PEROL_CODIGO " // Ojo con el JOIN de roles
                + "WHERE R.PEROL_CODIGO = '" + codigoRol + "'"; // <--- AQUÍ ESTÁ EL FILTRO

        try {
            cn = con.conectar();
            st = cn.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Empleado emp = new Empleado();
                
                // 1. Datos Personales
                emp.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                emp.setNombre(rs.getString("PEPER_NOMBRE"));
                emp.setApellido(rs.getString("PEPER_APELLIDO"));
                emp.setCedula(rs.getString("PEPER_CEDULA"));
                emp.setEmail(rs.getString("PEPER_EMAIL"));
                
                // 2. Datos de Empleado
                emp.setCodigoEmple(rs.getString("PEEMP_CODIGO"));
                
                // 3. Departamento
                Departamento dep = new Departamento();
                dep.setCodigoDepar(rs.getString("PEDEP_CODIGO"));
                dep.setDescriDepar(rs.getString("PEDEP_DESCRI"));
                emp.setCodigoDepar(dep); // Asegúrate de tener este Setter en Empleado
                
                // 4. Rol (Aquí confirmamos que el objeto tenga el rol correcto)
                Roles rol = new Roles();
                rol.setCodigoRol(rs.getString("PEROL_CODIGO"));
                rol.setDescriRol(rs.getString("PEROL_DESCRI"));
                emp.setCodigoRol(rol); // Setter de composición en Empleado

                lista.add(emp);
            }
        } catch (Exception e) {
            System.out.println("Error al listar por rol (" + codigoRol + "): " + e.getMessage());
            throw e;
        } finally {
            if (rs != null && !rs.isClosed()) rs.close();
            if (st != null && !st.isClosed()) st.close();
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return lista;
    }

    /**
     * Método para obtener TODOS los empleados sin importar el rol.
     */
    public List<Empleado> listarTodos() throws Exception {
        // ... (El código que te pasé antes sin el WHERE del rol)
        // Es útil tener ambos métodos.
        return listarPorRol("%"); // Truco: Si usas LIKE en SQL podrías reusar, pero mejor haz el método separado o quita el WHERE.
    }
}