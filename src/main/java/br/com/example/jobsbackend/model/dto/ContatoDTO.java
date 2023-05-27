package br.com.example.jobsbackend.model.dto;

import br.com.example.jobsbackend.model.entity.Contato;

public class ContatoDTO {

    private String nome;
    private String contato;

    public ContatoDTO() {
    }

    public ContatoDTO(String nome, String contato) {
        this.nome = nome;
        this.contato = contato;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public Contato dtoToEntity(Contato contato){
        if (contato != null){
            contato.setNome(this.nome);
            contato.setContato(this.contato);
            return contato;
        }
        return new Contato(this.nome, this.contato);
    }
}
