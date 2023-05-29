package br.com.example.jobsbackend.controller;

import br.com.example.jobsbackend.model.dto.ContactDTO;
import br.com.example.jobsbackend.model.entity.Contact;
import br.com.example.jobsbackend.service.ContactService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ContactController.class)
@AutoConfigureMockMvc
public class ContactControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContactService contactService;


    @Test
    void testGetContacts_NoSearch_ReturnAllContacts() throws Exception {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1L, "John", "john@gmail.com"));
        contacts.add(new Contact(2L, "Jane", "jane@gmail.com"));

        when(contactService.getAllContacts()).thenReturn(contacts);

        mockMvc.perform(get("/contacts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$[0].contactInfo", is("john@gmail.com")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Jane")))
                .andExpect(jsonPath("$[1].contactInfo", is("jane@gmail.com")));

        verify(contactService, times(1)).getAllContacts();
    }

    @Test
    void testGetContacts_WithSearch_ReturnMatchingContacts() throws Exception {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1L, "John", "john@gmail.com"));

        when(contactService.searchContacts("John", null)).thenReturn(contacts);

        mockMvc.perform(get("/contacts?find=John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$[0].contactInfo", is("john@gmail.com")));

        verify(contactService, times(1)).searchContacts("John", null);
    }

    @Test
    void testGetContacts_WithSearchAndFields_ReturnMatchingContacts() throws Exception {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(1L, "John", "john@gmail.com"));

        List<String> fields = Arrays.asList("name", "email");
        when(contactService.searchContacts("John", fields)).thenReturn(contacts);

        mockMvc.perform(get("/contacts?find=John&fields=name,email"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("John")))
                .andExpect(jsonPath("$[0].contactInfo", is("john@gmail.com")));

        verify(contactService, times(1)).searchContacts("John", fields);
    }

    @Test
    void testGetContactById_ExistingContact_ReturnContact() throws Exception {
        Long id = 1L;
        Contact contact = new Contact(id, "John", "Doe");

        when(contactService.getContactById(id)).thenReturn(Optional.of(contact));

        mockMvc.perform(get("/contacts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.contactInfo", is("Doe")));

        verify(contactService, times(1)).getContactById(id);
    }

    @Test
    void testGetContactById_NonExistingContact_ReturnNotFound() throws Exception {
        Long id = 1L;

        when(contactService.getContactById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/contacts/{id}", id))
                .andExpect(status().isNotFound());

        verify(contactService, times(1)).getContactById(id);
    }

    @Test
    void testCreateContact_InvalidContact_ReturnBadRequest() throws Exception {
        ContactDTO contactDTO = new ContactDTO("John", "");

        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contactDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Validation failed. Errors: contactInfo: must not be empty"));

        verify(contactService, never()).createContact(ArgumentMatchers.any(Contact.class));

    }

    @Test
    public void testCreateContact_ContactAlreadyExists_ReturnsBadRequest() throws Exception {
        // Arrange
        ContactDTO contactDTO = new ContactDTO("John Doe", "john@example.com");

        when(contactService.findByNameAndContactInfo(ArgumentMatchers.any(ContactDTO.class)))
                .thenReturn(Optional.of(new Contact()));

        // Act
        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contactDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Contact already exists"));

        // Assert
        verify(contactService, never()).createContact(ArgumentMatchers.any(Contact.class));
    }

    @Test
    void testCreateContact_ValidContact_ReturnSuccessMessage() throws Exception {
        ContactDTO contactDTO = new ContactDTO("John", "john@gmail.com");

        mockMvc.perform(post("/contacts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contactDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Contact created successfully."));

        verify(contactService, times(1)).createContact(ArgumentMatchers.any(Contact.class));
    }

    @Test
    void testUpdateContact_ExistingContact_ReturnSuccessMessage() throws Exception {
        Long id = 1L;
        ContactDTO contactDTO = new ContactDTO("John", "john@gmail.com");
        Contact existingContact = new Contact(id, "Old", "Contact");

        when(contactService.existsContact(id)).thenReturn(true);
        when(contactService.getContactById(id)).thenReturn(Optional.of(existingContact));

        mockMvc.perform(put("/contacts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contactDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Contact updated successfully."));

        verify(contactService, times(1)).existsContact(id);
        verify(contactService, times(1)).getContactById(id);
        verify(contactService, times(1)).updateContact(ArgumentMatchers.any(Contact.class));
    }

    @Test
    void testUpdateContact_NonExistingContact_ReturnNotFound() throws Exception {
        Long id = 1L;
        ContactDTO contactDTO = new ContactDTO("John", "john@gmail.com");

        when(contactService.existsContact(id)).thenReturn(false);

        mockMvc.perform(put("/contacts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(contactDTO)))
                .andExpect(status().isNotFound());

        verify(contactService, times(1)).existsContact(id);
        verify(contactService, never()).getContactById(id);
        verify(contactService, never()).updateContact(ArgumentMatchers.any(Contact.class));
    }

    @Test
    void testDeleteContact_ExistingContact_ReturnSuccessMessage() throws Exception {
        Long id = 1L;

        when(contactService.existsContact(id)).thenReturn(true);

        mockMvc.perform(delete("/contacts/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().string("Contact deleted successfully."));

        verify(contactService, times(1)).existsContact(id);
        verify(contactService, times(1)).deleteContact(id);
    }
}