/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import jakarta.persistence.*;

/**
 *
 * @author paulo
 */
@Entity
@Table(name = "Produto")
public class Produto implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_produto", sequenceName = "seq_produto", initialValue = 1)
    @GeneratedValue(generator = "seq_produto", strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = true)
    private int quantidade;
    @Transient
    private String preco;
    @Transient
    private String descricao;
    @Column(nullable = true)
    private String lote;
    @Temporal(TemporalType.DATE)
    private Date validade;

    public Produto() {
    }

    public Produto(String nome, int quantidade, String lote, Date validade, String descricao, String preco) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.lote = lote;
        this.validade = validade;
        this.descricao = descricao;
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "Produto{" + "id=" + id + ", nome=" + nome + ", quantidade=" + quantidade + ", preco=" + preco + ", descricao=" + descricao + ", lote=" + lote + ", validade=" + validade + '}';
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.nome);
        hash = 41 * hash + this.quantidade;
        hash = 41 * hash + Objects.hashCode(this.lote);
        hash = 41 * hash + Objects.hashCode(this.validade);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Produto other = (Produto) obj;
        if (this.quantidade != other.quantidade) {
            return false;
        }
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.lote, other.lote)) {
            return false;
        }
        return Objects.equals(this.validade, other.validade);
    }
}
