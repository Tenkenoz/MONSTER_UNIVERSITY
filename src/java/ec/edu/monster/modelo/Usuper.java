/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

import java.util.Date;

/**
 *
 * @author erick
 */
public class Usuper {
    private Usuario usuario;
    private Perfil perfil;
    private Date Asignacion;
    private Date Retiro;

    public Usuper() {
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public Date getAsignacion() {
        return Asignacion;
    }

    public void setAsignacion(Date Asignacion) {
        this.Asignacion = Asignacion;
    }

    public Date getRetiro() {
        return Retiro;
    }

    public void setRetiro(Date Retiro) {
        this.Retiro = Retiro;
    }
    
    
    
}
