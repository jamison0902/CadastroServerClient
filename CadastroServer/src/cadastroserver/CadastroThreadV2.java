/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cadastroserver;

import controller.MovimentoJpaController;
import controller.PessoaJpaController;
import controller.ProdutoJpaController;
import controller.UsuarioJpaController;
import controller.exceptions.NonexistentEntityException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Movimento;
import model.Pessoa;
import model.Produto;
import model.Usuario;

/**
 *
 * @author jsq
 */
public class CadastroThreadV2 extends Thread {

    private ProdutoJpaController ctrlProduto;
    private UsuarioJpaController ctrlUsuario;
    private Socket s1;
    private MovimentoJpaController ctrlMov;
    private PessoaJpaController ctrlPessoa;

    public CadastroThreadV2(ProdutoJpaController ctrlProduto, UsuarioJpaController ctrlUsuario, Socket s1, MovimentoJpaController ctrlMov, PessoaJpaController ctrlPessoa) {
        this.ctrlProduto = ctrlProduto;
        this.ctrlUsuario = ctrlUsuario;
        this.s1 = s1;
        this.ctrlMov = ctrlMov;
        this.ctrlPessoa = ctrlPessoa;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream in = new ObjectInputStream(s1.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(s1.getOutputStream());

            String login = (String) in.readObject();
            String senha = (String) in.readObject();

            Usuario usuario = ctrlUsuario.findUsuario(login, senha);
            if (usuario == null) {
                System.out.println("Usuário inválido");
                s1.close();
                return;
            }

            System.out.println("Usuário válido");

            while (true) {
                String comando = (String) in.readObject();

                switch (comando) {
                    case "E": {
                        // Entrada de produtos                            
                        int idPessoa = (int) in.readObject();
                        int idProduto = (int) in.readObject();
                        int quantidade = (int) in.readObject();
                        float valorUnitario = (float) in.readObject();

                        Pessoa pessoa = ctrlPessoa.findPessoa(idPessoa);
                        Produto produto = ctrlProduto.findProduto(idProduto);
                        
                        produto.setQuantidade(produto.getQuantidade() + quantidade);
                        ctrlProduto.edit(produto);

                        Movimento movimento = new Movimento();
                        movimento.setUsuario(usuario);
                        movimento.setTipo('E');
                        movimento.setPessoa(pessoa);
                        movimento.setProduto(produto);
                        movimento.setQuantidade(quantidade);
                        movimento.setValorUnitario(valorUnitario);
                        ctrlMov.create(movimento);                        
                        break;
                    }

                    case "S": {
                        // Saída de produtos                            
                        int idPessoa = (int) in.readObject();                        
                        int idProduto = (int) in.readObject();                        
                        int quantidade = (int) in.readObject();                         
                        float valorUnitario = (float) in.readObject();                        

                        Pessoa pessoa = ctrlPessoa.findPessoa(idPessoa);
                        Produto produto = ctrlProduto.findProduto(idProduto);
                        
                        produto.setQuantidade(produto.getQuantidade() - quantidade);
                        ctrlProduto.edit(produto);

                        Movimento movimento = new Movimento();
                        movimento.setUsuario(usuario);
                        movimento.setTipo('S');
                        movimento.setPessoa(pessoa);
                        movimento.setProduto(produto);
                        movimento.setQuantidade(quantidade);
                        movimento.setValorUnitario(valorUnitario);
                        ctrlMov.create(movimento);                        
                        break;
                    }
                    case "L": {
                        // Saída de produtos                            
                        List<Produto> produtos = ctrlProduto.findProdutoEntities();
                        out.writeObject(produtos);
                        break;
                    }
                    default:
                        System.out.println("Comando inválido");
                        break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
        } catch (NonexistentEntityException ex) {
            Logger.getLogger(CadastroThreadV2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CadastroThreadV2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
