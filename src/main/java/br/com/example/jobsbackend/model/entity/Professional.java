package br.com.example.jobsbackend.model.entity;

import br.com.example.jobsbackend.model.enums.Position;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.STRING)
    private Position position;
    private LocalDate birthDate;
    private LocalDate createDate = LocalDate.now();
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    // Se for necessario a exclusão de contatos associodos a profissionais trocar CascadeType.ALL
    // por {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}
    @JoinTable(name = "professional_contacts",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "contacts_id"),
            uniqueConstraints = {})
    private List<Contact> contacts = new ArrayList<>();

    public Professional() {
    }

    public Professional(Long id, String name, Position position, LocalDate birthDate, List<Contact> contacts) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.birthDate = birthDate;
        this.createDate = LocalDate.now();
        this.contacts = contacts;
    }

    public Long getId() {
        return id;
    }

    public Position getPosition() {
        return position;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public String getName() {
        return name;
    }

    public void addContacts(List<Contact> contacts) {
        this.contacts.addAll(contacts);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }
}
