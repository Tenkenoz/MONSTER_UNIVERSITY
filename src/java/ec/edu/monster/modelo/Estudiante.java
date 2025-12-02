/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

/**
 *
 * @author erick
 */
public class Estudiante extends Persona {
   private String codigoEstu;  
    private Carrera codigoCarr;    

    public Estudiante() {
    }

    
    public String getCodigoEstu() {
        return codigoEstu;
    }

    public void setCodigoEstu(String codigoEstu) {
        this.codigoEstu = codigoEstu;
    }

    public Carrera getCodigoCarr() {
        return codigoCarr;
    }

    public void setCodigoCarr(Carrera codigoCarr) {
        this.codigoCarr = codigoCarr;
    }



}
