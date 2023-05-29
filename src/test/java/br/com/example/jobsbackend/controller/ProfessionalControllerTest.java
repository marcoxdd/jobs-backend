package br.com.example.jobsbackend.controller;

import br.com.example.jobsbackend.model.dto.ProfessionalDTO;
import br.com.example.jobsbackend.model.entity.Professional;
import br.com.example.jobsbackend.model.enums.Position;
import br.com.example.jobsbackend.service.ProfessionalService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfessionalController.class)
public class ProfessionalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfessionalService professionalService;


    private String createContentProfessional(String name, String birthDate) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("name", name);
        requestBody.put("position", "DESENVOLVEDOR");
        requestBody.put("birthDate", birthDate);
        requestBody.putArray("contacts");

        return objectMapper.writeValueAsString(requestBody);
    }

    @Test
    public void getProfessionals_ReturnsAllProfessionals_WhenNoSearchQueryProvided() throws Exception {
        List<Professional> professionals = Arrays.asList(
                new Professional(1L, "John Doe", Position.DESENVOLVEDOR, LocalDate.of(1990, 1, 1),
                         new ArrayList<>()),
                new Professional(2L, "Jane Smith", Position.SUPORTE, LocalDate.of(1985, 5, 10),
                         new ArrayList<>())
        );
        when(professionalService.getAllProfessionals()).thenReturn(professionals);

        mockMvc.perform(get("/professionals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].name", is("Jane Smith")));

        verify(professionalService).getAllProfessionals();
    }

    @Test
    public void getProfessionals_ReturnsFilteredProfessionals_WhenSearchQueryProvided() throws Exception {
        List<Professional> professionals = Arrays.asList(
                new Professional(1L, "John Doe", Position.DESENVOLVEDOR, LocalDate.of(1990, 1, 1),
                         new ArrayList<>()),
                new Professional(2L, "Jane Smith", Position.SUPORTE, LocalDate.of(1985, 5, 10),
                         new ArrayList<>())
        );
        when(professionalService.searchProfessionals("John", null)).thenReturn(List.of(professionals.get(0)));

        mockMvc.perform(get("/professionals?find=John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("John Doe")));

        verify(professionalService).searchProfessionals("John", null);
    }


    @Test
    public void getProfessionalById_ReturnsProfessional_WhenIdExists() throws Exception {
        Professional professional = new Professional(1L, "John Doe", Position.DESENVOLVEDOR, LocalDate.of(1990, 1, 1),
                new ArrayList<>());
        when(professionalService.getProfessionalById(1L)).thenReturn(Optional.of(professional));

        mockMvc.perform(get("/professionals/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John Doe")));

        verify(professionalService).getProfessionalById(1L);
    }

    @Test
    public void getProfessionalById_ReturnsNotFound_WhenIdDoesNotExist() throws Exception {
        when(professionalService.getProfessionalById(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/professionals/{id}", 999))
                .andExpect(status().isNotFound());

        verify(professionalService).getProfessionalById(999L);
    }

    @Test
    public void createProfessional_ReturnsSuccess_WhenValidProfessionalDTO() throws Exception {
        ProfessionalDTO professionalDTO = new ProfessionalDTO("John Doe", Position.DESENVOLVEDOR,
                LocalDate.of(1990, 1, 1), new ArrayList<>());

        mockMvc.perform(post("/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createContentProfessional("John Doe", "1990-01-01"))
                )
                .andExpect(status().isOk())
                .andExpect(content().string("Professional created successfully."));

        verify(professionalService).createProfessional(argThat(dto ->
                dto.getName().equals(professionalDTO.getName()) &&
                        dto.getPosition() == professionalDTO.getPosition() &&
                        dto.getContacts().equals(professionalDTO.getContacts())
        ));
    }

    @Test
    public void createProfessional_ReturnsBadRequest_WhenInvalidProfessionalDTO() throws Exception {
        mockMvc.perform(post("/professionals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createContentProfessional("", "1990-01-01"))
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation failed. Errors: name: Name is required."));

        verify(professionalService, never()).createProfessional(any(ProfessionalDTO.class));
    }

    @Test
    public void updateProfessional_ReturnsSuccess_WhenValidProfessionalDTOAndIdExists() throws Exception {
        ProfessionalDTO professionalDTO = new ProfessionalDTO("John Doe", Position.DESENVOLVEDOR,
                LocalDate.of(1990, 1, 1), new ArrayList<>());

        when(professionalService.existsProfessional(1L)).thenReturn(false);

        mockMvc.perform(put("/professionals/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"position\":\"DESENVOLVEDOR\",\"birthDate\":\"1990-01-01\",\"contacts\":[]}")
                )
                .andExpect(status().isNotFound());

        verify(professionalService, never()).updateProfessional(eq(1L), eq(professionalDTO));
    }


    @Test
    public void updateProfessional_ReturnsNotFound_WhenIdDoesNotExist() throws Exception {
        mockMvc.perform(put("/professionals/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"John Doe\",\"position\":\"DESENVOLVEDOR\",\"birthDate\":\"1990-01-01\",\"contacts\":[]}")
                )
                .andExpect(status().isNotFound());

        verify(professionalService).existsProfessional(999L);
    }

    @Test
    public void deleteProfessional_ReturnsSuccess_WhenIdExists() throws Exception {
        when(professionalService.existsProfessional(1L)).thenReturn(true);
        mockMvc.perform(delete("/professionals/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().string("Professional deleted successfully."));

        verify(professionalService).existsProfessional(1L);
        verify(professionalService).deleteProfessional(1L);
    }

    @Test
    public void deleteProfessional_ReturnsNotFound_WhenIdDoesNotExist() throws Exception {
        mockMvc.perform(delete("/professionals/{id}", 999))
                .andExpect(status().isNotFound());

        verify(professionalService).existsProfessional(999L);
    }

}
