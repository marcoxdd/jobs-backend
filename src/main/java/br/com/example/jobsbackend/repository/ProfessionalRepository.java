package br.com.example.jobsbackend.repository;

import br.com.example.jobsbackend.model.entity.Professional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProfessionalRepository extends JpaRepository<Professional, Long> {
}