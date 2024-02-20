/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import modelo.Microarea;
import servicos.ServicoArea;

/**
 *
 * @author mathe
 */
public class ManagerArea {

    public void salvarMicroarea(ServicoArea servicoArea, Microarea area) {
        servicoArea.salvar(area);
    }

    public Microarea pesquisarMicroarea(ServicoArea servicoArea, String nome) {
        Microarea tmp = servicoArea.buscarMicroarea(nome);
        if (tmp != null) {
            return tmp;
        }
        return null;
    }

    public boolean pesquisarMicroarea(ServicoArea servicoArea, Microarea area) {
        Microarea tmp = servicoArea.buscarMicroarea(area.getNome());
        return tmp != null;
    }

    public boolean editarMicroarea(ServicoArea servicoArea, Microarea area) {
        Microarea microareaSelecionada = servicoArea.buscarID(area.getId());
        if (microareaSelecionada != null) {
            try {
                servicoArea.atualizar(area);
                return true;
            } catch (Exception ex) {
                return false;
            }
        } else {
            return false;
        }
    }

    public void deletarMicroarea(ServicoArea servicoArea, Microarea area) {
        servicoArea.deletarArea(area);
    }
}
