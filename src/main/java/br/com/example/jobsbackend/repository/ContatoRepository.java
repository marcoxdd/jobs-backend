package br.com.example.jobsbackend.repository;

import br.com.example.jobsbackend.model.entity.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContatoRepository extends JpaRepository<Contato, Long> {
    List<Contato> findByNomeContainingOrContatoContaining(String nome, String contato);
}
