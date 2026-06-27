package sube.interviews.mareoenvios.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.ItemDto;
import sube.interviews.mareoenvios.entity.Product;
import sube.interviews.mareoenvios.entity.ShippingItem;
import sube.interviews.mareoenvios.exception.ResourceNotFoundException;
import sube.interviews.mareoenvios.repository.ProductRepository;
import sube.interviews.mareoenvios.service.ProductService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testResolveShippingItems_AllProductsFound_ReturnsAllItems() {
        CreateShippingRequest request = mock(CreateShippingRequest.class);
        ItemDto item1 = mock(ItemDto.class);
        ItemDto item2 = mock(ItemDto.class);
        
        when(request.getProducts()).thenReturn(Arrays.asList(item1, item2));

        when(item1.getProductId()).thenReturn(101);
        when(item1.getCount()).thenReturn(2);
        
        when(item2.getProductId()).thenReturn(102);
        when(item2.getCount()).thenReturn(1);

        Product product1 = new Product();
        Product product2 = new Product();

        when(productRepository.findById(101)).thenReturn(Optional.of(product1));
        when(productRepository.findById(102)).thenReturn(Optional.of(product2));

        List<ShippingItem> result = productService.resolveShippingItems(request);

        assertEquals(2, result.size());
        verify(productRepository, times(2)).findById(anyInt());
    }

    @Test
    void testResolveShippingItems_ProductMissingAndPartialFulfillmentIsTrue_SkipsMissingProduct() {
        CreateShippingRequest request = mock(CreateShippingRequest.class);
        ItemDto item1 = mock(ItemDto.class);
        ItemDto item2 = mock(ItemDto.class);
        
        when(request.getProducts()).thenReturn(Arrays.asList(item1, item2));
        when(request.isPartialFulfillment()).thenReturn(true);

        when(item1.getProductId()).thenReturn(101);
        when(item1.getCount()).thenReturn(2);
        
        when(item2.getProductId()).thenReturn(999);
        
        Product product1 = new Product();

        when(productRepository.findById(101)).thenReturn(Optional.of(product1));
        when(productRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        List<ShippingItem> result = productService.resolveShippingItems(request);

        // Assert
        assertEquals(1, result.size(), "Debería haber omitido el producto faltante y devolver solo 1 item");
        assertEquals(2, result.get(0).getProductCount());
    }

    @Test
    void testResolveShippingItems_ProductMissingAndPartialFulfillmentIsFalse_ThrowsException() {
        CreateShippingRequest request = mock(CreateShippingRequest.class);
        ItemDto item1 = mock(ItemDto.class);
        ItemDto item2 = mock(ItemDto.class);
        
        when(request.getProducts()).thenReturn(Arrays.asList(item1, item2));

        when(request.isPartialFulfillment()).thenReturn(false);

        when(item1.getProductId()).thenReturn(101);
        when(item2.getProductId()).thenReturn(999);
        
        Product product1 = new Product();

        when(productRepository.findById(101)).thenReturn(Optional.of(product1));
        when(productRepository.findById(999)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            productService.resolveShippingItems(request);
        });

        assertTrue(exception.getMessage().contains("999"));
    }
}
