/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author erick
 */
public class Conexion {
    private final String baseDatos = "monster_university";
    private final String servidor = "jdbc:mysql://localhost/"+baseDatos;
    private String usuario= "root";
    private String clave="";
    Connection conexion = null;
   
    
     public Connection conectar() {
      try{
        Class.forName("com.mysql.jdbc.Driver");
        conexion=DriverManager.getConnection(servidor, usuario, clave);
        System.out.println("Conectado a la base de datos local");
    }catch(ClassNotFoundException | SQLException ex){
        Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
    } 
      return conexion;
    }


    public void desconectar() {
        conectar();
    try{
            conexion.close();
            System.out.println("Desconectado de la base de datos local");
        }catch(SQLException ex){
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
    }    
    }

    public boolean testearConexion() {
        try{
            return !conexion.isClosed();
        }catch(SQLException ex){
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, ex);
    return false;
        }
    }
    
}
