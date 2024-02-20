/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.io.Serializable;
import javax.ejb.Stateless;
import modelo.Paciente;
import servicos.ServicoPaciente;

/**
 *
 * @author paulo
 */
@Stateless
public class ManagerPaciente implements Serializable {

    public boolean salvarPaciente(ServicoPaciente servico, Paciente paciente) {
        if (servico.findBySus(paciente.getSus()) != null) {
            return false;
        } else {
            servico.salvar(paciente);
            return true;
        }
    }

    public boolean editarPaciente(ServicoPaciente servico, Paciente paciente) {
        Paciente pacienteClone = servico.buscarID(paciente.getId());
        if (pacienteClone != null) {
            servico.atualizar(paciente);
            return true;
        } else {
            System.out.println("Verifique o paciente que está tentando editar");
            return false;
        }
    }

    public Paciente pesquisarPaciente(ServicoPaciente servico, Paciente paciente) {
        return servico.findBySus(paciente.getSus());
    }

    public void deletarPaciente(ServicoPaciente servico, Paciente paciente) {
        servico.deletarPaciente(paciente);
    }

}
