package vista;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;

public class NumeroLinea extends JPanel {

    private JTextArea textArea;

    public NumeroLinea(JTextArea textArea){
        this.textArea = textArea;

        textArea.getDocument().addDocumentListener(new DocumentListener(){
            public void insertUpdate(DocumentEvent e){ repaint(); }
            public void removeUpdate(DocumentEvent e){ repaint(); }
            public void changedUpdate(DocumentEvent e){ repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        FontMetrics fm = g.getFontMetrics();
        int alturaLinea = fm.getHeight();

        int lineas = textArea.getLineCount();

        for(int i = 0; i < lineas; i++){
            String numero = String.valueOf(i + 1);
            int y = (i + 1) * alturaLinea - 5;
            g.drawString(numero, 5, y);
        }
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(30, textArea.getHeight());
    }
}