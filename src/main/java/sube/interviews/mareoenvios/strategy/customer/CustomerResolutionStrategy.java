package sube.interviews.mareoenvios.strategy.customer;

import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.response.CustomerResponseDto;
import sube.interviews.mareoenvios.entity.Customer;

public interface CustomerResolutionStrategy {
    boolean supports(CreateShippingRequest request);
    Customer resolve(CreateShippingRequest request);
}
