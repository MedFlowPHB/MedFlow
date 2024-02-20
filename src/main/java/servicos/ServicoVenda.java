/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicos;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import modelo.Venda;

/**
 *
 * @author paulo
 */
@Stateless
public class ServicoVenda extends ServicoGenerico<Venda> {

    public ServicoVenda() {
        super(Venda.class);
    }

    public Venda findVenda(Long id) {
        String jpql = "SELECT u FROM Venda u WHERE u.id = :id";
        try {
            return getEm().createQuery(jpql, Venda.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public void deletarVenda(Venda venda) {
        Object id = venda.getId();
        Venda v = getEm().getReference(Venda.class, id);
        deletar(v);
    }
}
