/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicos;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author paulo
 * @param <T>
 */
public class ServicoGenerico<T> {

    private final Class<T> classe;
    @PersistenceContext
    private EntityManager em;

    public ServicoGenerico(Class<T> classe) {
        this.classe = classe;
    }

    public void salvar(T objeto) {
        em.persist(objeto);
    }

    public void atualizar(T objeto) {
        try {
            em.merge(objeto);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void deletar(T objeto) {
        em.remove(objeto);
    }

    public List<T> findAll() {
        return em.createQuery("SELECT O FROM " + classe.getName() + " O").getResultList();
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
}
