package br.com.example.jobsbackend.model.entity;

import br.com.example.jobsbackend.model.enums.Cargo;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Profissional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @Enumerated(EnumType.ORDINAL)
    private Cargo cargo;
    private Date nascimento;
    private Date createData;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contato> contatos = new ArrayList<>();




    public void adicionarContato(Contato contato) {
        contatos.add(contato);
    }

    public void removerContato(Contato contato) {
        contatos.remove(contato);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public Date getCreateData() {
        return createData;
    }

    public List<Contato> getContatos() {
        return contatos;
    }
}
