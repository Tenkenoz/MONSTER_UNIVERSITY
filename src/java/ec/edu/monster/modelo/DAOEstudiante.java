package ec.edu.monster.modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DAOEstudiante extends Conexion {

    // 1. LISTAR TODOS LOS ESTUDIANTES (Para la tabla del JSP)
    public List<Estudiante> listarEstudiantes() throws Exception {
        List<Estudiante> lista = new ArrayList<>();
        Connection cn = null; Statement st = null; ResultSet rs = null;
        
        String sql = "SELECT EST.AEEST_CODIGO, P.PEPER_CODIGO, P.PEPER_NOMBRE, P.PEPER_APELLIDO, P.PEPER_EMAIL, C.AECAR_NOMBRE "
                   + "FROM AEEST_ESTU EST "
                   + "INNER JOIN PEPER_PERS P ON EST.PEPER_CODIGO = P.PEPER_CODIGO "
                   + "INNER JOIN AECAR_CARR C ON EST.AECAR_CODIGO = C.AECAR_CODIGO "
                   + "ORDER BY P.PEPER_APELLIDO";

        try {
            cn = conectar(); st = cn.createStatement(); rs = st.executeQuery(sql);
            while (rs.next()) {
                Estudiante e = new Estudiante();
                e.setCodigoEstu(rs.getString("AEEST_CODIGO"));
                e.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                e.setNombre(rs.getString("PEPER_NOMBRE"));
                e.setApellido(rs.getString("PEPER_APELLIDO"));
                e.setEmail(rs.getString("PEPER_EMAIL"));
                
                Carrera c = new Carrera();
                c.setNombreCarr(rs.getString("AECAR_NOMBRE"));
                e.setCodigoCarr(c);
                
                lista.add(e);
            }
        } finally {
            if(rs!=null) rs.close(); if(st!=null) st.close(); if(cn!=null) cn.close();
        }
        return lista;
    }

    // 2. OBTENER UN ESTUDIANTE (Para la cabecera del PDF)
    public Estudiante obtenerEstudiante(String codigo) throws Exception {
        Estudiante e = null;
        Connection cn = null; Statement st = null; ResultSet rs = null;
        
        String sql = "SELECT EST.AEEST_CODIGO, P.PEPER_CODIGO, P.PEPER_NOMBRE, P.PEPER_APELLIDO, P.PEPER_EMAIL, C.AECAR_NOMBRE "
                   + "FROM AEEST_ESTU EST "
                   + "INNER JOIN PEPER_PERS P ON EST.PEPER_CODIGO = P.PEPER_CODIGO "
                   + "INNER JOIN AECAR_CARR C ON EST.AECAR_CODIGO = C.AECAR_CODIGO "
                   + "WHERE EST.AEEST_CODIGO = '" + codigo + "'";

        try {
            cn = conectar(); st = cn.createStatement(); rs = st.executeQuery(sql);
            if (rs.next()) {
                e = new Estudiante();
                e.setCodigoEstu(rs.getString("AEEST_CODIGO"));
                e.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                e.setNombre(rs.getString("PEPER_NOMBRE"));
                e.setApellido(rs.getString("PEPER_APELLIDO"));
                e.setEmail(rs.getString("PEPER_EMAIL"));
                
                Carrera c = new Carrera();
                c.setNombreCarr(rs.getString("AECAR_NOMBRE"));
                e.setCodigoCarr(c);
            }
        } finally {
            if(rs!=null) rs.close(); if(st!=null) st.close(); if(cn!=null) cn.close();
        }
        return e;
    }

    // 3. LISTAR NOTAS (Para el cuerpo del PDF)
    public List<Nota> listarNotasEstudiante(String codigoEstu) throws Exception {
        List<Nota> lista = new ArrayList<>();
        Connection cn = null; Statement st = null; ResultSet rs = null;
        
        // Promedio de notas por materia usando tus tablas AENOT_NOTA y AEASI_ASIG
        String sql = "SELECT A.AEASI_NOMBREASIG AS MATERIA, AVG(N.AENOT_NOTA) AS NOTA_FINAL "
                   + "FROM AENOT_NOTA N "
                   + "INNER JOIN AEASI_ASIG A ON N.AEASI_CODIGO = A.AEASI_CODIGO "
                   + "WHERE N.AEEST_CODIGO = '" + codigoEstu + "' "
                   + "GROUP BY A.AEASI_NOMBREASIG";

        try {
            cn = conectar(); st = cn.createStatement(); rs = st.executeQuery(sql);
            while (rs.next()) {
                Nota n = new Nota();
                Materia m = new Materia();
                m.setNombreMateria(rs.getString("MATERIA"));
                n.setMateria(m);
                n.setNotaFinal(rs.getDouble("NOTA_FINAL"));
                lista.add(n);
            }
        } finally {
            if(rs!=null) rs.close(); if(st!=null) st.close(); if(cn!=null) cn.close();
        }
        return lista;
    }
}