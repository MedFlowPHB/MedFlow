/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import managers.ManagerPaciente;
import modelo.Paciente;
import org.primefaces.PrimeFaces;
import servicos.ServicoPaciente;

/**
 *
 * @author paulo
 */
@Named
@ViewScoped
public class InterfacePaciente implements Serializable {

    @EJB
    private ServicoPaciente servicoPaciente;
    private ManagerPaciente manager = new ManagerPaciente();
    private Paciente paciente;
    private List<Paciente> pacientes;
    private String numero;
    private boolean salvareditar = true, formulario = true, firstTime = true;

    @PostConstruct
    public void init() {
        instanciaPacientes();
    }

    public void instanciaPacientes() {
        pacientes = servicoPaciente.findAll();
        novoPaciente();
    }

    public void novoPaciente() {
        paciente = new Paciente();
    }

    public int calcularIdade(Date dataNascimento) {
        Calendar hoje = Calendar.getInstance();
        Calendar nascimento = Calendar.getInstance();
        nascimento.setTime(dataNascimento);

        int idade = hoje.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR);
        if (hoje.get(Calendar.MONTH) < nascimento.get(Calendar.MONTH)
                || (hoje.get(Calendar.MONTH) == nascimento.get(Calendar.MONTH) && hoje.get(Calendar.DAY_OF_MONTH) < nascimento.get(Calendar.DAY_OF_MONTH))) {
            idade--;
        }
        return idade;
    }

    public void salvarPaciente() {
        if (manager.pesquisarPaciente(servicoPaciente, paciente) != null) {
            message("Paciente já cadastrado no sistema!", "Verifique os dados.");
        } else if (paciente.getArea().equals("Outro") && firstTime) {
            PrimeFaces.current().executeScript("PF('outra-area').show();");
            firstTime = !firstTime;
        } else {
            manager.salvarPaciente(servicoPaciente, paciente);
            message("Paciente cadastrado com sucesso!", "");
            instanciaPacientes();
            PrimeFaces.current().executeScript("PF('cadastrar-Paciente-dialog').hide();");
            PrimeFaces.current().executeScript("PF('outra-area').hide();");
        }
    }

    public void editarPaciente() {
        if (manager.editarPaciente(servicoPaciente, paciente)) {
            message("Paciente editado com sucesso", "");
            paciente = new Paciente();
        } else {
            message("Paciente não encontrado", "Tente novamente");
        }
    }

    public void deletarPaciente() {
        manager.deletarPaciente(servicoPaciente, paciente);
        instanciaPacientes();
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

    public ServicoPaciente getServicoPaciente() {
        return servicoPaciente;
    }

    public void setServicoPaciente(ServicoPaciente servicopaciente) {
        this.servicoPaciente = servicopaciente;
    }

    public ManagerPaciente getManager() {
        return manager;
    }

    public void setManager(ManagerPaciente manager) {
        this.manager = manager;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public List<Paciente> getPacientes() {
        return pacientes;
    }

    public void setPacientes(List<Paciente> pacientes) {
        this.pacientes = pacientes;
    }

    public boolean isSalvareditar() {
        return salvareditar;
    }

    public void setSalvareditar(boolean salvareditar) {
        this.salvareditar = salvareditar;
    }

    public boolean isFormulario() {
        return formulario;
    }

    public void setFormulario(boolean formulario) {
        this.formulario = formulario;
    }

}
