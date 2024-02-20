package modelo;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author paulo
 */
@Entity
@Table(name = "Usuario")
public class Usuario implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_usuario", sequenceName = "seq_usuario", initialValue = 1)
    @GeneratedValue(generator = "seq_usuario", strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(length = 20, nullable = false)
    private String nome;
    @Column(length = 20, nullable = false)
    private String telefone;
    @Transient
    private String endereco;
    @Column(nullable = true)
    private String email;
    @Column(nullable = true)
    private String cpf;
    @Column(nullable = true)
    private String sexo;
    @Column(nullable = true)
    private String asct;

    public Usuario() {

    }

    public Usuario(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getAsct() {
        return asct;
    }

    public void setAsct(String asct) {
        this.asct = asct;
    }

    public Usuario getUsuario() {
        return this;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        hash = 41 * hash + Objects.hashCode(this.nome);
        hash = 41 * hash + Objects.hashCode(this.telefone);
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
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        if (!Objects.equals(this.telefone, other.telefone)) {
            return false;
        }
        return Objects.equals(this.id, other.id);
    }
}
