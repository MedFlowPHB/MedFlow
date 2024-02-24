package servicos;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import modelo.Microarea;

@Stateless
public class ServicoArea extends ServicoGenerico<Microarea> {

    public ServicoArea() {
        super(Microarea.class);
    }

    public Microarea buscarMicroarea(String nome) {
        String jpql = "SELECT u FROM Microarea u WHERE u.nome = :nome";
        try {
            return getEm().createQuery(jpql, Microarea.class)
                    .setParameter("nome", nome)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Microarea buscarID(Long id) {
        String jpql = "SELECT u FROM Microarea u WHERE u.id = :id";
        try {
            return getEm().createQuery(jpql, Microarea.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Microarea findByObject(Microarea area) {
        return getEm().find(Microarea.class, this).getMicroarea();
    }

    public void deletarArea(Microarea area) {
        Object id = area.getId();
        Microarea loc = getEm().getReference(Microarea.class, id);
        deletar(loc);
    }
}
