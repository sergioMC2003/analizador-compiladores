package controlador;

import modelo.*;
import vista.VentanaPrincipal;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;

public class ControladorCompilador {

    private VentanaPrincipal vista;

    public ControladorCompilador(VentanaPrincipal vista){
        this.vista = vista;

        // EVENTOS
        vista.getBotonAnalizar().addActionListener(e -> analizar());
        vista.getBotonAbrir().addActionListener(e -> abrirArchivo());
    }

    private void analizar(){

        String codigo = vista.getAreaCodigo().getText();

        try{

            // 🔥 ANALISIS LEXICO CON JFLEX
            Lexer lexer = new Lexer(new StringReader(codigo));

            List<Token> tokens = new ArrayList<>();
            Token token;

            while((token = lexer.yylex()) != null){
                tokens.add(token);
            }

            mostrarTokens(tokens);

            // 🚨 VALIDACION DE ERRORES LEXICOS
            for(Token t : tokens){
                if(t.getTipo() == TipoToken.ERROR){
                    vista.getAreaErrores().setText(
                        "Error léxico en línea " + t.getLinea() +
                        ": " + t.getLexema()
                    );
                    return;
                }
            }

            // 🔵 ANALISIS SINTACTICO
            Parser parser = new Parser(tokens);
            parser.analizar();

            // 🌳 OBTENER AST
            NodoAST raiz = parser.getAST();

            // 🌳 CONVERTIR A JTree
            DefaultMutableTreeNode raizTree = construirNodo(raiz);

            // 🌳 MOSTRAR EN LA VISTA
            vista.mostrarAST(raizTree);

            vista.getAreaErrores().setText("Análisis sintáctico correcto");

        }catch(Exception ex){

            vista.getAreaErrores().setText("Error: " + ex.getMessage());
        }
    }

    private void mostrarTokens(List<Token> tokens){

        DefaultTableModel modelo =
            (DefaultTableModel) vista.getTablaTokens().getModel();

        modelo.setRowCount(0);

        for(Token t : tokens){

            modelo.addRow(new Object[]{
                t.getLexema(),
                t.getTipo(),
                t.getLinea()
            });
        }
    }

    // 🌳 CONSTRUIR ARBOL PARA JTree
    private DefaultMutableTreeNode construirNodo(NodoAST nodo){

        DefaultMutableTreeNode treeNode =
            new DefaultMutableTreeNode(nodo.getNombre());

        for(NodoAST hijo : nodo.getHijos()){
            treeNode.add(construirNodo(hijo));
        }

        return treeNode;
    }

    // 📂 ABRIR ARCHIVO .PY
    private void abrirArchivo(){

        JFileChooser selector = new JFileChooser();

        FileNameExtensionFilter filtro =
            new FileNameExtensionFilter("Archivos Python (*.py)", "py");

        selector.setFileFilter(filtro);

        int opcion = selector.showOpenDialog(vista);

        if(opcion == JFileChooser.APPROVE_OPTION){

            File archivo = selector.getSelectedFile();

            try{

                String contenido =
                    new String(Files.readAllBytes(archivo.toPath()));

                vista.getAreaCodigo().setText(contenido);

                vista.getAreaErrores().setText(
                    "Archivo cargado: " + archivo.getName()
                );

            }catch(Exception e){

                vista.getAreaErrores().setText(
                    "Error al leer el archivo"
                );
            }
        }
    }
}