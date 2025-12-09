package ec.edu.monster.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAOROLES extends Conexion {

    // 1. LISTAR ROLES
    public List<Roles> listarRoles() throws Exception {
        List<Roles> lista = new ArrayList<>();
        // Traemos roles de empleados (excepto INV)
        String sql = "SELECT PEROL_CODIGO, PEROL_DESCRI FROM PEROL_ROLES WHERE PEROL_CODIGO != 'INV'";
        try (Connection cn = conectar(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Roles r = new Roles();
                r.setCodigoRol(rs.getString("PEROL_CODIGO"));
                r.setDescriRol(rs.getString("PEROL_DESCRI"));
                lista.add(r);
            }
            // Agregamos ESTUDIANTE manual
            Roles est = new Roles(); 
            est.setCodigoRol("EST"); 
            est.setDescriRol("ESTUDIANTE"); 
            lista.add(est);
        }
        return lista;
    }

    // 2. DISPONIBLES (Invitados en Empleados que NO son Estudiantes)
    public List<Empleado> listarDisponibles() throws Exception {
        List<Empleado> lista = new ArrayList<>();
        String sql = "SELECT P.PEPER_CODIGO, P.PEPER_NOMBRE, P.PEPER_APELLIDO FROM PEEMP_EMPLE E "
                   + "INNER JOIN PEPER_PERS P ON E.PEPER_CODIGO = P.PEPER_CODIGO "
                   + "WHERE (E.PEROL_CODIGO = 'INV' OR E.PEROL_CODIGO IS NULL) "
                   + "AND NOT EXISTS (SELECT 1 FROM AEEST_ESTU S WHERE S.PEPER_CODIGO = P.PEPER_CODIGO) "
                   + "ORDER BY P.PEPER_APELLIDO";
        
        try (Connection cn = conectar(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Empleado e = new Empleado();
                e.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                e.setNombre(rs.getString("PEPER_NOMBRE"));
                e.setApellido(rs.getString("PEPER_APELLIDO"));
                lista.add(e);
            }
        }
        return lista;
    }

    // 3. ASIGNADOS
    public List<Empleado> listarAsignados(String codigoRol) throws Exception {
        List<Empleado> lista = new ArrayList<>();
        String sql;
        if ("EST".equals(codigoRol)) {
            // Tabla Estudiantes
            sql = "SELECT P.PEPER_CODIGO, P.PEPER_NOMBRE, P.PEPER_APELLIDO FROM AEEST_ESTU S INNER JOIN PEPER_PERS P ON S.PEPER_CODIGO = P.PEPER_CODIGO ORDER BY P.PEPER_APELLIDO";
        } else {
            // Tabla Empleados
            sql = "SELECT P.PEPER_CODIGO, P.PEPER_NOMBRE, P.PEPER_APELLIDO FROM PEEMP_EMPLE E INNER JOIN PEPER_PERS P ON E.PEPER_CODIGO = P.PEPER_CODIGO WHERE E.PEROL_CODIGO = ? ORDER BY P.PEPER_APELLIDO";
        }
        try (Connection cn = conectar(); PreparedStatement ps = cn.prepareStatement(sql)) {
            if (!"EST".equals(codigoRol)) ps.setString(1, codigoRol);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Empleado e = new Empleado();
                    e.setCodigoPersona(rs.getString("PEPER_CODIGO"));
                    e.setNombre(rs.getString("PEPER_NOMBRE"));
                    e.setApellido(rs.getString("PEPER_APELLIDO"));
                    lista.add(e);
                }
            }
        }
        return lista;
    }

    // --- 4. MÉTODO CORREGIDO: GUARDAR CAMBIOS MASIVOS ---
    public boolean guardarCambiosMasivos(String rolDestino, String[] idsAsignados, String[] idsRemovidos) throws Exception {
        Connection cn = null;
        boolean exito = false;
        
        try {
            cn = conectar();
            cn.setAutoCommit(false); // INICIO TRANSACCIÓN

            // ==========================================
            // A. PROCESAR REMOVIDOS (Volver a INV)
            // ==========================================
            if (idsRemovidos != null && idsRemovidos.length > 0) {
                for (String id : idsRemovidos) {
                    if ("EST".equals(rolDestino)) {
                        // 1. Borrar de Estudiantes
                        try(PreparedStatement ps = cn.prepareStatement("DELETE FROM AEEST_ESTU WHERE PEPER_CODIGO=?")) {
                            ps.setString(1, id); ps.executeUpdate();
                        }
                        // 2. Re-insertar en Empleados como Invitado (Si no existe)
                        if(!existeEnEmpleados(cn, id)) {
                            // IMPORTANTE: Usamos 'INV' como departamento y rol
                            try(PreparedStatement ps = cn.prepareStatement("INSERT INTO PEEMP_EMPLE (PEEMP_CODIGO, PEDEP_CODIGO, PEROL_CODIGO, PEPER_CODIGO) VALUES (?, 'INV', 'INV', ?)")) {
                                String codEmp = "E" + id; if(codEmp.length()>10) codEmp = codEmp.substring(0,10);
                                ps.setString(1, codEmp); ps.setString(2, id); ps.executeUpdate();
                            }
                        }
                    } else {
                        // Era Empleado con Rol -> Update a INV
                        // Si por alguna razón no existiera (raro), lo insertamos
                        if(existeEnEmpleados(cn, id)) {
                            try(PreparedStatement ps = cn.prepareStatement("UPDATE PEEMP_EMPLE SET PEROL_CODIGO='INV', PEDEP_CODIGO='INV' WHERE PEPER_CODIGO=?")) {
                                ps.setString(1, id); ps.executeUpdate();
                            }
                        } else {
                             try(PreparedStatement ps = cn.prepareStatement("INSERT INTO PEEMP_EMPLE (PEEMP_CODIGO, PEDEP_CODIGO, PEROL_CODIGO, PEPER_CODIGO) VALUES (?, 'INV', 'INV', ?)")) {
                                String codEmp = "E" + id; if(codEmp.length()>10) codEmp = codEmp.substring(0,10);
                                ps.setString(1, codEmp); ps.setString(2, id); ps.executeUpdate();
                            }
                        }
                    }
                }
            }

            // ==========================================
            // B. PROCESAR NUEVOS ASIGNADOS
            // ==========================================
            if (idsAsignados != null && idsAsignados.length > 0) {
                for (String id : idsAsignados) {
                    if ("EST".equals(rolDestino)) {
                        // >>> CASO ESTUDIANTE <<<
                        
                        // 1. Insertar en Estudiantes (Solo si no existe ya)
                        // Verificamos existencia explícita para evitar error de Primary Key
                        boolean yaEsEstudiante = false;
                        try(PreparedStatement psCheck = cn.prepareStatement("SELECT 1 FROM AEEST_ESTU WHERE PEPER_CODIGO=?")) {
                            psCheck.setString(1, id);
                            if(psCheck.executeQuery().next()) yaEsEstudiante = true;
                        }
                        
                        if(!yaEsEstudiante) {
                            // Insertamos. Usamos NULL para carrera si la tabla lo permite, o un código default 'C01' si tienes carreras creadas.
                            // Si AECAR_CODIGO es NULLABLE en tu BD, usa NULL. Si no, usa un código válido.
                            // Aquí asumo que AECAR_CODIGO permite NULL o tienes 'C01'.
                            String sqlInsEst = "INSERT INTO AEEST_ESTU (AEEST_CODIGO, AECAR_CODIGO, PEPER_CODIGO) VALUES (?, NULL, ?)"; 
                            try(PreparedStatement ps = cn.prepareStatement(sqlInsEst)) {
                                ps.setString(1, id); // ID Estudiante = Cédula
                                ps.setString(2, id); // ID Persona
                                ps.executeUpdate();
                            }
                        }

                        // 2. Borrar de Empleados (porque ahora es estudiante)
                        try(PreparedStatement ps = cn.prepareStatement("DELETE FROM PEEMP_EMPLE WHERE PEPER_CODIGO=?")) {
                            ps.setString(1, id); ps.executeUpdate();
                        }

                    } else {
                        // >>> CASO EMPLEADO (ADM, DOC, SUP, SEC) <<<
                        
                        // 1. Asegurar que NO esté en estudiantes
                        try(PreparedStatement ps = cn.prepareStatement("DELETE FROM AEEST_ESTU WHERE PEPER_CODIGO=?")) {
                            ps.setString(1, id); ps.executeUpdate();
                        }
                        
                        String dep = obtenerDepartamento(rolDestino);

                        // 2. Verificar si existe en Empleados
                        if(existeEnEmpleados(cn, id)) {
                            // UPDATE (Si ya era invitado o tenía otro rol)
                            try(PreparedStatement ps = cn.prepareStatement("UPDATE PEEMP_EMPLE SET PEROL_CODIGO=?, PEDEP_CODIGO=? WHERE PEPER_CODIGO=?")) {
                                ps.setString(1, rolDestino); 
                                ps.setString(2, dep); 
                                ps.setString(3, id);
                                ps.executeUpdate();
                            }
                        } else {
                            // INSERT (Si venía de ser Estudiante y no existía en empleados)
                            try(PreparedStatement ps = cn.prepareStatement("INSERT INTO PEEMP_EMPLE (PEEMP_CODIGO, PEDEP_CODIGO, PEROL_CODIGO, PEPER_CODIGO) VALUES (?, ?, ?, ?)")) {
                                String codEmp = "E" + id; if(codEmp.length()>10) codEmp = codEmp.substring(0,10);
                                ps.setString(1, codEmp);
                                ps.setString(2, dep);
                                ps.setString(3, rolDestino);
                                ps.setString(4, id);
                                ps.executeUpdate();
                            }
                        }
                    }
                }
            }

            cn.commit();
            exito = true;
        } catch (Exception e) {
            if (cn != null) cn.rollback();
            e.printStackTrace();
            System.out.println("Error en asignación masiva: " + e.getMessage());
            throw e; // Lanzamos la excepción para que el Servlet sepa que falló
        } finally {
            if (cn != null) cn.close();
        }
        return exito;
    }

    // --- Helpers ---
    private boolean existeEnEmpleados(Connection cn, String id) throws Exception {
        String sql = "SELECT 1 FROM PEEMP_EMPLE WHERE PEPER_CODIGO = ?";
        try(PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }

    private String obtenerDepartamento(String rol) {
        // Mapeo exacto según tu imagen de la BD (image_ce1e3e.png)
        if ("ADM".equals(rol)) return "ADM"; // AREA ADMINISTRATIVA
        if ("SEC".equals(rol)) return "ADM"; // Secretaria suele estar en Admin
        if ("DOC".equals(rol)) return "DOC"; // DOCENCIA E INVESTIGACION
        if ("SUP".equals(rol)) return "TIC"; // TECNOLOGIAS DE INFORMACION
        return "INV"; // AREA DE INVITADOS
    }
}