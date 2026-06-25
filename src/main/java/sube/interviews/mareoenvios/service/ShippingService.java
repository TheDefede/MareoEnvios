package sube.interviews.mareoenvios.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sube.interviews.mareoenvios.dto.CreateShippingRequest;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.strategy.CustomerResolutionStrategy;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShippingService {

    private final List<CustomerResolutionStrategy> customerStrategies;

    public void createShipping(CreateShippingRequest request) {
        // 1. Resolvemos el Cliente delegando en las Estrategias
        Customer customer = customerStrategies.stream()
                .filter(strategy -> strategy.supports(request))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No se encontró una estrategia válida para el cliente"))
                .resolve(request);
    }


}
