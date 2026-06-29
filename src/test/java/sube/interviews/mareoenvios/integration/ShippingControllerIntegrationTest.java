package sube.interviews.mareoenvios.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.exception.RetryableIntegrationException;
import sube.interviews.mareoenvios.service.ShippingService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ShippingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private ShippingService shippingService;

    @Test
    void testTransitionToSendToMail_ShouldRetryAndSucceed() throws Exception {
        Integer shippingId = 3;

        doThrow(new CannotAcquireLockException("Simulated lock issue"))
                .doThrow(new CannotAcquireLockException("Simulated lock issue"))
                .doCallRealMethod()
                .when(shippingService).save(any(Shipping.class));

        mockMvc.perform(post("/shipping/transition/sendToMail/{shippingId}", shippingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shippingId))
                .andExpect(jsonPath("$.state").value("Entregado al correo"));

        verify(shippingService, times(3)).save(any(Shipping.class));
    }

    @Test
    void testCreateShipping_ShouldRetryAndFallbackOnRetryableException() throws Exception {
        doThrow(new RetryableIntegrationException("Error de integración"))
                .when(shippingService).save(any(Shipping.class));

        String jsonPayload = """
                {
                  "customerId": 1,
                  "priority": 1,
                  "products": [
                    {
                      "productId": 1,
                      "productCount": 2
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/shipping/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isFailedDependency())
                .andExpect(jsonPath("$.status").value(424))
                .andExpect(jsonPath("$.message").value("No se pudo procesar el envío por problemas técnicos. Intente más tarde."));

        verify(shippingService, times(3)).save(any(Shipping.class));

        verify(shippingService, times(1)).createShippingFallback(any(), any());
    }

    @Test
    void testCreateShipping_ShouldNotRetryOnNonConfiguredException() throws Exception {
        doThrow(new NullPointerException("Simulated non-retryable exception"))
                .when(shippingService).save(any(Shipping.class));

        String jsonPayload = """
                {
                  "customerId": 1,
                  "priority": 1,
                  "products": [
                    {
                      "productId": 1,
                      "productCount": 2
                    }
                  ]
                }
                """;

        mockMvc.perform(post("/shipping/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPayload))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("Simulated non-retryable exception"));

        verify(shippingService, times(1)).save(any(Shipping.class));

        verify(shippingService, never()).createShippingFallback(any(), any());
    }

    @Test
    void testGetShippingById_WhenExists_ReturnsShipping() throws Exception {
        Integer shippingId = 1;
        mockMvc.perform(get("/shipping/info/{shippingId}", shippingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(shippingId))
                .andExpect(jsonPath("$.state").value("Entregado"))
                .andExpect(jsonPath("$.customer.firstName").value("Marcos"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(3));
    }

    @Test
    void testGetShippingById_WhenNotExists_Returns404NotFound() throws Exception {
        Integer shippingId = 999;
        mockMvc.perform(get("/shipping/info/{shippingId}", shippingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Envío no encontrado con ID: 999"));
    }

    @Test
    void testGetShippingsBySendDate_ReturnsPaginatedList() throws Exception {
        String dateFrom = LocalDate.now().minusDays(10).toString();
        String dateTo = LocalDate.now().plusDays(1).toString();
        mockMvc.perform(get("/shipping/info/{sendDateFrom}/{sendDateTo}", dateFrom, dateTo)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").exists())
                .andExpect(jsonPath("$.page.size").value(10));
    }

    @Test
    void testGetShippingsByState_DescriptionName_ReturnsPaginatedList() throws Exception {
        mockMvc.perform(get("/shipping/info/state/{state}", "En camino")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].state").value("En camino"))
                .andExpect(jsonPath("$.content[0].id").value(2));
    }

    @Test
    void testGetShippingsByState_InvalidState_Returns422() throws Exception {
        mockMvc.perform(get("/shipping/info/state/{state}", "estado_fantasma")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.message").value("Estado de envío no válido: estado_fantasma"));
    }

}