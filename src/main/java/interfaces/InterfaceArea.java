/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import managers.ManagerArea;
import modelo.Microarea;
import servicos.ServicoArea;

/**
 *
 * @author paulo
 */
@Named
@ViewScoped
public class InterfaceArea implements Serializable {

    @EJB
    private ServicoArea servicoArea;
    private ManagerArea manager = new ManagerArea();
    private Microarea area;
    private List<Microarea> areas;
    private List<Microarea> outrasAreas;
    private String numero;
    private boolean salvareditar = true;

    @PostConstruct
    public void init() {
        instanciarAreas();
        outrasAreas();
        novaArea();
    }

    public void instanciarAreas() {
        areas = servicoArea.findAll();
    }

    public void novaArea() {
        area = new Microarea();
    }

    public void salvarArea() {
        if (manager.pesquisarMicroarea(servicoArea, area)) {
            message("Área já cadastrada!", "Verifique os dados.");
        } else {
            manager.salvarMicroarea(servicoArea, area);
            message("Salvo com sucesso!", "");
            instanciarAreas();
        }
    }

    public void salvarOutrasAreas(Microarea area) {
        if (!manager.pesquisarMicroarea(servicoArea, area)) {
            manager.salvarMicroarea(servicoArea, area);
        }
    }

    public void outrasAreas() {
        outrasAreas = new ArrayList<>();
        for (int i = 0; i < areas.size(); i++) {
            if (!areas.get(i).getNome().contains("Micro")) {
                outrasAreas.add(areas.get(i));
                System.out.println(areas.get(i).getNome());
            }
        }
    }
    
    public String outraAreaDialog(String nome) {
        if(nome.contains("Micro")) {
            return "PF('manageAreaDialog').show()";
        } else {
            return "PF('managerOutrasAreas').show()";
        }
    }

    public String outrasAreas(String nome) {
        if (nome.equals("Outro")) {
            return "pi pi-search";
        } else {
            return "pi pi-pencil";
        }
    }

    public void editarArea() {
        if (manager.editarMicroarea(servicoArea, area)) {
            message("Editado com sucesso!", "");
            novaArea();
        } else {
            message("Algum erro ocorreu!", "Tente novamente");
        }
    }

    public void deletarArea() {
        manager.deletarMicroarea(servicoArea, area);
        instanciarAreas();
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

    public Microarea getArea() {
        return area;
    }

    public void setArea(Microarea area) {
        this.area = area;
    }

    public List<Microarea> getAreas() {
        return areas;
    }

    public void setAreas(List<Microarea> areas) {
        this.areas = areas;
    }

    public ServicoArea getServicoArea() {
        return servicoArea;
    }

    public void setServicoArea(ServicoArea servicoArea) {
        this.servicoArea = servicoArea;
    }

    public ManagerArea getManager() {
        return manager;
    }

    public void setManager(ManagerArea manager) {
        this.manager = manager;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isSalvareditar() {
        return salvareditar;
    }

    public void setSalvareditar(boolean salvareditar) {
        this.salvareditar = salvareditar;
    }

    public List<Microarea> getOutrasAreas() {
        return outrasAreas;
    }

    public void setOutrasAreas(List<Microarea> outrasAreas) {
        this.outrasAreas = outrasAreas;
    }

}
