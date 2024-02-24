/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.io.Serializable;
import jakarta.ejb.Stateless;
import modelo.Usuario;
import servicos.ServicoUsuario;

/**
 *
 * @author paulo
 */
@Stateless
public class ManagerUsuario implements Serializable {

    public void salvarUsuario(ServicoUsuario servico, Usuario usuario) {
        servico.salvar(usuario);
    }

    public Usuario pesquisarUsuario(ServicoUsuario servico, String cpf) {
        Usuario tmp = servico.buscarCPF(cpf);
        if (tmp != null) {
            return tmp;
        }
        return null;
    }

    public boolean pesquisarUsuario(ServicoUsuario servico, Usuario usuario) {
        Usuario tmp = servico.findByObject(usuario);
        return tmp != null;
    }

    public boolean editarUsuario(ServicoUsuario servico, Usuario usuario) {
        Usuario usuarioselecionado = servico.buscarID(usuario.getId());
        if (usuarioselecionado != null) {
            try {
                servico.atualizar(usuario);
                return true;
            } catch (Exception ex) {
                return false;
            }
        } else {
            return false;
        }
    }

    public void deletarUsuario(ServicoUsuario servico, Usuario usuario) {
        servico.deletarUsuario(usuario);
    }
}
