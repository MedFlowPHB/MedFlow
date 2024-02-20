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
import managers.ManagerPaciente;
import managers.ManagerVenda;
import modelo.Paciente;
import modelo.Produto;
import modelo.ProdutosDispensados;
import modelo.Usuario;
import modelo.Venda;
import org.primefaces.PrimeFaces;
import servicos.ServicoPaciente;
import servicos.ServicoProduto;
import servicos.ServicoUsuario;
import servicos.ServicoVenda;

/**
 *
 * @author paulo
 */
@Named
@ViewScoped
public class InterfaceVenda implements Serializable {
    
    @EJB
    private ServicoProduto servicoProduto;
    @EJB
    private ServicoVenda servicoVenda;
    @EJB
    private ServicoPaciente servicoPaciente;
    @EJB
    private ManagerPaciente managerPaciente;
    @EJB
    private ServicoUsuario servicoUsuario;
    private List<Produto> estoque;
    private List<ProdutosDispensados> produtosDispensados;
    private ProdutosDispensados produtoDispensado;
    private Venda venda;
    private Produto produto;
    private Paciente paciente;
    private final ManagerVenda managerVenda = new ManagerVenda();
    private int quantidade;
    private boolean btFinalizar = false;
    private boolean formulario = true;
    private boolean tabelas = false;
    private String responsavel;
    private Usuario usuario;
    private boolean adicionarRetirar = true;
    
    @PostConstruct
    public void init() {
        estoque = servicoProduto.findAll();
        produto = new Produto();
        paciente = new Paciente();
        usuario = new Usuario();
    }
    
    public void adicionaProduto() {
        if (managerVenda.verificaQuantidade(produto, quantidade)) {
            produtoDispensado = new ProdutosDispensados(produto, quantidade);
            produtoDispensado.setValidade(produto.getValidade());
            produtoDispensado.setLote(produto.getLote());
            venda = managerVenda.adicionaProduto(venda, estoque, produtoDispensado);
            estoque = managerVenda.getProdutosEstoque();
            btFinalizar = true;
        }
    }
    
    public void retiraProduto() {
        if (managerVenda.verificaQuantidadeVenda(produtoDispensado, quantidade)) {
            venda = managerVenda.retiraProduto(venda, estoque, produtoDispensado.getProduto(), quantidade);
            if (venda.getProdutosDispensados().isEmpty()) {
                btFinalizar = false;
            }
            estoque = managerVenda.getProdutosEstoque();
        }
    }

    /*public ProdutosDispensados buscaProdutoDispensado(Produto produto) {
        for (int i = 0; i < produtosDispensados.size(); i++) {
            if (produtosDispensados.get(i).getId() == produto.getId()) {
                return produtosDispensados.get(i);
            }
        }
        return null;
    }*/
    public String finalizarVenda() throws InterruptedException {
        if (buscaUsuario()
                && managerVenda.finalizaVenda(servicoPaciente, servicoProduto, servicoVenda, venda, paciente, responsavel, usuario)) {
            message("Venda finalizada com sucesso!", "");
            Thread.sleep(2000);
            return "venda?faces-redirect=true";
        } else {
            if (!buscaUsuario()) {
                message("Usuário não encontrado!", "Este CPF não consta cadastrado no Banco de Dados");
                formulario = !formulario;
                return "";
            } else {
                formulario = !formulario;
                message("Paciente não encontrado!", "Este SUS não consta cadastrado no Banco de Dados");
                PrimeFaces current = PrimeFaces.current();
                current.executeScript("PF('cadastrar-Paciente-dialog').show();");
                return "";
            }
        }
    }
    
    public boolean buscaUsuario() {
        usuario = servicoUsuario.buscarCPF(usuario.getCpf());
        return usuario != null;
    }
    
    public void novoProduto() {
        produto = new Produto();
    }
    
    public void novoPaciente() {
        paciente = new Paciente();
    }
    
    public void salvarPaciente() {
        if (managerPaciente.salvarPaciente(servicoPaciente, paciente)) {
            message("Cadastro realizado com Sucesso!", "Paciente cadastrado com sucesso!");
            PrimeFaces current = PrimeFaces.current();
            current.executeScript("PF('confirmaVenda-dialog').show();");
            paciente = new Paciente();
        } else {
            message("Paciente já cadastrado!", "");
        }
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
    
    public void setServicoUsuario(ServicoUsuario servicoUsuario) {
        this.servicoUsuario = servicoUsuario;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public ManagerPaciente getManagerPaciente() {
        return managerPaciente;
    }
    
    public void setManagerPaciente(ManagerPaciente managerPaciente) {
        this.managerPaciente = managerPaciente;
    }
    
    public String getResponsavel() {
        return responsavel;
    }
    
    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }
    
    public ServicoProduto getServicoProduto() {
        return servicoProduto;
    }
    
    public void setServicoProduto(ServicoProduto servicoProduto) {
        this.servicoProduto = servicoProduto;
    }
    
    public ServicoVenda getServicoVenda() {
        return servicoVenda;
    }
    
    public void setServicoVenda(ServicoVenda servicoVenda) {
        this.servicoVenda = servicoVenda;
    }
    
    public ServicoPaciente getServicoPaciente() {
        return servicoPaciente;
    }
    
    public void setServicoPaciente(ServicoPaciente servicoPaciente) {
        this.servicoPaciente = servicoPaciente;
    }
    
    public List<Produto> getEstoque() {
        return estoque;
    }
    
    public void setEstoque(List<Produto> estoque) {
        this.estoque = estoque;
    }
    
    public int getQuantidade() {
        return quantidade;
    }
    
    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
    
    public boolean isFormulario() {
        return formulario;
    }
    
    public void setFormulario(boolean formulario) {
        this.formulario = formulario;
        if (formulario) {
            paciente = new Paciente();
        }
    }
    
    public List<ProdutosDispensados> getProdutosDispensados() {
        return produtosDispensados;
    }
    
    public void setProdutosDispensados(List<ProdutosDispensados> produtosDispensados) {
        this.produtosDispensados = produtosDispensados;
    }
    
    public boolean isAdicionarRetirar() {
        return adicionarRetirar;
    }
    
    public void setAdicionarRetirar(boolean adicionarRetirar) {
        this.adicionarRetirar = adicionarRetirar;
    }
    
    public ProdutosDispensados getProdutoDispensado() {
        return produtoDispensado;
    }
    
    public void setProdutoDispensado(ProdutosDispensados produtoDispensado) {
        this.produtoDispensado = produtoDispensado;
    }
    
    public boolean isTabelas() {
        return tabelas;
    }
    
    public void setTabelas(boolean tabelas) {
        this.tabelas = tabelas;
    }
    
    public boolean isBtFinalizar() {
        return btFinalizar;
    }
    
    public void setBtFinalizar(boolean btFinalizar) {
        this.btFinalizar = btFinalizar;
    }
    
    public Venda getVenda() {
        return venda;
    }
    
    public void setVenda(Venda venda) {
        this.venda = venda;
    }
    
    public Produto getProduto() {
        return produto;
    }
    
    public void setProduto(Produto produto) {
        this.produto = produto;
    }
    
    public Paciente getPaciente() {
        return paciente;
    }
    
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
    
}
