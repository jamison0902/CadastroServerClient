/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package cadastroclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.SwingUtilities;

/**
 *
 * @author jsq
 */
public class CadastroClientV2 {

    private static volatile boolean isRunning = true;

    public static void main(String[] args) {
        SaidaFrame saidaFrame = new SaidaFrame();
        try {
            // Socket apontando para localhost, na porta 4321.
            Socket socket = new Socket("localhost", 4321);

            // Encapsular os canais de entrada e saída do Socket.
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            //login e a senha.
            outputStream.writeObject("op1"); // Login
            outputStream.writeObject("op1"); // Senha

            // Leitura do teclado.
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

            // Instancia a janela de mensagem
            SwingUtilities.invokeLater(() -> {
                saidaFrame.setVisible(true);
            });

            //Instanciar a Thread.
            ThreadClient threadClient = new ThreadClient(inputStream, saidaFrame.getTextArea());
            threadClient.start();

            while (isRunning) {

                // Menu
                System.out.println("===============================================");
                System.out.println("               Menu de Opções                  ");
                System.out.println("===============================================");
                System.out.println("L - Listar");
                System.out.println("X - Finalizar");
                System.out.println("E - Entrada");
                System.out.println("S - Saída");
                System.out.print("Escolha uma opção: ");
                String comando = reader.readLine();

                // Listar Produtos
                if (comando.equalsIgnoreCase("L")) {                    
                    outputStream.writeObject("L");
                } else if (comando.equalsIgnoreCase("X")) {
                    isRunning = false; // Define a condição de saída da thread
                    socket.close(); // Fecha o socket
                    System.exit(0); // Golpe Fatal
                    break; // Sai do loop principal
                } else if (comando.equalsIgnoreCase("E") || comando.equalsIgnoreCase("S")) {   
                    
                    // Enviar o comando para o servidor.
                    outputStream.writeObject(comando);

                    // IdPessoa
                    System.out.print("Digite o ID da pessoa: ");
                    int pessoaId = Integer.parseInt(reader.readLine());
                    outputStream.writeObject(pessoaId);

                    // IdProduto
                    System.out.print("Digite o ID do produto: ");
                    int produtoId = Integer.parseInt(reader.readLine());
                    outputStream.writeObject(produtoId);

                    // quantidade
                    System.out.print("Digite a quantidade: ");
                    int quantidade = Integer.parseInt(reader.readLine());
                    outputStream.writeObject(quantidade);

                    // valorUnitario
                    System.out.print("Digite o valor unitário: ");
                    float valorUnitario = Float.parseFloat(reader.readLine());
                    outputStream.writeObject( valorUnitario);                    
                } else {
                    System.out.println("Comando inválido.");
                }
            }
        } catch (IOException e) {
        }
    }
}
