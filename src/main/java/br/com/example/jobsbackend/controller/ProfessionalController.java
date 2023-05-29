package br.com.example.jobsbackend.controller;

import br.com.example.jobsbackend.exception.NotFoundException;
import br.com.example.jobsbackend.model.dto.ProfessionalDTO;
import br.com.example.jobsbackend.model.entity.Professional;
import br.com.example.jobsbackend.service.ProfessionalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;

    @Autowired
    public ProfessionalController(ProfessionalService professionalService) {
        this.professionalService = professionalService;
    }

    @GetMapping
    public List<Professional> getProfessionals(@RequestParam(required = false) String find,
                                               @RequestParam(required = false) List<String> fields) {
        if (find == null || find.isEmpty()) {
            return professionalService.getAllProfessionals();
        }
        return professionalService.searchProfessionals(find, fields);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professional> getProfessionalById(@PathVariable Long id) {
        Professional professional = professionalService.getProfessionalById(id)
                .orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(professional);
    }

    @PostMapping
    public ResponseEntity<String> createProfessional(@Valid @RequestBody ProfessionalDTO professionalDTO) {
        try {
            professionalService.createProfessional(professionalDTO);
            return ResponseEntity.ok("Professional created successfully.");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create professional.");
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> updateProfessional(@PathVariable Long id, @Valid @RequestBody ProfessionalDTO professionalDTO) {
        if (!professionalService.existsProfessional(id)) return ResponseEntity.notFound().build();
        professionalService.updateProfessional(id, professionalDTO);
        return ResponseEntity.ok("Professional updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfessional(@PathVariable Long id) {
        if (!professionalService.existsProfessional(id)) {
            return ResponseEntity.notFound().build();
        }
        professionalService.deleteProfessional(id);
        return ResponseEntity.ok("Professional deleted successfully.");
    }

}
