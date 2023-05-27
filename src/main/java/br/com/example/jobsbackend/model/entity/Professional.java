package br.com.example.jobsbackend.model.entity;

import br.com.example.jobsbackend.model.enums.Position;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Professional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Enumerated(EnumType.ORDINAL)
    private Position position;
    private Date birthDate;
    private Date createDate;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "professional_contacts",
            joinColumns = @JoinColumn(name = "professional_id"),
            inverseJoinColumns = @JoinColumn(name = "contacts_id"),
            uniqueConstraints = {})
    private List<Contact> contacts = new ArrayList<>();

    protected Professional() {
    }

    public Professional(Long id, String name, Position position, Date birthDate, Date createDate, List<Contact> contacts) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.birthDate = birthDate;
        this.createDate = createDate;
        this.contacts = contacts;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void addContacts(List<Contact> contacts) {
        this.contacts.addAll(contacts);
    }
}
