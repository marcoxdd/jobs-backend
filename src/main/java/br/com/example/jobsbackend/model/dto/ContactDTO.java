package br.com.example.jobsbackend.model.dto;

import br.com.example.jobsbackend.model.entity.Contact;

public class ContactDTO {

    private String name;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public Contact dtoToEntity(Contact contact) {
        if (contact != null) {
            contact.setName(this.name);
            contact.setContactInfo(this.contactInfo);
            return contact;
        }
        return new Contact(this.name, this.contactInfo);
    }
}
