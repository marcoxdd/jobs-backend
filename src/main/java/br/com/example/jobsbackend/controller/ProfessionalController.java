package br.com.example.jobsbackend.controller;

import br.com.example.jobsbackend.exception.NotFoundException;
import br.com.example.jobsbackend.model.dto.ProfessionalDTO;
import br.com.example.jobsbackend.model.entity.Professional;
import br.com.example.jobsbackend.service.ProfessionalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
        // If no search query is provided, return all professionals
        if (find == null || find.isEmpty()) {
            return professionalService.getAllProfessionals();
        }
        // Search professionals based on the query and fields
        return professionalService.searchProfessionals(find, fields);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professional> getProfessionalById(@PathVariable Long id) {
        // Get professional by ID and return it if found, or throw NotFoundException
        Professional professional = professionalService.getProfessionalById(id)
                .orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(professional);
    }

    @PostMapping
    public ResponseEntity<String> createProfessional(@RequestBody ProfessionalDTO professionalDTO) {
        // Create a new professional with the given data
        professionalService.createProfessional(professionalDTO);
        return ResponseEntity.ok("Professional created successfully.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateProfessional(@PathVariable Long id, @RequestBody ProfessionalDTO professionalDTO) {
        // Get professional by ID and update its data with the provided DTO
        Professional professional = professionalService.getProfessionalById(id)
                .orElseThrow(NotFoundException::new);
        professionalDTO.setCreateDate(new Date());
        Professional professionalUpdate = professionalDTO.dtoToEntity(professional);
        professionalService.updateProfessional(professionalUpdate);
        return ResponseEntity.ok("Professional updated successfully.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfessional(@PathVariable Long id) {
        // Check if professional exists, then delete it
        if (!professionalService.existsProfessional(id)) {
            throw new NotFoundException();
        }
        professionalService.deleteProfessional(id);
        return ResponseEntity.ok("Professional deleted successfully.");
    }
}

