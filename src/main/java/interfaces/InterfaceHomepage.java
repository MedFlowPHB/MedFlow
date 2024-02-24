/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import managers.ManagerPaciente;
import managers.ManagerProduto;
import modelo.Paciente;
import modelo.Produto;
import org.primefaces.PrimeFaces;
import servicos.ServicoPaciente;
import servicos.ServicoProduto;

/**
 *
 * @author paulo
 */
@Named
@ViewScoped
public class InterfaceHomepage implements Serializable {

    private ManagerProduto managerProduto = new ManagerProduto();
    private ManagerPaciente mPaciente = new ManagerPaciente();
    @Inject
    private ServicoProduto servicoProduto;
    @Inject
    private ServicoPaciente sPaciente;
    private Produto produto, produtoEditado;
    private List<Produto> produtos;
    private boolean formulario = true, salvareditar = true;
    private Paciente paciente;
    private Map<Long, Boolean> editadoMap = new HashMap<>();
    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private List<String> validadeFormatada;

    @PostConstruct
    public void init() {
        paciente = new Paciente();
        instanciarProdutos();
    }

    public void instanciarProdutos() {
        produtos = servicoProduto.findAll();
    }

    public void mostrarData() {
        System.out.println(produtos.get(0).getValidade());
    }

    public void novoProduto() {
        produto = new Produto();
    }

    public void salvarProduto() {
        if (managerProduto.salvarProduto(servicoProduto, produto)) {
            instanciarProdutos();
            message("Produto cadastrado com sucesso!", "");
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('manageProductDialog').hide();");
        } else {
            message("Este produto já está cadastrado!", "Tente novamente");
        }
    }

    public void formataData() {
        validadeFormatada = new ArrayList<>();
        for (Produto produto1 : produtos) {
            String dataFormatada = formato.format(produto1.getValidade());
            validadeFormatada.add(dataFormatada);
        }
    }

    public void editarProduto() {
        if (managerProduto.editarProduto(servicoProduto, produto)) {
            message("Produto editado", produto.getNome());
            produto = new Produto();
        } else {
            message("Erro", "Tente novamente");
        }
    }

    public String color(Date data) {
        Date hoje = new Date();
        if (data == null) {
            return "";
        }
        if (data.after(hoje)) {
            return "";
        } else {
            return "#b52b2b";
        }
    }

    public void checkSession() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);

        if (session == null || session.isNew()) {
            try {
                // Redirect to the session timeout page
                externalContext.redirect("sessionTimeout.xhtml");
            } catch (IOException e) {
                // Handle the IOException (e.g., log it)
            }
        }
    }

    public void deletarProduto() {
        managerProduto.deletarProduto(servicoProduto, produto);
        instanciarProdutos();
        message("Registro deletado com sucesso!", "");
    }

    public void novoPaciente() {
        paciente = new Paciente();
        formulario = true;
    }

    public void salvarPaciente() {
        if (mPaciente.salvarPaciente(sPaciente, paciente)) {
            message("Cadastro realizado com Sucesso!", "");
            paciente = new Paciente();
        } else {
            message("Este paciente já se encontra cadastrado no sistema!", "Utilize a pesquisa de pacientes para edita-lo.");
        }
    }

    public void editarPaciente() {
        if (mPaciente.editarPaciente(sPaciente, paciente)) {
            message("Dados editados com sucesso!", "");
            paciente = new Paciente();
        } else {
            message("Ocorreu um erro!", "Tente novamente");
        }
    }

    public void deletarPaciente() {

    }

    public void pesquisarPaciente() {
        paciente = mPaciente.pesquisarPaciente(sPaciente, paciente);
        if (paciente == null) {
            message("Esse Paciente não Existe", "Paciente não encontrado no banco de dados");
        } else {
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('cadastrar-Paciente-dialog').show();");
            formulario = !formulario;
        }
    }

    public void message(String titulo, String detalhes) {
        FacesMessage msg = new FacesMessage(titulo, detalhes);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public String navegacao(int valor) {
        switch (valor) {
            case 1:
                return "profile?faces-redirect=true";
            case 2:
                return "venda?faces-redirect=true";
            case 3:
                return "historico?faces-redirect=true";
            case 4:
                return "pacientes?faces-redirect=true";
            case 5:
                return "areas?faces-redirect=true";
            default:
                return "";
        }
    }

    public boolean isSalvareditar() {
        return salvareditar;
    }

    public void setSalvareditar(boolean salvareditar) {
        this.salvareditar = salvareditar;
    }

    public List<String> getValidadeFormatada() {
        return validadeFormatada;
    }

    public void setValidadeFormatada(List<String> validadeFormatada) {
        this.validadeFormatada = validadeFormatada;
    }

    public ManagerPaciente getmPaciente() {
        return mPaciente;
    }

    public void setmPaciente(ManagerPaciente mPaciente) {
        this.mPaciente = mPaciente;
    }

    public ServicoPaciente getsPaciente() {
        return sPaciente;
    }

    public void setsPaciente(ServicoPaciente sPaciente) {
        this.sPaciente = sPaciente;
    }

    public Map<Long, Boolean> getEditadoMap() {
        return editadoMap;
    }

    public void setEditadoMap(Map<Long, Boolean> editadoMap) {
        this.editadoMap = editadoMap;
    }

    public boolean isFormulario() {
        return formulario;
    }

    public void setFormulario(boolean formulario) {
        this.formulario = formulario;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Produto getProdutoEditado() {
        return produtoEditado;
    }

    public void setProdutoEditado(Produto produtoEditado) {
        this.produtoEditado = produtoEditado;
    }

    public ServicoProduto getServicoProduto() {
        return servicoProduto;
    }

    public boolean isEditado(Long id) {
        return editadoMap.getOrDefault(id, false);
    }

    public void setEditado(Long id, boolean editado) {
        this.editadoMap.put(id, editado);
    }

    public void setServicoProduto(ServicoProduto servicoProduto) {
        this.servicoProduto = servicoProduto;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public ManagerProduto getManagerProduto() {
        return managerProduto;
    }

    public void setManagerProduto(ManagerProduto managerProduto) {
        this.managerProduto = managerProduto;
    }
}
