package br.com.example.jobsbackend.service;

import br.com.example.jobsbackend.model.entity.Contact;
import br.com.example.jobsbackend.repository.ContactRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private EntityManager entityManager;

    @Autowired
    public ContactService(ContactRepository contactRepository, EntityManager entityManager) {
        this.contactRepository = contactRepository;
        this.entityManager = entityManager;
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
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> query = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> root = query.from(Contact.class);

        List<Predicate> predicates = new ArrayList<>();

        if (fields.isEmpty()) {
            // If the list of fields is empty, search in all fields of the entity
            Metamodel metamodel = entityManager.getMetamodel();
            EntityType<Contact> entityType = metamodel.entity(Contact.class);
            Set<SingularAttribute<? super Contact, ?>> attributes = entityType.getSingularAttributes();

            for (SingularAttribute<? super Contact, ?> attribute : attributes) {
                predicates.add(criteriaBuilder.like(root.get(attribute.getName()).as(String.class), "%" + find.toLowerCase() + "%"));
            }
        } else {
            // Search in the specified fields
            for (String field : fields) {
                predicates.add(criteriaBuilder.like(root.get(field).as(String.class), "%" + find.toLowerCase() + "%"));
            }
        }

        query.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }

}
