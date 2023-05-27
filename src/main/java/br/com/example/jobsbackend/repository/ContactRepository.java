package br.com.example.jobsbackend.repository;

import br.com.example.jobsbackend.model.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByNameOrContactInfo(String name, String contact);
}
