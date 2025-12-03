package ec.edu.monster.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement; // Usamos PreparedStatement para evitar errores de sintaxis y seguridad
import java.sql.SQLException;
import java.sql.Date; // Para las fechas SQL

public class DAOPERSONA extends Conexion {

    public boolean registrar(Persona p) throws Exception {
        boolean registrado = false;
        String sql = "INSERT INTO PEPER_PERS "
                + "(PEPER_CODIGO, PSEX_CODIGO, PEESC_CODIGO, PEPER_NOMBRE, PEPER_APELLIDO, "
                + "PEPER_CEDULA, PEPER_FECHANACI, PEPER_CARGAS, PEPER_DIRECCION, "
                + "PEPER_CELULAR, PEPER_TELDOM, PEPER_EMAIL) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection cn = null;
        PreparedStatement pst = null;
        
        try {
            Conexion con = new Conexion();
            cn = con.conectar();
            pst = cn.prepareStatement(sql);

            // 1. PK y Datos básicos
            pst.setString(1, p.getCodigoPersona());
            
            // 2. CLAVES FORÁNEAS (Aquí extraemos el ID del OBJETO)
            pst.setString(2, p.getCodigoSexo().getCodigoSexo());       // PSEX_CODIGO
            pst.setString(3, p.getCodigoEstciv().getCodigoEstciv()); // PEESC_CODIGO
            
            // 3. Resto de datos
            pst.setString(4, p.getNombre());
            pst.setString(5, p.getApellido());
            pst.setString(6, p.getCedula());
            
            // 4. Conversión de Fecha (Java Date -> SQL Date)
            // Asumimos que p.getFechaNaci() devuelve un java.util.Date
            pst.setDate(7, new java.sql.Date(p.getFechaNaci().getTime()));
            
            pst.setInt(8, p.getCargas());
            pst.setString(9, p.getDireccion());
            pst.setString(10, p.getCelular());
            pst.setString(11, p.getTelDom());
            pst.setString(12, p.getEmail());

            // Ejecutar
            if (pst.executeUpdate() > 0) {
                registrado = true;
            }

        } catch (SQLException e) {
            System.out.println("Error al registrar persona: " + e.getMessage());
            throw e;
        } finally {
            if (pst != null) pst.close();
            if (cn != null) cn.close();
        }
        return registrado;
    }
}