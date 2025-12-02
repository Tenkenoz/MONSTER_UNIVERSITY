/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

/**
 *
 * @author erick
 */
public class Carrera {
    private String codigoCarr;  // <p> Characters (10)
    private String nombreCarr;  // Variable characters (50)
    private String descripcion; // Variable characters (200)

    public String getCodigoCarr() {
        return codigoCarr;
    }

    public void setCodigoCarr(String codigoCarr) {
        this.codigoCarr = codigoCarr;
    }

    public String getNombreCarr() {
        return nombreCarr;
    }

    public void setNombreCarr(String nombreCarr) {
        this.nombreCarr = nombreCarr;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
}
