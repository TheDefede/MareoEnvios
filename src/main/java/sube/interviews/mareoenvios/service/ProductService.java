package sube.interviews.mareoenvios.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import sube.interviews.mareoenvios.dto.response.TopSendedResponseDto;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.ItemDto;
import sube.interviews.mareoenvios.entity.Product;
import sube.interviews.mareoenvios.entity.ShippingItem;
import sube.interviews.mareoenvios.exception.BusinessRuleException;
import sube.interviews.mareoenvios.exception.ResourceNotFoundException;
import sube.interviews.mareoenvios.repository.ProductRepository;
import sube.interviews.mareoenvios.repository.ShippingItemRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ShippingItemRepository shippingItemRepository;

    public List<ShippingItem> resolveShippingItems(CreateShippingRequest request) {
        List<ShippingItem> validItems = new ArrayList<>();
        for (ItemDto itemDto : request.getProducts()) {
            productRepository.findById(itemDto.getProductId())
                    .ifPresentOrElse(product -> validItems.add(createShippingItem(itemDto, product)),
                    () -> {
                        if (!request.isPartialFulfillment()) {
                            throw new ResourceNotFoundException("Producto no encontrado con ID: " + itemDto.getProductId() + ". Envío cancelado.");
                        }
                        log.info("Producto ID {} no encontrado. Se omite por política de cumplimiento parcial.", itemDto.getProductId());
                    }
            );
        }
        if (validItems.isEmpty()) {
            throw new BusinessRuleException("No se encontraron productos válidos para crear el envío.");
        }
        return validItems;
    }

    private static ShippingItem createShippingItem(ItemDto itemDto, Product product) {
        return ShippingItem.builder()
                .product(product)
                .productCount(itemDto.getProductCount())
                .build();
    }

    public List<TopSendedResponseDto> getTopSendedProducts(int limit) {
        return shippingItemRepository.findTopSendedProducts(PageRequest.of(0, limit));
    }
}
