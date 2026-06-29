package sube.interviews.mareoenvios.unit.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sube.interviews.mareoenvios.dto.ProductDto;
import sube.interviews.mareoenvios.dto.ShippingItemDto;
import sube.interviews.mareoenvios.dto.mapper.ProductMapper;
import sube.interviews.mareoenvios.dto.mapper.ShippingItemMapper;
import sube.interviews.mareoenvios.entity.Product;
import sube.interviews.mareoenvios.entity.ShippingItem;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShippingItemMapperTest {

    @Mock
    private ProductMapper productMapper;
    @InjectMocks
    private ShippingItemMapper shippingItemMapper;

    @Test
    void testToDto_WithValidShippingItem_ReturnsDto() {
        Product product = Product.builder().id(1).description("Termo").build();
        ShippingItem entity = ShippingItem.builder()
                .id(10)
                .product(product)
                .productCount(5)
                .build();

        ProductDto productDto = ProductDto.builder().id(1).description("Termo").build();

        when(productMapper.toDto(product)).thenReturn(productDto);

        ShippingItemDto dto = shippingItemMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getProductCount(), dto.getProductCount());
        assertEquals(productDto, dto.getProduct());
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void testToDto_WithNullShippingItem_ReturnsNull() {
        assertNull(shippingItemMapper.toDto(null));
    }

    @Test
    void testToDtoList_WithValidList_ReturnsDtoList() {
        ShippingItem item1 = ShippingItem.builder().id(1).build();
        ShippingItem item2 = ShippingItem.builder().id(2).build();

        List<ShippingItemDto> result = shippingItemMapper.toDtoList(Arrays.asList(item1, item2));

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testToDtoList_WithNullList_ReturnsEmptyList() {
        List<ShippingItemDto> result = shippingItemMapper.toDtoList(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
