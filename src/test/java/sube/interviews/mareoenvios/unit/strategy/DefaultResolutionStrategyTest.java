package sube.interviews.mareoenvios.unit.strategy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.response.ShippingResponseDto;
import sube.interviews.mareoenvios.service.ShippingService;
import sube.interviews.mareoenvios.strategy.shipping.DefaultResolutionStrategy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultResolutionStrategyTest {
    @Mock
    private ShippingService shippingService;
    @InjectMocks
    private DefaultResolutionStrategy defaultResolutionStrategy;

    @Test
    void testSupports_WithPriorityGreaterOrEqualToZero_ReturnsTrue() {
        CreateShippingRequest request = new CreateShippingRequest();

        request.setPriority(0);
        assertTrue(defaultResolutionStrategy.supports(request));
        request.setPriority(1);
        assertTrue(defaultResolutionStrategy.supports(request));
    }

    @Test
    void testSupports_WithPriorityLessThanZero_ReturnsFalse() {
        CreateShippingRequest request = new CreateShippingRequest();
        request.setPriority(-1);

        assertFalse(defaultResolutionStrategy.supports(request));
    }

    @Test
    void testResolve_DelegatesToShippingService() {
        CreateShippingRequest request = new CreateShippingRequest();
        ShippingResponseDto responseDto = ShippingResponseDto.builder().id(1).build();

        when(shippingService.createShipping(request)).thenReturn(responseDto);

        ShippingResponseDto result = defaultResolutionStrategy.resolve(request);

        assertNotNull(result);
        assertSame(responseDto, result);
        verify(shippingService, times(1)).createShipping(request);
    }

}
