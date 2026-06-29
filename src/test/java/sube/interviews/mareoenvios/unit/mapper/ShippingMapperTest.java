package sube.interviews.mareoenvios.unit.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sube.interviews.mareoenvios.dto.CustomerDto;
import sube.interviews.mareoenvios.dto.ShippingItemDto;
import sube.interviews.mareoenvios.dto.response.ShippingResponseDto;
import sube.interviews.mareoenvios.dto.mapper.CustomerMapper;
import sube.interviews.mareoenvios.dto.mapper.ShippingItemMapper;
import sube.interviews.mareoenvios.dto.mapper.ShippingMapper;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.entity.ShippingItem;
import sube.interviews.mareoenvios.enums.ShippingState;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShippingMapperTest {

    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private ShippingItemMapper shippingItemMapper;
    @InjectMocks
    private ShippingMapper shippingMapper;

    @Test
    void testToDto_WithValidShipping_ReturnsDto() {
        Customer customer = Customer.builder().id(1).build();
        ShippingItem item = ShippingItem.builder().id(10).build();
        Instant now = Instant.now();

        Shipping shipping = Shipping.builder()
                .id(100)
                .customer(customer)
                .state(ShippingState.ENTREGADO_CORREO)
                .sendDate(now)
                .arriveDate(now)
                .priority(2)
                .items(Collections.singletonList(item))
                .build();

        CustomerDto customerDto = CustomerDto.builder().id(1).build();
        ShippingItemDto itemDto = ShippingItemDto.builder().id(10).build();

        when(customerMapper.toDto(customer)).thenReturn(customerDto);
        when(shippingItemMapper.toDtoList(shipping.getItems())).thenReturn(Collections.singletonList(itemDto));

        ShippingResponseDto result = shippingMapper.toDto(shipping);

        assertNotNull(result);
        assertEquals(100, result.getId());
        assertEquals(customerDto, result.getCustomer());
        assertEquals("Entregado al correo", result.getState());
        assertEquals(now, result.getSendDate());
        assertEquals(now, result.getArriveDate());
        assertEquals(2, result.getPriority());
        assertEquals(1, result.getItems().size());
        assertEquals(itemDto, result.getItems().get(0));
        verify(customerMapper, times(1)).toDto(customer);
        verify(shippingItemMapper, times(1)).toDtoList(anyList());
    }

    @Test
    void testToDto_WithNullShipping_ReturnsNull() {
        assertNull(shippingMapper.toDto(null));
    }

    @Test
    void testToDtoList_WithValidList_ReturnsDtoList() {
        Shipping shipping1 = Shipping.builder().id(1).build();
        Shipping shipping2 = Shipping.builder().id(2).build();
        List<ShippingResponseDto> result = shippingMapper.toDtoList(Arrays.asList(shipping1, shipping2));
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testToDtoList_WithNullList_ReturnsEmptyList() {
        List<ShippingResponseDto> result = shippingMapper.toDtoList(null);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
