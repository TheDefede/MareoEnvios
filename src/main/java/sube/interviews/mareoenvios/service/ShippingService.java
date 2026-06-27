package sube.interviews.mareoenvios.service;

import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sube.interviews.mareoenvios.dto.mapper.ShippingMapper;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.response.ShippingResponseDto;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.entity.Shipping;
import sube.interviews.mareoenvios.entity.ShippingItem;
import sube.interviews.mareoenvios.enums.ShippingState;
import sube.interviews.mareoenvios.exception.*;
import sube.interviews.mareoenvios.repository.ShippingRepository;
import sube.interviews.mareoenvios.strategy.customer.CustomerResolutionStrategy;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShippingService {

    private final ProductService productService;
    private final List<CustomerResolutionStrategy> customerStrategies;
    private final ShippingRepository shippingRepository;
    private final ShippingMapper shippingMapper;

    @Retry(name = "shippingRetry", fallbackMethod = "createShippingFallback")
    public ShippingResponseDto createShipping(CreateShippingRequest request) {
        Customer customer = customerStrategies.stream()
                .filter(strategy -> strategy.supports(request))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("No se encontró una estrategia válida para el cliente"))
                .resolve(request);

        List<ShippingItem> validatedItems = productService.resolveShippingItems(request);

        Shipping shipping = Shipping.builder()
                .customer(customer)
                .state(ShippingState.INICIAL)
                .sendDate(Instant.now())
                .priority(request.getPriority())
                .build();

        validatedItems.forEach(shipping::addItem);

        Shipping savedShipping = this.save(shipping);

        return shippingMapper.toDto(savedShipping);
    }

    @Cacheable(value = "shippings", key = "#shippingId")
    public ShippingResponseDto getShippingInfo(Integer shippingId) {
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(()-> new ResourceNotFoundException(String.format("Ship not found with ID: %d", shippingId)));

        log.info("Find shipping with ID: {}", shippingId);

        return shippingMapper.toDto(shipping);
    }

    public Page<ShippingResponseDto> getShippingsBySendDate(LocalDate sendDateFrom, LocalDate sendDateTo, Pageable pageable) {
        Instant start = sendDateFrom.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = sendDateTo.atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();
        Page<Shipping> shippingPage = shippingRepository.findBySendDateBetween(start, end, pageable);
        return shippingPage.map(shippingMapper::toDto);
    }

    public Page<ShippingResponseDto> getShippingsByState(String stateDescription, Pageable pageable) {
        ShippingState state = Stream.of(ShippingState.values())
                .filter(s -> s.getDescription().equalsIgnoreCase(stateDescription))
                .findFirst()
                .orElseThrow(() -> new BusinessRuleException("Estado de envío no válido: " + stateDescription));

        Page<Shipping> shippingPage = shippingRepository.findByState(state, pageable);
        return shippingPage.map(shippingMapper::toDto);
    }

    @Transactional
    @Retry(name = "shippingRetry", fallbackMethod = "transitionToFallback")
    @CacheEvict(value = "shippings", key = "#shippingId")
    public ShippingResponseDto transitionTo(Integer shippingId, ShippingState targetState) {
        Shipping shipping = shippingRepository.findById(shippingId)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Envío con ID: %d no encontrado", shippingId)));

        if (!shipping.getState().canTransitionTo(targetState)) {
            throw new InvalidStateTransitionException(
                    String.format("No se puede transicionar el envío del estado [%s] al estado [%s]",
                            shipping.getState().getDescription(), targetState.getDescription()));
        }

        shipping.setState(targetState);
        if (targetState.equals(ShippingState.ENTREGADO)) {
            shipping.setArriveDate(Instant.now());
        }

        Shipping savedShipping = this.save(shipping);

        return shippingMapper.toDto(savedShipping);
    }

    public Shipping save(Shipping shipping){
        return shippingRepository.save(shipping);
    }

    public ShippingResponseDto createShippingFallback(CreateShippingRequest request, RetryableIntegrationException ex) {
        log.error("Todos los reintentos fallaron para la solicitud del cliente {}. Motivo: {}",
                request.getCustomerId(), ex.getMessage());

        throw new FailedDependencyException("No se pudo procesar el envío por problemas técnicos. Intente más tarde.");
    }

}
