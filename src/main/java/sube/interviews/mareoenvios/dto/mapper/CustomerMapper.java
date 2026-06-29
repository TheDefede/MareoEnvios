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

    public Customer toEntity(CustomerDto dto) {
        if (Objects.isNull(dto)) {
            return null;
        }

        if (isInvalid(dto.getFirstName()) ||
                isInvalid(dto.getLastName()) ||
                isInvalid(dto.getAddress()) ||
                isInvalid(dto.getCity())) {
            throw new BusinessRuleException(
                    "Los datos del comprador (nombre, apellido, dirección y ciudad) son obligatorios.");
        }
        return Customer.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .address(dto.getAddress())
                .city(dto.getCity())
                .build();
    }

    public Customer toEntity(CreateShippingRequest request) {
        if (Objects.isNull(request)) {
            return null;
        }
        CustomerDto tempDto = CustomerDto.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .address(request.getAddress())
                .city(request.getCity())
                .build();

        return toEntity(tempDto);
    }

    public void updateEntity(Customer customer, CustomerDto dto) {
        if (Objects.isNull(dto)) {
            return;
        }

        if (Objects.nonNull(dto.getFirstName())) {
            if (dto.getFirstName().isBlank()) {
                throw new BusinessRuleException("El nombre del comprador no puede estar vacío.");
            }
            customer.setFirstName(dto.getFirstName());
        }
        if (Objects.nonNull(dto.getLastName())) {
            if (dto.getLastName().isBlank()) {
                throw new BusinessRuleException("El apellido del comprador no puede estar vacío.");
            }
            customer.setLastName(dto.getLastName());
        }
        if (Objects.nonNull(dto.getAddress())) {
            if (dto.getAddress().isBlank()) {
                throw new BusinessRuleException("La dirección del comprador no puede estar vacía.");
            }
            customer.setAddress(dto.getAddress());
        }
        if (Objects.nonNull(dto.getCity())) {
            if (dto.getCity().isBlank()) {
                throw new BusinessRuleException("La ciudad del comprador no puede estar vacía.");
            }
            customer.setCity(dto.getCity());
        }
    }

    private boolean isInvalid(String str) {
        return Objects.isNull(str) || str.isBlank();
    }
}