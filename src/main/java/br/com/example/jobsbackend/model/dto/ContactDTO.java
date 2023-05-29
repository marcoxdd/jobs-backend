package br.com.example.jobsbackend.model.dto;

import br.com.example.jobsbackend.model.entity.Contact;
import jakarta.validation.constraints.NotEmpty;

public class ContactDTO {

    @NotEmpty
    private String name;
    @NotEmpty
    private String contactInfo;

    public ContactDTO() {
    }

    public ContactDTO(String name, String contactInfo) {
        this.name = name;
        this.contactInfo = contactInfo;
    }

    public String getName() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo;
    }


    public Contact toEntity(Contact contact) {
        if (contact != null) {
            contact.setName(this.name);
            contact.setContactInfo(this.contactInfo);
            return contact;
        }
        return new Contact(null, this.name, this.contactInfo);
    }
}
