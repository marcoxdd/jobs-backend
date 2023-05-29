package br.com.example.jobsbackend.model.dto;

import br.com.example.jobsbackend.model.entity.Contact;
import br.com.example.jobsbackend.model.entity.Professional;
import br.com.example.jobsbackend.model.enums.Position;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProfessionalDTO {
    @NotEmpty(message = "Name is required.")
    private String name;
    @NotNull(message = "Position is required.")
    private Position position;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private List<ContactDTO> contacts;


    public ProfessionalDTO(String name, Position position, LocalDate birthDate, List<ContactDTO> contacts) {
        this.name = name;
        this.position = position;
        this.birthDate = birthDate;
        this.contacts = contacts;
    }

    public String getName() {
        return name;
    }

    public List<ContactDTO> getContacts() {
        return contacts;
    }

    public Position getPosition() {
        return position;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Professional toEntity(Professional professional) {
        Professional entity = (professional != null) ? professional : new Professional();

        entity.setName(this.name);
        entity.setPosition(this.position);
        entity.setBirthDate(this.birthDate);

        List<Contact> contacts = convertContactDTOsToEntities(this.contacts);
        entity.addContacts(contacts);

        return entity;
    }

    private List<Contact> convertContactDTOsToEntities(List<ContactDTO> contactDTOs) {
        List<Contact> contacts = new ArrayList<>();
        for (ContactDTO contactDTO : contactDTOs) {
            contacts.add(new Contact(null, contactDTO.getName(), contactDTO.getContactInfo()));
        }
        return contacts;
    }
}
