package sube.interviews.mareoenvios.strategy.customer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sube.interviews.mareoenvios.dto.mapper.CustomerMapper;
import sube.interviews.mareoenvios.repository.CustomerRepository;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.entity.Customer;


@Slf4j
@Component
@RequiredArgsConstructor
public class NewCustomerStrategy implements CustomerResolutionStrategy{

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public boolean supports(CreateShippingRequest request) {
        return request.getCustomerId() == null;
    }

    @Override
    public Customer resolve(CreateShippingRequest request) {
        log.info("Creando cliente nuevo");
        return customerRepository.save(customerMapper.toEntity(request));
    }
}
