package sube.interviews.mareoenvios.strategy.shipping;

import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.response.ShippingResponseDto;

public interface ShippingResolutionStrategy {
    boolean supports(CreateShippingRequest request);
    ShippingResponseDto resolve(CreateShippingRequest request);
}
