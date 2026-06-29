package sube.interviews.mareoenvios.dto.mapper;

import org.springframework.stereotype.Component;
import sube.interviews.mareoenvios.dto.request.CreateShippingRequest;
import sube.interviews.mareoenvios.dto.CustomerDto;
import sube.interviews.mareoenvios.entity.Customer;
import sube.interviews.mareoenvios.exception.BusinessRuleException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    public CustomerDto toDto(Customer entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return CustomerDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .address(entity.getAddress())
                .city(entity.getCity())
                .build();
    }

    public List<CustomerDto> toDtoList(List<Customer> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Customer toEntity(CreateShippingRequest request){
        if (Objects.isNull(request)) {
            throw new BusinessRuleException("La entidad no puede ser null.");
        }

        if (isInvalid(request.getFirstName()) ||
                isInvalid(request.getLastName()) ||
                isInvalid(request.getAddress()) ||
                isInvalid(request.getCity())) {
            throw new BusinessRuleException(
                    "Los datos del comprador (nombre, apellido, dirección y ciudad) son obligatorios para registrar un nuevo cliente.");
        }

        return Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .city(request.getCity())
                .build();
    }

    private boolean isInvalid(String str) {
        return Objects.isNull(str) || str.trim().isEmpty();
    }
}