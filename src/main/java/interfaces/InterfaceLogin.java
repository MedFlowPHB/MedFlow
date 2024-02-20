/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import managers.ManagerUsuario;
import modelo.Usuario;
import servicos.ServicoUsuario;
import ultilitario.Message;

/**
 *
 * @author paulo
 */
@Named
@ViewScoped
public class InterfaceLogin implements Serializable {

    @EJB
    private ServicoUsuario servico;
    private ManagerUsuario manager;
    private Usuario usuario;
    private boolean exibirFormulario = false;

    @PostConstruct
    public void init() {
        manager = new ManagerUsuario();
        usuario = new Usuario();
        System.out.println("usuario instaciado");
    }

    public void salvarUsuario() {
        Usuario tmp = manager.pesquisarUsuario(servico, usuario.getNome());
        if (tmp != null) {
            new Message().mensagemErro("Esse username já está em uso!", "Escolha outro nome de usuário!");
            return;
        }
        manager.salvarUsuario(servico, usuario);
        new Message().mensagemErro("Sucesso!", "Cadastro realizado com sucesso!");
        exibirFormulario = !exibirFormulario;
    }

    public String navegacao(int valor) {
        switch (valor) {
            case 1:
                return "homepage?faces-redirect=true";
            default:
                return "";
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public ServicoUsuario getServico() {
        return servico;
    }

    public void setServico(ServicoUsuario servico) {
        this.servico = servico;
    }

    public ManagerUsuario getManager() {
        return manager;
    }

    public void setManager(ManagerUsuario manager) {
        this.manager = manager;
    }

    public boolean isExibirFormulario() {
        return exibirFormulario;
    }

    public void setExibirFormulario(boolean exibirFormulario) {
        this.exibirFormulario = exibirFormulario;
    }

}
