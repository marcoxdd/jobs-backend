package br.com.example.jobsbackend.service;

import br.com.example.jobsbackend.model.entity.Contato;
import br.com.example.jobsbackend.repository.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

@Service
public class ContatoService {

    private final ContatoRepository contatoRepository;

    @Autowired
    public ContatoService(ContatoRepository contatoRepository) {
        this.contatoRepository = contatoRepository;
    }

    public List<Contato> searchContacts(String query) {
        return contatoRepository.findByNomeContainingOrContatoContaining(query, query);
    }

    public List<Contato> getAllContacts() {
        return contatoRepository.findAll();
    }

    public Optional<Contato> getContactById(Long id) {
        return contatoRepository.findById(id);
    }

    public void createContact(Contato contato) {
        contatoRepository.save(contato);
    }

    public void updateContact(Contato contato) {
        contatoRepository.save(contato);
    }

    public void deleteContact(Long id) {
        contatoRepository.deleteById(id);
    }

    public boolean existsContact(Long id) {
        return contatoRepository.existsById(id);
    }

    public List<Contato> searchContactsByFields(String query, List<String> fields) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

        Contato contatoExample = new Contato();
        for (String field : fields) {
            try {
                Field declaredField = Contato.class.getDeclaredField(field);
                declaredField.setAccessible(true);
                declaredField.set(contatoExample, query);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // Tratar exceção ou ignorar, conforme necessário
            }
        }

        Example<Contato> example = Example.of(contatoExample, matcher);
        return contatoRepository.findAll(example);
    }
}
