/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicos;

import javax.ejb.Stateless;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import modelo.Paciente;

/**
 *
 * @author paulo
 */
@Stateless
public class ServicoPaciente extends ServicoGenerico<Paciente> {

    public ServicoPaciente() {
        super(Paciente.class);
    }

    public Paciente findBySus(String sus) {
        String sql = "SELECT p FROM Paciente p WHERE p.sus = :sus";
        try {
            return getEm().createQuery(sql, Paciente.class)
                    .setParameter("sus", sus)
                    .getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public Paciente buscarID(Long id) {
        String jpql = "SELECT u FROM Paciente u WHERE u.id = :id";
        try {
            return getEm().createQuery(jpql, Paciente.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException ex) {
            message("erro", String.valueOf(ex));
            return null;
        }
    }

    public void message(String titulo, String detalhes) {
        FacesMessage msg = new FacesMessage(titulo, detalhes);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void deletarPaciente(Paciente paciente) {
        Long id = paciente.getId();
        System.out.println(paciente.getId() + "  " + paciente.getNome());
        Paciente pac = getEm().getReference(Paciente.class, id);
        deletar(pac);
    }
}
