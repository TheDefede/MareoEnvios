package sube.interviews.mareoenvios.strategy.shipping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.response.ShippingResponseDto;
import sube.interviews.mareoenvios.service.ShippingService;

@Component
@RequiredArgsConstructor
public class DefaultResolutionStrategy implements ShippingResolutionStrategy{

    private final ShippingService shippingService;

    @Override
    public boolean supports(CreateShippingRequest request) {
        return request.getPriority() >= 0;
    }

    @Override
    public ShippingResponseDto resolve(CreateShippingRequest request) {
        return shippingService.createShipping(request);
    }
}
