package modelo;

import java.util.ArrayList;
import java.util.List;

public class NodoAST {

    private String nombre;
    private List<NodoAST> hijos;

    public NodoAST(String nombre) {
        this.nombre = nombre;
        this.hijos = new ArrayList<>();
    }

    public void agregarHijo(NodoAST hijo) {
        hijos.add(hijo);
    }

    public String getNombre() {
        return nombre;
    }

    public List<NodoAST> getHijos() {
        return hijos;
    }
}