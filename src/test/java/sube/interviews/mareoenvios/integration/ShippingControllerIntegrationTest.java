package sube.interviews.mareoenvios.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.enums.ShippingState;
import sube.interviews.mareoenvios.service.ShippingService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
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
    void testTransitionToSendToMail_ShouldFailAfter3AttemptsAndReturn424() throws Exception {
        Integer shippingId = 3;

        doThrow(new CannotAcquireLockException("Simulated permanent lock issue"))
                .when(shippingService).save(any(Shipping.class));

        mockMvc.perform(post("/shipping/transition/sendToMail/{shippingId}", shippingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isFailedDependency())
                .andExpect(jsonPath("$.status").value(424))
                .andExpect(jsonPath("$.message").value("No se pudo cambiar el estado del envío por problemas técnicos. Intente más tarde."));

        verify(shippingService, times(3)).save(any(Shipping.class));
        verify(shippingService, times(1)).transitionToFallback(eq(shippingId), eq(ShippingState.ENTREGADO_CORREO), any());
    }
}