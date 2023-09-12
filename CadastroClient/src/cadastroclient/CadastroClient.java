/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cadastroclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import model.Produto;

/**
 *
 * @author jsq
 */
public class CadastroClient {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {        
        
        // Instanciar um Socket apontando para localhost, na porta 4321.
        Socket s1 = new Socket("localhost", 4321);
        
        // Encapsular os canais de entrada e saída.
        ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(s1.getInputStream());
        
        out.writeObject("op1"); // usuário
        out.writeObject("op1"); // senha
        out.writeObject("L"); // comando       
                  
        // Receber a lista de produtos
        List<Produto> lista = (List<Produto>) in.readObject();

        // Exibir nome dos produtos
        for (Produto s : lista) {
            System.out.println(s.getNome()+" - "+s.getQuantidade()+" Unidade(s)");
        }

        // Fechar a conexão.
        s1.close();
    }
    
}
