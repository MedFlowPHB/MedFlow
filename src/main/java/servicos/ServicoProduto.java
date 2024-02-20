/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicos;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import modelo.Produto;
import modelo.ProdutosDispensados;

/**
 *
 * @author paulo
 */
@Stateless
public class ServicoProduto extends ServicoGenerico<Produto> {

    public ServicoProduto() {
        super(Produto.class);
    }

    public Produto findProductByID(Long id) {
        String jpql = "SELECT u FROM Produto u WHERE u.id = :id";
        try {
            return getEm().createQuery(jpql, Produto.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            System.out.println(ex);
            return null;
        }
    }

    public Produto findByProductName(String nome) {
        String jpql = "SELECT u FROM Produto u WHERE u.nome = :nome";
        try {
            return getEm().createQuery(jpql, Produto.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    public Produto findByProductLote(String lote) {
        String jpql = "SELECT u FROM Produto u WHERE u.lote = :lote";
        try {
            return getEm().createQuery(jpql, Produto.class)
                    .setParameter("lote", lote)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public List<ProdutosDispensados> findProductsByID() {
        String jpql = "SELECT U FROM ProdutosDispensados U";
        try {
            return getEm().createQuery(jpql, ProdutosDispensados.class)
                    .getResultList();
        } catch (NoResultException ex) {
            return null;
        }

    }

    public void atualiza(ProdutosDispensados produto) {
        try {
            getEm().merge(produto);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void deletarProduto(Produto produto) {
        Object id = produto.getId();
        Produto prod = getEm().getReference(Produto.class, id);
        deletar(prod);
    }
}
