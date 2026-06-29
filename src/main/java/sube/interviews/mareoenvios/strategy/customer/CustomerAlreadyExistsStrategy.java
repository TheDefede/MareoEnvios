package sube.interviews.mareoenvios.strategy.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.exception.ResourceNotFoundException;
import sube.interviews.mareoenvios.repository.CustomerRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomerAlreadyExistsStrategy implements CustomerResolutionStrategy {

    private final CustomerRepository customerRepository;

    @Override
    public boolean supports(CreateShippingRequest request) {
        return request.getCustomerId() != null;
    }

    @Override
    public Customer resolve(CreateShippingRequest request) {
        log.info("Buscando cliente con ID: {}", request.getCustomerId());
        return customerRepository.fetchById(request.getCustomerId())
                .orElseThrow(()->new ResourceNotFoundException(String.format("Cliente con ID: %d no encontrado", request.getCustomerId())));
    }
}
