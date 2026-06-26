package sube.interviews.mareoenvios.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.response.ShippingResponseDto;
import sube.interviews.mareoenvios.exception.BusinessRuleException;
import sube.interviews.mareoenvios.strategy.shipping.ShippingResolutionStrategy;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProcessService {

    private final List<ShippingResolutionStrategy> shippingStrategies;

    public ShippingResponseDto process(CreateShippingRequest request){
        return shippingStrategies.stream()
                .filter(strategy -> strategy.supports(request))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("No se encontró una estrategia válida"))
                .resolve(request);
    }
}
