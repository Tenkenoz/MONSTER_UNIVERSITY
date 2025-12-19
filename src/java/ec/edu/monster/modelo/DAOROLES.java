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
public List<Perfil> listarPerfiles() throws Exception {
        List<Perfil> listaPerfiles = new ArrayList<>();
        String query = "SELECT XEPER_CODIGO, XEPER_DESCRI FROM XEPER_PERFI";
        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Perfil perfil = new Perfil();
                perfil.setCod_Perfil(rs.getString("XEPER_CODIGO"));
                perfil.setDesc_Perfil(rs.getString("XEPER_DESCRI"));
                listaPerfiles.add(perfil);
            }
        }
        return listaPerfiles;
    }

    // --- 4. MÉTODO CORREGIDO: GUARDAR CAMBIOS MASIVOS ---
   public boolean guardarCambiosMasivos(String rolDestino, String[] idsAsignados, String[] idsRemovidos) throws Exception {
    Connection cn = null;
    boolean exito = false;

    try {
        cn = conectar();
        cn.setAutoCommit(false); // INICIO TRANSACCIÓN

        // ====================================================================
        // A. PROCESAR REMOVIDOS (Los degradamos a 'INV')
        // ====================================================================
        if (idsRemovidos != null && idsRemovidos.length > 0) {
            for (String id : idsRemovidos) {
                // --- 1. LÓGICA DE TABLAS DE PERSONA (INTACTA - NO LA HE TOCADO) ---
                if ("EST".equals(rolDestino)) {
                    try(PreparedStatement ps = cn.prepareStatement("DELETE FROM AEEST_ESTU WHERE PEPER_CODIGO=?")) {
                        ps.setString(1, id); ps.executeUpdate();
                    }
                    if(!existeEnEmpleados(cn, id)) {
                        try(PreparedStatement ps = cn.prepareStatement("INSERT INTO PEEMP_EMPLE (PEEMP_CODIGO, PEDEP_CODIGO, PEROL_CODIGO, PEPER_CODIGO) VALUES (?, 'INV', 'INV', ?)")) {
                            String codEmp = "E" + id; if(codEmp.length()>10) codEmp = codEmp.substring(0,10);
                            ps.setString(1, codEmp); ps.setString(2, id); ps.executeUpdate();
                        }
                    }
                } else {
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
                
                // --- 2. ACTUALIZAR TABLA DE PERFILES DE USUARIO (XEUXP_USUPE) ---
                actualizarTablaPerfil(cn, id, "INV"); 
            }
        }

        // ====================================================================
        // B. PROCESAR NUEVOS ASIGNADOS
        // ====================================================================
        if (idsAsignados != null && idsAsignados.length > 0) {
            
            String dep = obtenerDepartamento(rolDestino); 

            for (String id : idsAsignados) {
                
                // --- 1. LÓGICA DE TABLAS DE PERSONA (INTACTA - NO LA HE TOCADO) ---
                if ("EST".equals(rolDestino)) {
                    boolean yaEsEstudiante = false;
                    try(PreparedStatement psCheck = cn.prepareStatement("SELECT 1 FROM AEEST_ESTU WHERE PEPER_CODIGO=?")) {
                        psCheck.setString(1, id);
                        if(psCheck.executeQuery().next()) yaEsEstudiante = true;
                    }
                    
                    if(!yaEsEstudiante) {
                        try(PreparedStatement ps = cn.prepareStatement("INSERT INTO AEEST_ESTU (AEEST_CODIGO, AECAR_CODIGO, PEPER_CODIGO) VALUES (?, NULL, ?)")) {
                            ps.setString(1, id); ps.setString(2, id); ps.executeUpdate();
                        }
                    }
                    try(PreparedStatement ps = cn.prepareStatement("DELETE FROM PEEMP_EMPLE WHERE PEPER_CODIGO=?")) {
                        ps.setString(1, id); ps.executeUpdate();
                    }

                } else {
                    try(PreparedStatement ps = cn.prepareStatement("DELETE FROM AEEST_ESTU WHERE PEPER_CODIGO=?")) {
                        ps.setString(1, id); ps.executeUpdate();
                    }

                    if(existeEnEmpleados(cn, id)) {
                        try(PreparedStatement ps = cn.prepareStatement("UPDATE PEEMP_EMPLE SET PEROL_CODIGO=?, PEDEP_CODIGO=? WHERE PEPER_CODIGO=?")) {
                            ps.setString(1, rolDestino);
                            ps.setString(2, dep);
                            ps.setString(3, id);
                            ps.executeUpdate();
                        }
                    } else {
                        try(PreparedStatement ps = cn.prepareStatement("INSERT INTO PEEMP_EMPLE (PEEMP_CODIGO, PEDEP_CODIGO, PEROL_CODIGO, PEPER_CODIGO) VALUES (?, ?, ?, ?)")) {
                            String codEmp = "E" + id; if(codEmp.length()>10) codEmp = codEmp.substring(0,10);
                            ps.setString(1, codEmp); ps.setString(2, dep); ps.setString(3, rolDestino); ps.setString(4, id);
                            ps.executeUpdate();
                        }
                    }
                }

                // --- 2. ACTUALIZAR TABLA DE PERFILES DE USUARIO (XEUXP_USUPE) ---
                actualizarTablaPerfil(cn, id, rolDestino);
            }
        }

        cn.commit();
        exito = true;
    } catch (Exception e) {
        if (cn != null) cn.rollback();
        e.printStackTrace();
        System.out.println("Error en asignación masiva: " + e.getMessage());
        throw e;
    } finally {
        if (cn != null) cn.close();
    }
    return exito;
}

// ==========================================================================
// MÉTODO AUXILIAR CORREGIDO CON TUS TABLAS REALES (XEUSU_USUAR y XEUXP_USUPE)
// ==========================================================================
private void actualizarTablaPerfil(Connection cn, String idPersona, String codPerfil) throws Exception {
    // 1. Obtener el LOGIN desde la tabla XEUSU_USUAR usando la Cédula/Persona
    String loginUsuario = null;
    String sqlBuscarLogin = "SELECT XEUSU_LOGIN FROM XEUSU_USUAR WHERE PEPER_CODIGO = ?";
    
    try (PreparedStatement ps = cn.prepareStatement(sqlBuscarLogin)) {
        ps.setString(1, idPersona);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                loginUsuario = rs.getString("XEUSU_LOGIN");
            }
        }
    }

    // Si encontramos el usuario, actualizamos sus permisos
    if (loginUsuario != null) {
        // 2. Borrar perfiles anteriores en XEUXP_USUPE para ese Login
        // (Para asegurar que solo tenga el rol nuevo activo)
        String sqlDelete = "DELETE FROM XEUXP_USUPE WHERE XEUSU_LOGIN = ?";
        try (PreparedStatement ps = cn.prepareStatement(sqlDelete)) {
            ps.setString(1, loginUsuario);
            ps.executeUpdate();
        }

        // 3. Insertar el nuevo perfil en XEUXP_USUPE
        // XEPER_CODIGO viene del parámetro (EST, DOC, INV, etc.)
        String sqlInsert = "INSERT INTO XEUXP_USUPE (XEUSU_LOGIN, XEPER_CODIGO, XEUXP_FECASI, XEUXP_FECRET) VALUES (?, ?, ?, NULL)";
        try (PreparedStatement ps = cn.prepareStatement(sqlInsert)) {
            ps.setString(1, loginUsuario);
            ps.setString(2, codPerfil);
            ps.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Fecha Actual
            ps.executeUpdate();
        }
    }
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
    
    public List<Sistema> obtenerArbolMenu(String codPerfil) throws Exception {
        List<Sistema> listaSistemas = new ArrayList<>();
        Connection cn = null;
        
        // Esta consulta hace un LEFT JOIN. 
        // Si P.XEOPC_CODIGO no es nulo, significa que el perfil TIENE esa opción.
        String sql = "SELECT S.XESIS_CODIGO, S.XESIS_DESCRI, " +
                     "       O.XEOPC_CODIGO, O.XEOPC_DESCRI, " +
                     "       (CASE WHEN P.XEOPC_CODIGO IS NOT NULL THEN 1 ELSE 0 END) AS TIENE_PERMISO " +
                     "FROM XESIS_SISTE S " +
                     "INNER JOIN XEOPC_OPCIO O ON S.XESIS_CODIGO = O.XESIS_CODIGO " +
                     "LEFT JOIN XEOXP_OPCPE P ON O.XEOPC_CODIGO = P.XEOPC_CODIGO AND P.XEPER_CODIGO = ? " +
                     "ORDER BY S.XESIS_CODIGO, O.XEOPC_CODIGO";

        try {
            cn = conectar();
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, codPerfil); // Pasamos el perfil seleccionado
            ResultSet rs = ps.executeQuery();

            String codSisActual = "";
            Sistema sisTemp = null;

            while (rs.next()) {
                String sisCodigo = rs.getString("XESIS_CODIGO");
                String sisNombre = rs.getString("XESIS_DESCRI");
                String opcCodigo = rs.getString("XEOPC_CODIGO");
                String opcNombre = rs.getString("XEOPC_DESCRI");
                boolean tienePermiso = rs.getInt("TIENE_PERMISO") == 1;

                // Lógica de Agrupamiento: Si cambia el código de sistema, creamos uno nuevo
                if (!sisCodigo.equals(codSisActual)) {
                    sisTemp = new Sistema();
                    sisTemp.setCodigo(sisCodigo);
                    sisTemp.setNombre(sisNombre);
                    // Inicializamos la lista de hijos para evitar NullPointer
                    sisTemp.setOpciones(new ArrayList<>()); 
                    
                    listaSistemas.add(sisTemp);
                    codSisActual = sisCodigo;
                }

                // Creamos la opción hija
                Opcion op = new Opcion();
                op.setCodigo(opcCodigo);
                op.setNombre(opcNombre);
                op.setAsignado(tienePermiso); // ESTO ACTIVA EL CHECKBOX VISUALMENTE

                // Agregamos la opción al sistema actual
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

    // --- MÉTODO PARA GUARDAR LOS CHECKS (Borra y Re-inserta) ---
    public boolean guardarPermisosPerfil(String codPerfil, String[] opcionesSeleccionadas) throws Exception {
        Connection cn = null;
        boolean exito = false;
        try {
            cn = conectar();
            cn.setAutoCommit(false); // Transacción

            // 1. LIMPIAR: Borrar todos los permisos viejos de este perfil
            String sqlDelete = "DELETE FROM XEOXP_OPCPE WHERE XEPER_CODIGO = ?";
            try (PreparedStatement psDel = cn.prepareStatement(sqlDelete)) {
                psDel.setString(1, codPerfil);
                psDel.executeUpdate();
            }

            // 2. INSERTAR: Guardar solo los que vinieron marcados (Checked)
            if (opcionesSeleccionadas != null && opcionesSeleccionadas.length > 0) {
                String sqlInsert = "INSERT INTO XEOXP_OPCPE (XEOPC_CODIGO, XEPER_CODIGO, XEOXP_FECASI) VALUES (?, ?, ?)";
                try (PreparedStatement psIns = cn.prepareStatement(sqlInsert)) {
                    for (String opcCod : opcionesSeleccionadas) {
                        psIns.setString(1, opcCod);
                        psIns.setString(2, codPerfil);
                        psIns.setDate(3, new java.sql.Date(System.currentTimeMillis())); 
                        psIns.addBatch(); 
                    }
                    psIns.executeBatch();
                }
            }

            cn.commit();
            exito = true;
        } catch (Exception e) {
            if (cn != null) cn.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            if (cn != null) cn.close();
        }
        return exito;
    }
}
    
