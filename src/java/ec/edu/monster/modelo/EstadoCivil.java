/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

/**
 *
 * @author erick
 */
public class EstadoCivil {
    private String codigoEstciv; // <p> Characters (3)
    private String descEstciv;   // Variable characters (50)

    public EstadoCivil() {
    }

    public String getCodigoEstciv() {
        return codigoEstciv;
    }

    public void setCodigoEstciv(String codigoEstciv) {
        this.codigoEstciv = codigoEstciv;
    }

    public String getDescEstciv() {
        return descEstciv;
    }

    public void setDescEstciv(String descEstciv) {
        this.descEstciv = descEstciv;
    }
    
}
