/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroclient;

import javax.swing.JDialog;
import javax.swing.JTextArea;

/**
 *
 * @author jsq
 */
public class SaidaFrame extends JDialog {

    private JTextArea texto;

    public SaidaFrame() {
        setBounds(100, 100, 300, 400);
        setModal(false);

        texto = new JTextArea();
        add(texto);
    }

  
    public JTextArea getTextArea() {
        return this.texto;
    }
}
