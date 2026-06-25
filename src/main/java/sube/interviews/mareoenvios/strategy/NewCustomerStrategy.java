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
public class NewCustomerStrategy implements CustomerResolutionStrategy{

    private final CustomerRepository customerRepository;

    @Override
    public boolean supports(CreateShippingRequest request) {
        return request.getCustomerId() == null;
    }

    @Override
    public Customer resolve(CreateShippingRequest request) {
        Customer newCustomer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .city(request.getCity())
                .build();

        return customerRepository.save(newCustomer);
    }
}
