/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

/**
 *
 * @author erick
 */
public class Roles {
    private String codigoRol;   // <p> Characters (3)
    
    // Claves Foráneas (FKs)
    private Departamento codigoDepar; // <f1> Characters (3) (FK a DEPARTAMENTO)
    
    // Atributos
    private String descriRol;   // Variable characters (50)

    public Roles() {
    }

    public String getCodigoRol() {
        return codigoRol;
    }

    public void setCodigoRol(String codigoRol) {
        this.codigoRol = codigoRol;
    }

    public Departamento getCodigoDepar() {
        return codigoDepar;
    }

    public void setCodigoDepar(Departamento codigoDepar) {
        this.codigoDepar = codigoDepar;
    }


    public String getDescriRol() {
        return descriRol;
    }

    public void setDescriRol(String descriRol) {
        this.descriRol = descriRol;
    }

    
    
}
