package ec.edu.monster.modelo;
import java.util.ArrayList;
import java.util.List;

public class Sistema {
    private String codigo;
    private String nombre;
    private List<Opcion> opciones;

    public Sistema() { opciones = new ArrayList<>(); }
    
    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public List<Opcion> getOpciones() { return opciones; }
    public void setOpciones(List<Opcion> opciones) { this.opciones = opciones; }
    public void addOpcion(Opcion op) { this.opciones.add(op); }
}
