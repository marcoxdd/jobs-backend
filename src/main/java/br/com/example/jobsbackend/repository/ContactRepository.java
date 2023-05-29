package br.com.example.jobsbackend.repository;

import br.com.example.jobsbackend.model.entity.Contact;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public class ContactRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Contact> findAll() {
        String jpql = "SELECT c FROM Contact c";
        return entityManager.createQuery(jpql, Contact.class).getResultList();
    }

    public Optional<Contact> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Contact.class, id));
    }

    public void save(Contact contact) {
        if (contact.getId() == null) {
            entityManager.persist(contact);
        } else {
            entityManager.merge(contact);
        }
    }

    public void deleteById(Long id) {
        Contact contact = entityManager.find(Contact.class, id);
        if (contact != null) {
            entityManager.remove(contact);
        }
    }

    public boolean existsById(Long id) {
        return entityManager.find(Contact.class, id) != null;
    }

    public List<Contact> search(String find, List<String> fields) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> query = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> root = query.from(Contact.class);

        List<Predicate> predicates = new ArrayList<>();

        if (fields == null || fields.isEmpty()) {
            Metamodel metamodel = entityManager.getMetamodel();
            EntityType<Contact> entityType = metamodel.entity(Contact.class);
            Set<SingularAttribute<? super Contact, ?>> attributes = entityType.getSingularAttributes();

            for (SingularAttribute<? super Contact, ?> attribute : attributes) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(attribute.getName()).as(String.class)), "%" + find.toLowerCase() + "%"));
            }
        } else {
            for (String field : fields) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(field).as(String.class)), "%" + find.toLowerCase() + "%"));
            }
        }

        query.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }

    public Optional<Contact> findByNameAndContactInfo(String name, String contact) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Contact> query = criteriaBuilder.createQuery(Contact.class);
        Root<Contact> root = query.from(Contact.class);

        query.select(root);
        query.where(criteriaBuilder.and(
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("name").as(String.class)), name.toLowerCase()),
                criteriaBuilder.equal(criteriaBuilder.lower(root.get("contactInfo").as(String.class)), contact.toLowerCase())
        ));

        return entityManager.createQuery(query).setMaxResults(1).getResultList().stream().findFirst();
    }
}
