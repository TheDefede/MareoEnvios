package sube.interviews.mareoenvios.service;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sube.interviews.mareoenvios.dto.CreateShippingRequest;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.entity.ShippingItem;
import sube.interviews.mareoenvios.exception.BusinessRuleException;
import sube.interviews.mareoenvios.exception.RetryableIntegrationException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {

    private final ProductService productService;
    private final CustomerService customerService;

    @Retry(name = "shippingRetry", fallbackMethod = "createShippingFallback")
    public void createShipping(CreateShippingRequest request) {
        Customer customer = customerService.get(request);

        List<ShippingItem> validatedItems = productService.resolveShippingItems(request);
    }

    public void createShippingFallback(CreateShippingRequest request, RetryableIntegrationException ex) {
        log.error("Todos los reintentos fallaron para la solicitud del cliente {}. Motivo: {}",
                request.getCustomerId(), ex.getMessage());

        throw new BusinessRuleException("No se pudo procesar el envío por problemas técnicos. Intente más tarde.");
    }


}
