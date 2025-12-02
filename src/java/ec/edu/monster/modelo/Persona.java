/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

import java.util.Date;


public class Persona {
    private String codigoPersona; // <p> Characters (10)
    
    private Sexo codigoSexo;   // <f1> Characters (1) (FK a SEXO)
    private EstadoCivil codigoEstciv; // <f2> Characters (3) (FK a ESTADO_CIVIL)
    
    private String nombre;        // Variable characters (15)
    private String apellido;      // Variable characters (15)
    private String cedula;        // Characters (10)
    private Date fechaNaci;       // Date
    private int cargas;           // Number (2)
    private String direccion;     // Variable characters (100)
    private String celular;       // Characters (10)
    private String telDom;        // Characters (10)
    private String email;         // Variable characters (100)

    public Persona() {
    }

    public String getCodigoPersona() {
        return codigoPersona;
    }

    public void setCodigoPersona(String codigoPersona) {
        this.codigoPersona = codigoPersona;
    }

    public Sexo getCodigoSexo() {
        return codigoSexo;
    }

    public void setCodigoSexo(Sexo codigoSexo) {
        this.codigoSexo = codigoSexo;
    }

    public EstadoCivil getCodigoEstciv() {
        return codigoEstciv;
    }

    public void setCodigoEstciv(EstadoCivil codigoEstciv) {
        this.codigoEstciv = codigoEstciv;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public Date getFechaNaci() {
        return fechaNaci;
    }

    public void setFechaNaci(Date fechaNaci) {
        this.fechaNaci = fechaNaci;
    }

    public int getCargas() {
        return cargas;
    }

    public void setCargas(int cargas) {
        this.cargas = cargas;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getTelDom() {
        return telDom;
    }

    public void setTelDom(String telDom) {
        this.telDom = telDom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
