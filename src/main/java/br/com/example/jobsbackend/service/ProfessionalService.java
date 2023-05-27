package br.com.example.jobsbackend.service;

import br.com.example.jobsbackend.model.dto.ContactDTO;
import br.com.example.jobsbackend.model.dto.ProfessionalDTO;
import br.com.example.jobsbackend.model.entity.Contact;
import br.com.example.jobsbackend.model.entity.Professional;
import br.com.example.jobsbackend.repository.ContactRepository;
import br.com.example.jobsbackend.repository.ProfessionalRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final ContactRepository contactRepository;
    private final EntityManager entityManager;

    @Autowired
    public ProfessionalService(ProfessionalRepository professionalRepository, ContactRepository contactRepository, EntityManager entityManager) {
        this.professionalRepository = professionalRepository;
        this.contactRepository = contactRepository;
        this.entityManager = entityManager;
    }

    public List<Professional> getAllProfessionals() {
        // Retrieve all professionals from the repository
        return professionalRepository.findAll();
    }

    public Optional<Professional> getProfessionalById(Long id) {
        // Get professional by ID from the repository
        return professionalRepository.findById(id);
    }

    public void createProfessional(ProfessionalDTO professionalDTO) {
        // Create a new professional entity from the DTO and save it to the repository
        professionalDTO.setCreateDate(new Date());
        List<ContactDTO> contactDTOs = professionalDTO.getContacts();
        List<Contact> existingContacts = new ArrayList<>();

        Iterator<ContactDTO> iterator = contactDTOs.iterator();
        while (iterator.hasNext()) {
            ContactDTO contactDTO = iterator.next();
            Optional<Contact> existingContact = contactRepository.findByNameOrContactInfo(contactDTO.getName(), contactDTO.getContactInfo());
            if (existingContact.isPresent()) {
                existingContacts.add(existingContact.get());
                iterator.remove();
            }
        }

        Professional professional = professionalDTO.dtoToEntity(null);
        professional.addContacts(existingContacts);

        professionalRepository.save(professional);
    }

    public void updateProfessional(Professional professional) {
        // Update the professional in the repository
        professionalRepository.save(professional);
    }

    public void deleteProfessional(Long id) {
        // Delete the professional from the repository
        professionalRepository.deleteById(id);
    }

    public boolean existsProfessional(Long id) {
        // Check if professional exists in the repository
        return professionalRepository.existsById(id);
    }

    public List<Professional> searchProfessionals(String find, List<String> fields) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Professional> query = criteriaBuilder.createQuery(Professional.class);
        Root<Professional> root = query.from(Professional.class);

        List<Predicate> predicates = new ArrayList<>();

        if (fields == null || fields.isEmpty()) {
            // If the list of fields is empty, search in all entity fields
            Metamodel metamodel = entityManager.getMetamodel();
            EntityType<Professional> entityType = metamodel.entity(Professional.class);
            Set<SingularAttribute<? super Professional, ?>> attributes = entityType.getSingularAttributes();

            for (SingularAttribute<? super Professional, ?> attribute : attributes) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(attribute.getName()).as(String.class)),
                        "%" + find.toLowerCase() + "%"));
            }
        } else {
            // Search in the specified fields
            for (String field : fields) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(field).as(String.class)),
                        "%" + find.toLowerCase() + "%"));
            }
        }

        query.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }
}
