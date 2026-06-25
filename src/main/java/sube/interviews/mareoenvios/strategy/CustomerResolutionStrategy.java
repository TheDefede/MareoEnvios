package sube.interviews.mareoenvios.strategy;

import sube.interviews.mareoenvios.dto.CreateShippingRequest;
import sube.interviews.mareoenvios.entity.Customer;

public interface CustomerResolutionStrategy {
    boolean supports(CreateShippingRequest request);
    Customer resolve(CreateShippingRequest request);
}
