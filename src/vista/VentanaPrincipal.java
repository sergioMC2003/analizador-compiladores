package vista;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    
    private JTextArea areaCod;
    private JButton botonAnalizar;
    private JButton botonAbrir;
    private JTable tablaTokens;
    private JTextArea areaErrores;

    // Árbol
    private JTree arbol;

    public VentanaPrincipal(){

        setTitle("Analizador de Python");
        setSize(1000,700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inicializarComponentes();
    }

    private void inicializarComponentes(){

        setLayout(new BorderLayout());

        // AREA DE CODIGO
        areaCod = new JTextArea();
        areaCod.setFont(new Font("Consolas", Font.PLAIN, 14));

        JScrollPane scrollCod = new JScrollPane(areaCod);
        scrollCod.setBorder(BorderFactory.createTitledBorder("Codigo Python"));

        // NUMEROS DE LINEA
        NumeroLinea numeroLinea = new NumeroLinea(areaCod);
        scrollCod.setRowHeaderView(numeroLinea);

        // BOTONES
        botonAnalizar = new JButton("Analizar Codigo");
        botonAbrir = new JButton("Abrir .py");

        JPanel panelBoton = new JPanel();
        panelBoton.add(botonAbrir);
        panelBoton.add(botonAnalizar);

        // TABLA TOKENS
        String[] columnas = {"Token", "Tipo", "Linea"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas,0);
        tablaTokens = new JTable(modeloTabla);

        JScrollPane scrollTabla = new JScrollPane(tablaTokens);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Tokens"));

        // 🌳 ARBOL
        arbol = new JTree(new DefaultMutableTreeNode("AST"));
        JScrollPane scrollArbol = new JScrollPane(arbol);
        scrollArbol.setBorder(BorderFactory.createTitledBorder("Árbol Sintáctico"));

        // AREA ERRORES
        areaErrores = new JTextArea();
        areaErrores.setEditable(false);

        JScrollPane scrollErrores = new JScrollPane(areaErrores);
        scrollErrores.setBorder(BorderFactory.createTitledBorder("Errores"));

        // PANEL CENTRAL (Tokens + Árbol)
        JSplitPane panelCentro = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT,
            scrollTabla,
            scrollArbol
        );
        panelCentro.setDividerLocation(450);

        // PANEL INFERIOR (Centro + Errores)
        JSplitPane panelInferior = new JSplitPane(
            JSplitPane.VERTICAL_SPLIT,
            panelCentro,
            scrollErrores
        );
        panelInferior.setDividerLocation(300);

        // AGREGAR COMPONENTES
        add(scrollCod,BorderLayout.NORTH);
        add(panelBoton,BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        scrollCod.setPreferredSize(new Dimension(800, 250));
    }

    // 🌳 NUEVO: actualizar árbol
    public void mostrarAST(DefaultMutableTreeNode raiz){
        arbol.setModel(new javax.swing.tree.DefaultTreeModel(raiz));
    }

    // GETTERS

    public JTextArea getAreaCodigo(){
        return areaCod;
    }

    public JButton getBotonAnalizar(){
        return botonAnalizar;
    }

    public JButton getBotonAbrir(){
        return botonAbrir;
    }

    public JTable getTablaTokens(){
        return tablaTokens;
    }

    public JTextArea getAreaErrores(){
        return areaErrores;
    }
}