/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author mathe
 */
@Entity
@Table(name = "Microarea")
public class Microarea implements Serializable {

    @Id
    @SequenceGenerator(name = "seq_microarea", sequenceName = "seq_microarea", initialValue = 1)
    @GeneratedValue(generator = "seq_microarea", strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(nullable = false)
    private String agente;
    @Column(nullable = true)
    private String nome;

    public Microarea() {

    }

    public Microarea getMicroarea() {
        return this;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAgente() {
        return agente;
    }

    public void setAgente(String agente) {
        this.agente = agente;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
