package sube.interviews.mareoenvios.strategy.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.exception.RetryableIntegrationException;

import java.util.List;

@Component
@Order(1)
@Slf4j
public class CustomerBlockStrategy implements CustomerResolutionStrategy{
    @Value("${chaos.customer}")
    private List<Integer> chaosCustomers;

    @Override
    public boolean supports(CreateShippingRequest request) {
        return chaosCustomers.contains(request.getCustomerId());
    }

    @Override
    public Customer resolve(CreateShippingRequest request) {
        log.info("Customer {} is blocked for testing", request.getCustomerId());
        throw new RetryableIntegrationException("Customer is blocked for testing");
    }
}
