package br.com.example.jobsbackend.controller;

import br.com.example.jobsbackend.exception.NotFoundException;
import br.com.example.jobsbackend.model.dto.ContactDTO;
import br.com.example.jobsbackend.model.entity.Contact;
import br.com.example.jobsbackend.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contacts")
public class ContactController {
    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping
    public List<Contact> getContacts(@RequestParam(required = false) String find,
                                     @RequestParam(required = false) List<String> fields) {
        if (find == null || find.isEmpty()) return contactService.getAllContacts();
        return contactService.searchContacts(find, fields);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        Optional<Contact> contact = contactService.getContactById(id);
        return contact.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createContact(@RequestBody ContactDTO contact) {
        contactService.createContact(contact.dtoToEntity(null));
        return ResponseEntity.ok("Contact created successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateContact(@PathVariable Long id, @RequestBody ContactDTO contact) {
        if (!contactService.existsContact(id)) return ResponseEntity.notFound().build();
        Contact contactEntity = contactService.getContactById(id)
                .orElseThrow(() -> new NotFoundException("Contact not found"));
        contactService.updateContact(contact.dtoToEntity(contactEntity));
        return ResponseEntity.ok("Contact updated successfully.");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable Long id) {
        if (!contactService.existsContact(id)) return ResponseEntity.notFound().build();
        contactService.deleteContact(id);
        return ResponseEntity.ok("Contact deleted successfully.");
    }
}
