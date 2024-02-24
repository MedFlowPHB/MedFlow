/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package interfaces;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import managers.ManagerHistorico;
import modelo.Paciente;
import modelo.Produto;
import modelo.ProdutosDispensados;
import modelo.Venda;
import servicos.ServicoPaciente;
import servicos.ServicoProduto;
import servicos.ServicoVenda;

/**
 *
 * @author paulo
 */
@Named
@ViewScoped
public class InterfaceHistorico implements Serializable {

    @EJB
    private ServicoVenda servicoVenda;
    @EJB
    private ServicoProduto servicoProduto;
    @EJB
    private ServicoPaciente servicoPaciente;
    private ManagerHistorico managerHistorico = new ManagerHistorico();
    private List<Produto> produtosVenda;
    private List<Venda> vendas;
    private Venda venda;
    private Paciente paciente;
    private List<Integer> quantidade;
    private List<ProdutosDispensados> produtosDispensados;
    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private List<String> datas;

    @PostConstruct
    public void init() {
        vendas = servicoVenda.findAll();
        venda = new Venda();
        produtosDispensados = servicoProduto.findProductsByID();
        datas = new ArrayList<>();
    }

    public void pesquisarVenda(Venda v) {
        managerHistorico.pesquisaVenda(servicoVenda, servicoProduto, servicoPaciente, v);
        venda = managerHistorico.getVenda();
        produtosVenda = managerHistorico.getProdutos();
        paciente = managerHistorico.getPaciente();
        quantidade = managerHistorico.getQuantidade();
        produtosVenda = substituirQuantidades(produtosVenda, quantidade);
    }

    public String recuperarLote(Long id) {
        for (int i = 0; i < produtosDispensados.size(); i++) {
            if (produtosDispensados.get(i).getProduto().getId() == id) {
                message(produtosDispensados.get(i).getLote(), "");
                return produtosDispensados.get(i).getLote();
            }
        }
        return null;
    }

    public Date recuperarData(Long id) {
        for (int i = 0; i < produtosDispensados.size(); i++) {
            if (produtosDispensados.get(i).getProduto().getId() == id) {
                message(String.valueOf(produtosDispensados.get(i).getValidade()), "");
                return produtosDispensados.get(i).getValidade();
            }
        }
        System.out.println("data nulo");
        return null;
    }

    public List<Produto> substituirQuantidades(List<Produto> produtosVenda, List<Integer> quantidades) {
        if (produtosVenda.size() != quantidades.size()) {
            throw new IllegalArgumentException("As listas não têm o mesmo tamanho");
        }

        List<Produto> novaListaProdutosVenda = new ArrayList<>();

        for (int i = 0; i < produtosVenda.size(); i++) {
            Produto produto = produtosVenda.get(i);
            int novaQuantidade = quantidades.get(i);

            Produto novoProduto = new Produto(); // Crie uma nova instância de Produto
            novoProduto.setId(produto.getId());
            novoProduto.setNome(produto.getNome());
            novoProduto.setQuantidade(novaQuantidade); // Substitui a quantidade
            novoProduto.setLote(produto.getLote());
            novoProduto.setValidade(produto.getValidade());

            novaListaProdutosVenda.add(novoProduto);
        }

        return novaListaProdutosVenda;
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

    public SimpleDateFormat getFormato() {
        return formato;
    }

    public void setFormato(SimpleDateFormat formato) {
        this.formato = formato;
    }

    public List<String> getDatas() {
        return datas;
    }

    public void setDatas(List<String> datas) {
        this.datas = datas;
    }

    public List<ProdutosDispensados> getProdutosDispensados() {
        return produtosDispensados;
    }

    public void setProdutosDispensados(List<ProdutosDispensados> produtosDispensados) {
        this.produtosDispensados = produtosDispensados;
    }

    public List<Integer> getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(List<Integer> quantidade) {
        this.quantidade = quantidade;
    }

    public ServicoVenda getServicoVenda() {
        return servicoVenda;
    }

    public void setServicoVenda(ServicoVenda servicoVenda) {
        this.servicoVenda = servicoVenda;
    }

    public ServicoProduto getServicoProduto() {
        return servicoProduto;
    }

    public void setServicoProduto(ServicoProduto servicoProduto) {
        this.servicoProduto = servicoProduto;
    }

    public ServicoPaciente getServicoPaciente() {
        return servicoPaciente;
    }

    public void setServicoPaciente(ServicoPaciente servicoPaciente) {
        this.servicoPaciente = servicoPaciente;
    }

    public ManagerHistorico getManagerHistorico() {
        return managerHistorico;
    }

    public void setManagerHistorico(ManagerHistorico managerHistorico) {
        this.managerHistorico = managerHistorico;
    }

    public List<Produto> getProdutosVenda() {
        return produtosVenda;
    }

    public void setProdutosVenda(List<Produto> produtosVenda) {
        this.produtosVenda = produtosVenda;
    }

    public List<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(List<Venda> vendas) {
        this.vendas = vendas;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

}
