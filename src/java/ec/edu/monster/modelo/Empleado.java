/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

/**
 *
 * @author erick
 */
public class Empleado extends Persona{
    private String codigoEmple; // <p> Characters (10) (PK, se asume igual que CodigoPersona)
    
    // Claves Foráneas (FKs)
    private Departamento codigoDepar; // <f1> Characters (3) (FK a DEPARTAMENTO)
    private Roles codigoRol;   // <f2> Characters (3) (FK a ROLES)

    public Empleado() {
    }

    public String getCodigoEmple() {
        return codigoEmple;
    }

    public void setCodigoEmple(String codigoEmple) {
        this.codigoEmple = codigoEmple;
    }

    public Departamento getCodigoDepar() {
        return codigoDepar;
    }

    public void setCodigoDepar(Departamento codigoDepar) {
        this.codigoDepar = codigoDepar;
    }

    public Roles getCodigoRol() {
        return codigoRol;
    }

    public void setCodigoRol(Roles codigoRol) {
        this.codigoRol = codigoRol;
    }

}
