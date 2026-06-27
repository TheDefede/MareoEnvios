package sube.interviews.mareoenvios.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sube.interviews.mareoenvios.dto.ShippingItemDto;
import sube.interviews.mareoenvios.entity.ShippingItem;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShippingItemMapper {

    private final ProductMapper productMapper;

    public ShippingItemDto toDto(ShippingItem entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return ShippingItemDto.builder()
                .id(entity.getId())
                .product(productMapper.toDto(entity.getProduct()))
                .productCount(entity.getProductCount())
                .build();
    }

    public List<ShippingItemDto> toDtoList(List<ShippingItem> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}