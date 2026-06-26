package sube.interviews.mareoenvios.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.exception.BusinessRuleException;
import sube.interviews.mareoenvios.strategy.customer.CustomerResolutionStrategy;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final List<CustomerResolutionStrategy> customerStrategies;

    public Customer get(CreateShippingRequest request) {
        return customerStrategies.stream()
                .filter(strategy -> strategy.supports(request))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("No se encontró una estrategia válida para el cliente"))
                .resolve(request);
    }
}
