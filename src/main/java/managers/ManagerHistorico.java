/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import modelo.Paciente;
import modelo.Produto;
import modelo.Venda;
import servicos.ServicoPaciente;
import servicos.ServicoProduto;
import servicos.ServicoVenda;

/**
 *
 * @author paulo
 */
@Stateless
public class ManagerHistorico implements Serializable {

    private List<Produto> produtos;
    private List<Integer> quantidade;
    private Venda venda;
    private Paciente paciente;

    public void pesquisaVenda(ServicoVenda sVenda, ServicoProduto sProduto, ServicoPaciente sPaciente, Venda v) {
        venda = sVenda.findVenda(v.getId());
        produtos = new ArrayList<>();
        quantidade = new ArrayList<>();
        for (int i = 0; i < venda.getProdutosDispensados().size(); i++) {
            produtos.add(venda.getProdutosDispensados().get(i).getProduto());
            quantidade.add(venda.getProdutosDispensados().get(i).getQuantidade());
        }
        paciente = sPaciente.findBySus(venda.getPaciente().getSus());
    }

    public List<Integer> getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(List<Integer> quantidade) {
        this.quantidade = quantidade;
    }

    public List<Produto> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

}
