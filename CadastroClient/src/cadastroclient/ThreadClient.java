/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroclient;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import javax.swing.JTextArea;
import model.Produto;

/**
 *
 * @author jsq
 */
public class ThreadClient extends Thread {

    private ObjectInputStream entrada;
    private JTextArea textArea;

    public ThreadClient(ObjectInputStream entrada, JTextArea textArea) {
        this.entrada = entrada;
        this.textArea = textArea;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Recebe os dados enviados pelo servidor
                Object obj = entrada.readObject();

                // Se o objeto for do tipo String, apenas adiciona ao JTextArea
                if (obj instanceof String) {
                    EventQueue.invokeLater(() -> {
                        textArea.append(obj.toString() + "\n");
                    });
                } else if (obj instanceof List) {
                    // Se o objeto for do tipo List, acrescenta o nome e a quantidade de cada produto ao JTextArea
                    textArea.append("=======================================\n");
                    textArea.append("                   Produtos            \n");
                    textArea.append("=======================================\n");
                    List<Produto> produtos = (List<Produto>) obj;
                    for (Produto produto : produtos) {
                        EventQueue.invokeLater(() -> {
                            textArea.append(produto.getNome() + " - " + produto.getQuantidade() + "\n");
                        });
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
            }
        }
    }
}
