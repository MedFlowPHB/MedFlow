/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author paulo
 */
@Entity
@Table(name = "vendas")
public class Venda implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_vendas", sequenceName = "seq_vendas", initialValue = 1)
    @GeneratedValue(generator = "seq_vendas", strategy = GenerationType.SEQUENCE)
    private long id;
    @Temporal(TemporalType.DATE)
    private Date data;
    @OneToOne
    private Usuario usuario;
    @OneToOne
    private Paciente paciente;
    @Column(nullable = false)
    private String responsavel;
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<ProdutosDispensados> produtosDispensados = new ArrayList<>();

    public Venda() {
    }

    public Venda(Date data, Usuario usuario, Paciente paciente, String responsavel) {
        this.data = data;
        this.usuario = usuario;
        this.paciente = paciente;
        this.responsavel = responsavel;
    }

    public List<ProdutosDispensados> getProdutosDispensados() {
        return produtosDispensados;
    }

    public void setProdutosDispensados(List<ProdutosDispensados> produtosDispensados) {
        this.produtosDispensados = produtosDispensados;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 47 * hash + Objects.hashCode(this.data);
        hash = 47 * hash + Objects.hashCode(this.usuario);
        hash = 47 * hash + Objects.hashCode(this.paciente);
        hash = 47 * hash + Objects.hashCode(this.responsavel);
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
        final Venda other = (Venda) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.usuario, other.usuario)) {
            return false;
        }
        if (!Objects.equals(this.paciente, other.paciente)) {
            return false;
        }
        if (!Objects.equals(this.responsavel, other.responsavel)) {
            return false;
        }
        return Objects.equals(this.data, other.data);
    }

}
