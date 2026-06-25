package sube.interviews.mareoenvios.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sube.interviews.mareoenvios.repository.CustomerRepository;
import sube.interviews.mareoenvios.dto.CreateShippingRequest;
import sube.interviews.mareoenvios.entity.Customer;

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
        return customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + request.getCustomerId()));
    }
}
