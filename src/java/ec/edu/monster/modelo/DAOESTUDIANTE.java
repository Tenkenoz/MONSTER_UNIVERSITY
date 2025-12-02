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

public class DAOESTUDIANTE extends Conexion {

    // Listar todos los estudiantes (Para Secretaria o Admin)
    public List<Estudiante> listar() throws Exception {
        List<Estudiante> lista = new ArrayList<>();
        Conexion con = new Conexion();
        Connection cn = null;
        Statement st = null;
        ResultSet rs = null;
        
        String sql = "SELECT "
                + "E.AEEST_CODIGO, "
                + "P.PEPER_CODIGO, P.PEPER_NOMBRE, P.PEPER_APELLIDO, P.PEPER_EMAIL, "
                + "C.AECAR_CODIGO, C.AECAR_NOMBRE "
                + "FROM AEEST_ESTU E "
                + "INNER JOIN PEPER_PERS P ON E.PEPER_CODIGO = P.PEPER_CODIGO "
                + "INNER JOIN AECAR_CARR C ON E.AECAR_CODIGO = C.AECAR_CODIGO";

        try {
            cn = con.conectar();
            st = cn.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                Estudiante est = new Estudiante();
                
                // 1. Datos Personales
                est.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                est.setNombre(rs.getString("PEPER_NOMBRE"));
                est.setApellido(rs.getString("PEPER_APELLIDO"));
                est.setEmail(rs.getString("PEPER_EMAIL"));
                
                // 2. Datos de Estudiante
                est.setCodigoEstu(rs.getString("AEEST_CODIGO"));
                
                // 3. Objeto Carrera
                Carrera carr = new Carrera();
                carr.setCodigoCarr(rs.getString("AECAR_CODIGO")); // Asegúrate que tu clase Carrera tenga este setter
                carr.setNombreCarr(rs.getString("AECAR_NOMBRE"));
                
                est.setCodigoCarr(carr); // Setter en clase Estudiante

                lista.add(est);
            }
        } catch (Exception e) {
            System.out.println("Error al listar estudiantes: " + e.getMessage());
            throw e;
        } finally {
            if (rs != null && !rs.isClosed()) rs.close();
            if (st != null && !st.isClosed()) st.close();
            if (cn != null && !cn.isClosed()) cn.close();
        }
        return lista;
    }

    // Buscar estudiante por ID (Para mostrar notas, perfil, etc.)
    public Estudiante buscarPorId(String idEstudiante) throws Exception {
        Estudiante est = null;
        Conexion con = new Conexion();
        Connection cn = null;
        Statement st = null;
        ResultSet rs = null;

        String sql = "SELECT E.AEEST_CODIGO, P.*, C.AECAR_NOMBRE "
                   + "FROM AEEST_ESTU E "
                   + "INNER JOIN PEPER_PERS P ON E.PEPER_CODIGO = P.PEPER_CODIGO "
                   + "INNER JOIN AECAR_CARR C ON E.AECAR_CODIGO = C.AECAR_CODIGO "
                   + "WHERE E.AEEST_CODIGO = '" + idEstudiante + "'";

        try {
            cn = con.conectar();
            st = cn.createStatement();
            rs = st.executeQuery(sql);

            if (rs.next()) {
                est = new Estudiante();
                est.setCodigoEstu(rs.getString("AEEST_CODIGO"));
                est.setNombre(rs.getString("PEPER_NOMBRE"));
                est.setApellido(rs.getString("PEPER_APELLIDO"));
                est.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                
                Carrera carr = new Carrera();
                carr.setNombreCarr(rs.getString("AECAR_NOMBRE"));
                est.setCodigoCarr(carr);
            }
        } catch (Exception e) {
            System.out.println("Error buscando estudiante: " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (cn != null) cn.close();
        }
        return est;
    }
}