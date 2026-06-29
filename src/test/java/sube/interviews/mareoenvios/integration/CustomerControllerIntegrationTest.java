package sube.interviews.mareoenvios.integration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import sube.interviews.mareoenvios.repository.CustomerRepository;
import sube.interviews.mareoenvios.service.CustomerService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CustomerControllerIntegrationTest {

    @Autowired
    private CustomerService customerService;

    @SpyBean
    private CustomerRepository customerRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateCustomer_WithValidData_ReturnsCreatedCustomer() throws Exception {
        String jsonPayload = """
                {
                  "firstName": "Juan",
                  "lastName": "Perez",
                  "address": "Calle Falsa 123",
                  "city": "Mendoza"
                }
                """;
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/customer/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Juan"))
                .andExpect(jsonPath("$.lastName").value("Perez"));
    }

    @Test
    void testCreateCustomer_WithMissingFields_Returns422Unprocessable() throws Exception {
        String jsonPayload = """
                {
                  "firstName": "   ",
                  "lastName": "Perez",
                  "address": "Calle Falsa 123"
                }
                """;
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/customer/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("Los datos del comprador (nombre, apellido, dirección y ciudad) son obligatorios."));
    }

    @Test
    void testUpdateCustomer_WithValidData_UpdatesAndEvictsCache() throws Exception {
        Integer customerId = 2;

        mockMvc.perform(get("/customer/info/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Hernan"));

        mockMvc.perform(get("/customer/info/{customerId}", customerId))
                .andExpect(status().isOk());

        verify(customerRepository, times(1)).fetchById(customerId);

        String updatePayload = """
                {
                  "firstName": "Hernando",
                  "lastName": "Toledo",
                  "address": "Nueva Direccion 789",
                  "city": "CABA"
                }
                """;

        mockMvc.perform(put("/customer/update/{customerId}", customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatePayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Hernando"));

        mockMvc.perform(get("/customer/info/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Hernando"));

        verify(customerRepository, times(2)).fetchById(customerId);
    }
}
