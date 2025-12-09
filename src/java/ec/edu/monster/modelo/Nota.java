/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.modelo;

/**
 *
 * @author erick
 */
public class Nota {
        
    private Materia Materia;
    private  double NotaFinal;

    public void setMateria(Materia Materia) {
        this.Materia = Materia;
    }

    public void setNotaFinal(double NotaFinal) {
        this.NotaFinal = NotaFinal;
    }
    
  
    public Materia getMateria() {
        return Materia;
    }

    public double getNotaFinal() {
        return NotaFinal;
    }

    
    
}
