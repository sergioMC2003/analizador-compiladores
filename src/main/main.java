package main;

import vista.VentanaPrincipal;
import controlador.ControladorCompilador;

public class main {

    public static void main(String[] args) {

        VentanaPrincipal ventana = new VentanaPrincipal();

        new ControladorCompilador(ventana);

        ventana.setVisible(true);
    }
}