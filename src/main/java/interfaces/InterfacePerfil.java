/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import managers.ManagerUsuario;
import modelo.Usuario;
import servicos.ServicoUsuario;

/**
 *
 * @author paulo
 */
@Named
@ViewScoped
public class InterfacePerfil implements Serializable {

    @EJB
    private ServicoUsuario servicoUsuario;
    private ManagerUsuario manager = new ManagerUsuario();
    private Usuario usuario;
    private List<Usuario> usuarios;
    private String numero;
    private boolean salvareditar = true;

    @PostConstruct
    public void init() {
        instanciaUsuarios();
    }

    public void instanciaUsuarios() {
        usuarios = servicoUsuario.findAll();
        novoUsuario();
    }

    public void novoUsuario() {
        usuario = new Usuario();
    }

    public void salvarUsuario() {
        if (manager.pesquisarUsuario(servicoUsuario, usuario.getCpf()) != null) {
            message("CPF já cadastrado em outro usuário!", "Verifique os dados.");
        } else {
            usuario.setEndereco(usuario.getEndereco() + ", " + numero);
            manager.salvarUsuario(servicoUsuario, usuario);
            message("Usuário cadastrado com sucesso!", "");
            instanciaUsuarios();
        }
    }

    public void editarUsuario() {
        if (manager.editarUsuario(servicoUsuario, usuario)) {
            message("Usuário editado com sucesso", "");
            usuario = new Usuario();
        } else {
            message("Usuário não encontrado", "Tente novamente");
        }
    }

    public void deletarUsuario() {
        manager.deletarUsuario(servicoUsuario, usuario);
        instanciaUsuarios();
        message("Registro deletado com sucesso!", "");
    }

    public void message(String titulo, String detalhes) {
        FacesMessage msg = new FacesMessage(titulo, detalhes);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String navegacao(int valor) {
        switch (valor) {
            case 1:
                return "homepage?faces-redirect=true";
            case 2:
                return "";
            default:
                return "";
        }
    }

    public ServicoUsuario getServicoUsuario() {
        return servicoUsuario;
    }

    public void setServicoUsuario(ServicoUsuario servicousuario) {
        this.servicoUsuario = servicousuario;
    }

    public ManagerUsuario getManager() {
        return manager;
    }

    public void setManager(ManagerUsuario manager) {
        this.manager = manager;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public boolean isSalvareditar() {
        return salvareditar;
    }

    public void setSalvareditar(boolean salvareditar) {
        this.salvareditar = salvareditar;
    }

}
