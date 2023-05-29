package br.com.example.jobsbackend.service;

import br.com.example.jobsbackend.repository.ContactRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import br.com.example.jobsbackend.repository.ProfessionalRepository;
import br.com.example.jobsbackend.model.entity.Professional;
import br.com.example.jobsbackend.model.dto.ProfessionalDTO;
import br.com.example.jobsbackend.model.dto.ContactDTO;
import br.com.example.jobsbackend.model.entity.Contact;
import br.com.example.jobsbackend.exception.NotFoundException;

@Service
@Transactional
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final ContactRepository contactRepository;

    public ProfessionalService(ProfessionalRepository professionalRepository, ContactRepository contactRepository) {
        this.professionalRepository = professionalRepository;
        this.contactRepository = contactRepository;
    }

    public List<Professional> getAllProfessionals() {
        return professionalRepository.findAll();
    }

    public Optional<Professional> getProfessionalById(Long id) {
        return professionalRepository.findById(id);
    }

    public void createProfessional(ProfessionalDTO professionalDTO) {
        List<Contact> existingContacts = filterExistingContacts(professionalDTO.getContacts());
        Professional professional = professionalDTO.toEntity(null);
        professional.addContacts(existingContacts);
        professionalRepository.save(professional);
    }

    public void updateProfessional(Long id, ProfessionalDTO professionalDTO) {
        Professional professional = professionalRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        List<Contact> existingContacts = filterExistingContacts(professionalDTO.getContacts());
        professional.getContacts().clear();
        professionalDTO.toEntity(professional);
        professional.addContacts(existingContacts);
        professionalRepository.save(professional);
    }

    private List<Contact> filterExistingContacts(List<ContactDTO> contactDTOs) {
        List<Contact> existingContacts = new ArrayList<>();

        Iterator<ContactDTO> iterator = contactDTOs.iterator();
        while (iterator.hasNext()) {
            ContactDTO contactDTO = iterator.next();
            Optional<Contact> existingContact = contactRepository.findByNameAndContactInfo(contactDTO.getName(), contactDTO.getContactInfo()).stream().findFirst();
            if (existingContact.isPresent()) {
                existingContacts.add(existingContact.get());
                iterator.remove();
            }
        }

        return existingContacts;
    }

    public void deleteProfessional(Long id) {
        professionalRepository.deleteById(id);
    }

    public boolean existsProfessional(Long id) {
        return professionalRepository.existsById(id);
    }

    public List<Professional> searchProfessionals(String find, List<String> fields) {
        return professionalRepository.search(find, fields);
    }
}
