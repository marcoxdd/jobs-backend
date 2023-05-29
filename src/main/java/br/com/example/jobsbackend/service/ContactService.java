package br.com.example.jobsbackend.service;

import br.com.example.jobsbackend.model.dto.ContactDTO;
import br.com.example.jobsbackend.model.entity.Contact;
import br.com.example.jobsbackend.repository.ContactRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }


    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public void createContact(Contact contact) {
        contactRepository.save(contact);
    }

    public void updateContact(Contact contact) {
        contactRepository.save(contact);
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

    public boolean existsContact(Long id) {
        return contactRepository.existsById(id);
    }

    public List<Contact> searchContacts(String find, List<String> fields) {
      return contactRepository.search(find, fields);
    }

    public Optional<Contact> findByNameAndContactInfo(ContactDTO contactDTO){
        return contactRepository.findByNameAndContactInfo(contactDTO.getName(), contactDTO.getContactInfo());
    }

}
