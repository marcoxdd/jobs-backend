package br.com.example.jobsbackend.controller;

import br.com.example.jobsbackend.exception.NotFoundException;
import br.com.example.jobsbackend.model.dto.ContatoDTO;
import br.com.example.jobsbackend.model.entity.Contato;
import br.com.example.jobsbackend.service.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contacts")
public class ContatoController {
    private final ContatoService contatoService;

    @Autowired
    public ContatoController(ContatoService contatoService) {
        this.contatoService = contatoService;
    }

    @GetMapping
    public List<Contato> getContacts(@RequestParam(required = false) String find,
                                     @RequestParam(required = false) List<String> fields) {
        if (find == null || fields.isEmpty()) return contatoService.getAllContacts();
        return contatoService.searchContactsByFields(find, fields);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contato> getContactById(@PathVariable Long id) {
        Optional<Contato> contato = contatoService.getContactById(id);
        return contato.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<String> createContact(@RequestBody ContatoDTO contato) {
        contatoService.createContact(contato.dtoToEntity(null));
        return ResponseEntity.ok("Contato cadastrado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateContact(@PathVariable Long id, @RequestBody ContatoDTO contato) {
        if (!contatoService.existsContact(id)) return ResponseEntity.notFound().build();
        Contato contatoEntity = contatoService.getContactById(id)
                .orElseThrow(() -> new NotFoundException("Contato não encontrado"));
        contatoService.updateContact(contato.dtoToEntity(contatoEntity));
        return ResponseEntity.ok("Contato alterado com sucesso.");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable Long id) {
        if (!contatoService.existsContact(id)) return ResponseEntity.notFound().build();
        contatoService.deleteContact(id);
        return ResponseEntity.ok("Contato excluído com sucesso.");
    }
}