/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

/**
 *
 * @author paulo
 */
@Entity
@Table(name = "Paciente")
public class Paciente implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_paciente", sequenceName = "seq_paciente", initialValue = 1)
    @GeneratedValue(generator = "seq_paciente", strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String nome;
    @Temporal(TemporalType.DATE)
    private Date idade;
    @Column(nullable = true)
    private String sexo;
    private String telefone;
    private String qtdmembros;
    @Column(nullable = false)
    private String sus;
    @Column(nullable = true)
    private String patologia;
    @Column(nullable = true)
    private String area;

    public Paciente() {
    }

    public Paciente(String nome, Date idade, String sexo, String telefone) {
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.telefone = telefone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPatologia() {
        return patologia;
    }

    public void setPatologia(String patologia) {
        this.patologia = patologia;
    }

    public String getQtdmembros() {
        return qtdmembros;
    }

    public void setQtdmembros(String qtdmembros) {
        this.qtdmembros = qtdmembros;
    }

    public String getSus() {
        return sus;
    }

    public void setSus(String sus) {
        this.sus = sus;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
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

    public Date getIdade() {
        return idade;
    }

    public void setIdade(Date idade) {
        this.idade = idade;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
