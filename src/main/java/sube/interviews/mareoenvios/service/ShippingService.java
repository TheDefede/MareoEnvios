package sube.interviews.mareoenvios.service;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sube.interviews.mareoenvios.dto.mapper.ShippingMapper;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.response.CustomerResponseDto;
import sube.interviews.mareoenvios.dto.response.ShippingResponseDto;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.entity.ShippingItem;
import sube.interviews.mareoenvios.enums.ShippingState;
import sube.interviews.mareoenvios.exception.BusinessRuleException;
import sube.interviews.mareoenvios.exception.RetryableIntegrationException;
import sube.interviews.mareoenvios.repository.ShippingRepository;
import sube.interviews.mareoenvios.strategy.customer.CustomerResolutionStrategy;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {

    private final ProductService productService;
    private final List<CustomerResolutionStrategy> customerStrategies;
    private final ShippingRepository shippingRepository;
    private final ShippingMapper shippingMapper;

    @Retry(name = "shippingRetry", fallbackMethod = "createShippingFallback")
    public ShippingResponseDto createShipping(CreateShippingRequest request) {
        Customer customer = customerStrategies.stream()
                .filter(strategy -> strategy.supports(request))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("No se encontró una estrategia válida para el cliente"))
                .resolve(request);

        List<ShippingItem> validatedItems = productService.resolveShippingItems(request);

        Shipping shipping = Shipping.builder()
                .customer(customer)
                .state(ShippingState.INICIAL)
                .sendDate(LocalDate.now())
                .priority(request.getPriority())
                .build();

        validatedItems.forEach(shipping::addItem);

        Shipping savedShipping = shippingRepository.save(shipping);

        return shippingMapper.toDto(savedShipping);
    }

    public ShippingResponseDto createShippingFallback(CreateShippingRequest request, RetryableIntegrationException ex) {
        log.error("Todos los reintentos fallaron para la solicitud del cliente {}. Motivo: {}",
                request.getCustomerId(), ex.getMessage());

        throw new BusinessRuleException("No se pudo procesar el envío por problemas técnicos. Intente más tarde.");
    }

}
