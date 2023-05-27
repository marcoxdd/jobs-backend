package br.com.example.jobsbackend.model.dto;

import br.com.example.jobsbackend.model.entity.Contact;
import br.com.example.jobsbackend.model.entity.Professional;
import br.com.example.jobsbackend.model.enums.Position;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfessionalDTO {

    private String name;
    private Position position;
    private Date birthdate;
    private Date createDate;
    private List<ContactDTO> contacts;

    public ProfessionalDTO() {
        this.contacts = new ArrayList<>();
    }

    public ProfessionalDTO(String name, Position position, Date birthdate, Date createDate, List<ContactDTO> contacts) {
        this.name = name;
        this.position = position;
        this.birthdate = birthdate;
        this.createDate = createDate;
        this.contacts = contacts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<ContactDTO> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactDTO> contacts) {
        this.contacts = contacts;
    }

    public Professional dtoToEntity(Professional professional) {
        if (professional != null) {
            // Convert the DTO to an entity when updating an existing professional
            return new Professional(professional.getId(), this.name, this.position, this.birthdate, this.createDate, convertContactDTOsToEntities(this.contacts));
        }
        // Convert the DTO to an entity when creating a new professional
        return new Professional(null, this.name, this.position, this.birthdate, this.createDate, convertContactDTOsToEntities(this.contacts));
    }

    private List<Contact> convertContactDTOsToEntities(List<ContactDTO> contactDTOs) {
        List<Contact> contacts = new ArrayList<>();
        for (ContactDTO contactDTO : contactDTOs) {
            contacts.add(new Contact(contactDTO.getName(), contactDTO.getContactInfo()));
        }
        return contacts;
    }
}
