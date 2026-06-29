package sube.interviews.mareoenvios.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.response.ShippingResponseDto;
import sube.interviews.mareoenvios.dto.mapper.ShippingMapper;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.entity.ShippingItem;
import sube.interviews.mareoenvios.enums.ShippingState;
import sube.interviews.mareoenvios.exception.InvalidStateTransitionException;
import sube.interviews.mareoenvios.exception.ResourceNotFoundException;
import sube.interviews.mareoenvios.repository.ShippingRepository;
import sube.interviews.mareoenvios.service.ProductService;
import sube.interviews.mareoenvios.service.ShippingService;
import sube.interviews.mareoenvios.strategy.customer.CustomerResolutionStrategy;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShippingServiceTest {
    @Mock
    private ShippingRepository shippingRepository;
    @Mock
    private ShippingMapper shippingMapper;
    @Mock
    private ProductService productService;
    @Mock
    private CustomerResolutionStrategy customerStrategy;
    @InjectMocks
    private ShippingService shippingService;

    @Test
    void testCreateShipping_SuccessfullyCreatesShipping() {
        CreateShippingRequest request = new CreateShippingRequest();
        request.setCustomerId(1);
        request.setPriority(1);

        Customer customer = Customer.builder().id(1).firstName("Marcos").lastName("Gutierrez").build();
        Shipping shipping = Shipping.builder().id(10).customer(customer).state(ShippingState.INICIAL).build();
        Shipping savedShipping = Shipping.builder().id(10).customer(customer).state(ShippingState.INICIAL).build();
        ShippingResponseDto responseDto = ShippingResponseDto.builder().id(10).state("Inicial").build();
        ShippingItem shippingItem = ShippingItem.builder().id(1).shipping(shipping).build();

        shippingService = new ShippingService(
                productService,
                List.of(customerStrategy),
                shippingRepository,
                shippingMapper
        );

        when(customerStrategy.supports(request)).thenReturn(true);
        when(customerStrategy.resolve(request)).thenReturn(customer);
        when(productService.resolveShippingItems(request)).thenReturn(List.of(shippingItem));
        when(shippingRepository.save(any(Shipping.class))).thenReturn(savedShipping);
        when(shippingMapper.toDto(savedShipping)).thenReturn(responseDto);

        ShippingResponseDto result = shippingService.createShipping(request);
        assertNotNull(result);
        assertSame(responseDto, result);
        verify(customerStrategy, times(1)).resolve(request);
        verify(shippingRepository, times(1)).save(any(Shipping.class));
    }

    @Test
    void testTransitionTo_ValidTransition_UpdatesState() {
        Integer shippingId = 1;

        Shipping shipping = Shipping.builder()
                .id(shippingId)
                .state(ShippingState.INICIAL)
                .build();

        Shipping savedShipping = Shipping.builder().id(shippingId).state(ShippingState.ENTREGADO_CORREO).build();
        ShippingResponseDto responseDto = ShippingResponseDto.builder().id(shippingId).state("Entregado al correo").build();

        when(shippingRepository.findById(shippingId)).thenReturn(Optional.of(shipping));
        when(shippingRepository.save(shipping)).thenReturn(savedShipping);
        when(shippingMapper.toDto(savedShipping)).thenReturn(responseDto);

        ShippingResponseDto result = shippingService.transitionTo(shippingId, ShippingState.ENTREGADO_CORREO);

        assertNotNull(result);
        assertEquals(ShippingState.ENTREGADO_CORREO, shipping.getState());
        assertSame(responseDto, result);

        verify(shippingRepository, times(1)).save(shipping);
    }

    @Test
    void testTransitionTo_InvalidTransition_ThrowsInvalidStateTransitionException() {
        Integer shippingId = 1;

        Shipping shipping = Shipping.builder()
                .id(shippingId)
                .state(ShippingState.INICIAL)
                .build();

        when(shippingRepository.findById(shippingId)).thenReturn(Optional.of(shipping));
        assertThrows(InvalidStateTransitionException.class, () -> {
            shippingService.transitionTo(shippingId, ShippingState.EN_CAMINO);
        });

        verify(shippingRepository, never()).save(any());
    }

    @Test
    void ShippingNotFound_ThrowsResourceNotFoundException() {
        Integer shippingId = 999;
        when(shippingRepository.findById(shippingId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            shippingService.transitionTo(shippingId, ShippingState.ENTREGADO_CORREO);
        });
        verify(shippingRepository, never()).save(any());
    }

    @Test
    void testTransitionTo_StateDelivered_SetsArriveDate() {
        Integer shippingId = 1;

        Shipping shipping = Shipping.builder()
                .id(shippingId)
                .state(ShippingState.EN_CAMINO)
                .arriveDate(null)
                .build();

        Shipping savedShipping = Shipping.builder()
                .id(shippingId)
                .state(ShippingState.ENTREGADO)
                .arriveDate(java.time.Instant.now())
                .build();

        ShippingResponseDto responseDto = ShippingResponseDto.builder()
                .id(shippingId)
                .state("Entregado")
                .arriveDate(Instant.now())
                .build();

        when(shippingRepository.findById(shippingId)).thenReturn(Optional.of(shipping));
        when(shippingRepository.save(shipping)).thenReturn(savedShipping);
        when(shippingMapper.toDto(savedShipping)).thenReturn(responseDto);

        ShippingResponseDto result = shippingService.transitionTo(shippingId, ShippingState.ENTREGADO);

        assertNotNull(result);
        assertEquals(ShippingState.ENTREGADO, shipping.getState());
        assertNotNull(shipping.getArriveDate());

        assertTrue(shipping.getArriveDate().isAfter(Instant.now().minusSeconds(5)));

        verify(shippingRepository, times(1)).save(shipping);
    }
}
