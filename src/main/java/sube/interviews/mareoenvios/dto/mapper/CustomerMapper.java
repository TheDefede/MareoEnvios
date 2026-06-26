package sube.interviews.mareoenvios.dto.mapper;

import org.springframework.stereotype.Component;
import sube.interviews.mareoenvios.dto.response.CustomerResponseDto;
import sube.interviews.mareoenvios.entity.Customer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class CustomerMapper {

    public CustomerResponseDto toDto(Customer entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return CustomerResponseDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .address(entity.getAddress())
                .city(entity.getCity())
                .build();
    }

    public List<CustomerResponseDto> toDtoList(List<Customer> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}