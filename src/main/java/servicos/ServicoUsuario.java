package servicos;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import modelo.Usuario;

@Stateless
public class ServicoUsuario extends ServicoGenerico<Usuario> {

    public ServicoUsuario() {
        super(Usuario.class);
    }

    public Usuario buscarCPF(String cpf) {
        String jpql = "SELECT u FROM Usuario u WHERE u.cpf = :cpf";
        try {
            return getEm().createQuery(jpql, Usuario.class)
                    .setParameter("cpf", cpf)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
    
    public Usuario buscarID(Long id) {
        String jpql = "SELECT u FROM Usuario u WHERE u.id = :id";
        try {
            return getEm().createQuery(jpql, Usuario.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Usuario findByObject(Usuario usuario) {
        return getEm().find(Usuario.class, this).getUsuario();
    }

    public void deletarUsuario(Usuario usuario) {
        Object id = usuario.getId();
        Usuario user = getEm().getReference(Usuario.class, id);
        deletar(user);
    }
}
