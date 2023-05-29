package br.com.example.jobsbackend.repository;

import br.com.example.jobsbackend.model.entity.Professional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.persistence.metamodel.SingularAttribute;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public class ProfessionalRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Professional> findAll() {
        String jpql = "SELECT p FROM Professional p";
        return entityManager.createQuery(jpql, Professional.class).getResultList();
    }

    public Optional<Professional> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Professional.class, id));
    }

    public void save(Professional professional) {
        if (professional.getId() == null) {
            entityManager.persist(professional);
        } else {
            entityManager.merge(professional);
        }
    }

    public void deleteById(Long id) {
        Professional professional = entityManager.find(Professional.class, id);
        if (professional != null) {
            entityManager.remove(professional);
        }
    }

    public boolean existsById(Long id) {
        return entityManager.find(Professional.class, id) != null;
    }

    public List<Professional> search(String find, List<String> fields) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Professional> query = criteriaBuilder.createQuery(Professional.class);
        Root<Professional> root = query.from(Professional.class);

        List<Predicate> predicates = new ArrayList<>();

        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<Professional> entityType = metamodel.entity(Professional.class);
        Set<SingularAttribute<? super Professional, ?>> attributes = entityType.getSingularAttributes();

        for (SingularAttribute<? super Professional, ?> attribute : attributes) {
            convertAndAddPredicate(find, criteriaBuilder, root, predicates, attribute, fields);
        }

        query.where(criteriaBuilder.or(predicates.toArray(new Predicate[0])));

        return entityManager.createQuery(query).getResultList();
    }

    private void convertAndAddPredicate(String find, CriteriaBuilder criteriaBuilder, Root<Professional> root, List<Predicate> predicates, SingularAttribute<? super Professional, ?> attribute, List<String> fields) {
        if (fields == null || fields.contains(attribute.getName())) {
            if (attribute.getJavaType() == LocalDate.class) {
                LocalDate date = LocalDate.parse(find, DateTimeFormatter.ISO_LOCAL_DATE);
                predicates.add(criteriaBuilder.equal(root.get(attribute).as(LocalDate.class), date));
            } else {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(attribute).as(String.class)), "%" + find.toLowerCase() + "%"));
            }
        }
    }
}
