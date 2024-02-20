/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.io.Serializable;
import javax.ejb.Stateless;
import modelo.Produto;
import modelo.ProdutosDispensados;
import servicos.ServicoProduto;

/**
 *
 * @author paulo
 */
@Stateless
public class ManagerProduto implements Serializable {

    public boolean salvarProduto(ServicoProduto servico, Produto produto) {
        Produto temp = servico.findByProductName(produto.getNome());
        if (temp != null && temp.getLote().equals(produto.getLote())) {
            return false;
        } else {
            servico.salvar(produto);
            return true;
        }
    }

    public boolean editarProduto(ServicoProduto servico, Produto produto) {
        Produto temp = servico.findProductByID(produto.getId());
        if (temp != null) {
            try {
                servico.atualizar(produto);
                System.out.println("produto atualizado");
                return true;
            } catch (Exception ex) {
                System.out.println(ex);
                return false;
            }
        } else {
            System.out.println("Produto não encontrado");
            return false;
        }
    }

    public boolean editarProduto(ServicoProduto servico, ProdutosDispensados produto) {
        try {
            servico.atualiza(produto);
            System.out.println("produto atualizado");
            return true;
        } catch (Exception ex) {
            System.out.println(ex);
            return false;
        }
    }

    public void deletarProduto(ServicoProduto servico, Produto produto) {
        servico.deletarProduto(produto);
    }
}
