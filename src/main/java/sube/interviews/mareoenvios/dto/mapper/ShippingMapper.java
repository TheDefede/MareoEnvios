package sube.interviews.mareoenvios.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sube.interviews.mareoenvios.dto.response.ShippingResponseDto;
import sube.interviews.mareoenvios.entity.Shipping;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShippingMapper {

    private final CustomerMapper customerMapper;
    private final ShippingItemMapper shippingItemMapper;

    public ShippingResponseDto toDto(Shipping entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return ShippingResponseDto.builder()
                .id(entity.getId())
                .customer(customerMapper.toDto(entity.getCustomer()))
                .state(entity.getState() != null ? entity.getState().getDescription() : null)
                .sendDate(entity.getSendDate())
                .arriveDate(entity.getArriveDate())
                .priority(entity.getPriority())
                .items(shippingItemMapper.toDtoList(entity.getItems()))
                .build();
    }

    public List<ShippingResponseDto> toDtoList(List<Shipping> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}