/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import modelo.Paciente;
import modelo.Produto;
import modelo.ProdutosDispensados;
import modelo.Usuario;
import modelo.Venda;
import servicos.ServicoPaciente;
import servicos.ServicoProduto;
import servicos.ServicoVenda;

/**
 *
 * @author paulo
 */
@Stateless
public class ManagerVenda implements Serializable {

    private Venda vendaFinal;
    private List<Produto> produtosEstoque;
    private Paciente p;

    public Venda adicionaProduto(Venda venda, List<Produto> estoque, ProdutosDispensados produtoDispensado) {
        Produto clone = cloneProduto(produtoDispensado.getProduto());
        //caso em que a venda já foi iniciada e não está vazia de produtos
        if (venda != null && !venda.getProdutosDispensados().isEmpty()) {
            List<ProdutosDispensados> produtosDispensados = venda.getProdutosDispensados();

            for (ProdutosDispensados produto : produtosDispensados) {
                if (clone.getId() == produto.getProduto().getId()) {
                    int quantidadeVenda = produto.getQuantidade() + produtoDispensado.getQuantidade();
                    produto.setQuantidade(quantidadeVenda);
                    atualizaEstoque(produto.getProduto(), estoque, produtoDispensado.getQuantidade());
                    return venda;
                }
            }

            produtoDispensado.setQuantidade(produtoDispensado.getQuantidade());
            produtosDispensados.add(produtoDispensado);
            venda.setProdutosDispensados(produtosDispensados);
            atualizaEstoque(produtoDispensado.getProduto(), estoque, produtoDispensado.getQuantidade());
            return venda;
        }
        //caso em que a venda já foi iniciada mas está vazia de produtos
        if (venda != null && venda.getProdutosDispensados().isEmpty()) {
            venda.getProdutosDispensados().add(produtoDispensado);
            atualizaEstoque(produtoDispensado.getProduto(), estoque, produtoDispensado.getQuantidade());
            return venda;
        }
        //caso em que a vena ainda não foi iniciada tão qual a lista de produtos
        venda = new Venda();
        List<ProdutosDispensados> produtosDispensados = new ArrayList<>();
        produtoDispensado.setQuantidade(produtoDispensado.getQuantidade());
        produtosDispensados.add(produtoDispensado);
        venda.setProdutosDispensados(produtosDispensados);
        atualizaEstoque(produtoDispensado.getProduto(), estoque, produtoDispensado.getQuantidade());
        return venda;
    }

    public Venda retiraProduto(Venda venda, List<Produto> estoque, Produto produto, int quantidade) {
        //loop iterando pela lista de produtos dispensados na venda
        for (int i = 0; i < venda.getProdutosDispensados().size(); i++) {
            if (venda.getProdutosDispensados().get(i).getProduto().getId() == produto.getId()) {
                int quantidadeDispensada = venda.getProdutosDispensados().get(i).getQuantidade();
                quantidadeDispensada -= quantidade;
                if (quantidadeDispensada == 0) {
                    venda.getProdutosDispensados().remove(i);
                } else {
                    venda.getProdutosDispensados().get(i).setQuantidade(quantidadeDispensada);
                }
                int index = estoque.indexOf(produto);
                int quantidadeEstoque = estoque.get(index).getQuantidade();
                estoque.get(index).setQuantidade(quantidadeEstoque + quantidade);
                produtosEstoque = estoque;
                return venda;
            }
        }
        return venda;
    }

    public Paciente identificaCliente(ServicoPaciente servicoPaciente, Paciente paciente) {
        if (servicoPaciente.findBySus(paciente.getSus()) != null) {
            return p = servicoPaciente.findBySus(paciente.getSus());
        } else {
            return null;
        }
    }

    public boolean finalizaVenda(ServicoPaciente servicoPaciente, ServicoProduto servicoProduto,
            ServicoVenda servicoVenda, Venda venda, Paciente paciente, String responsavel, Usuario usuario) {
        vendaFinal = venda;
        if (identificaCliente(servicoPaciente, paciente) != null) {
            if (responsavel == null) {
                responsavel = usuario.getNome();
            }
            vendaFinal.setData(new Date());
            vendaFinal.setUsuario(usuario);
            vendaFinal.setResponsavel(responsavel);
            vendaFinal.setPaciente(p);
            servicoVenda.salvar(vendaFinal);
            atualizaBD(servicoProduto);
            return true;
        } else {
            return false;
        }
    }

    public void atualizaBD(ServicoProduto servicoProduto) {
        for (Produto produtos : produtosEstoque) {
            servicoProduto.atualizar(produtos);
        }
    }

    public Produto cloneProduto(Produto produto) {
        Produto produtoClone = new Produto();
        produtoClone.setNome(produto.getNome());
        produtoClone.setDescricao(produto.getDescricao());
        produtoClone.setId(produto.getId());
        produtoClone.setLote(produto.getLote());
        produtoClone.setQuantidade(produto.getQuantidade());
        produtoClone.setValidade(produto.getValidade());
        return produtoClone;
    }

    /*   public String precoTotal(String p, int q) {
        BigDecimal valorProduto = new BigDecimal(p.replaceAll(",", "."));
        BigDecimal preco = BigDecimal.ZERO;
        for (int x = 0; x < q; x++) {
            preco = preco.add(valorProduto);
        }
        return precoLabel = String.valueOf(preco.setScale(2, RoundingMode.HALF_UP)).replaceAll("\\.", ",");
    }*/
    public void atualizaEstoque(Produto produto, List<Produto> estoque, int quantidade) {
        int index = estoque.indexOf(produto);
        int quantidadeEstoque = estoque.get(index).getQuantidade();
        estoque.get(index).setQuantidade(quantidadeEstoque - quantidade);
        produtosEstoque = estoque;
    }

    public void message(String titulo, String detalhes) {
        FacesMessage msg = new FacesMessage(titulo, detalhes);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public boolean verificaQuantidade(Produto produto, int quantidade) {
        if (quantidade <= 0) {
            message("Você precisa inserir um valor positivo", "");
            return false;
        }
        if (produto.getQuantidade() >= quantidade && quantidade > 0) {
            return true;
        } else {
            message("Verifique a quantidade", "A quantidade inserida é maior que a do estoque!");
            return false;
        }
    }

    public boolean verificaQuantidadeVenda(ProdutosDispensados produtoDispensado, int quantidade) {
        if (produtoDispensado.getQuantidade() >= quantidade) {
            return true;
        } else {
            message("Verifique a quantidade", "A quantidade é maior que a do produto em dispensa");
            return false;
        }
    }

    public List<Produto> getProdutosEstoque() {
        return produtosEstoque;
    }

    public void setProdutosEstoque(List<Produto> produtosEstoque) {
        this.produtosEstoque = produtosEstoque;
    }

    public Venda getVendaFinal() {
        return vendaFinal;
    }

    public void setVendaFinal(Venda vendaFinal) {
        this.vendaFinal = vendaFinal;
    }

}
