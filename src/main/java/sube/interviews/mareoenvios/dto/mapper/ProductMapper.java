package sube.interviews.mareoenvios.dto.mapper;

import org.springframework.stereotype.Component;
import sube.interviews.mareoenvios.dto.response.ProductResponseDto;
import sube.interviews.mareoenvios.entity.Product;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductResponseDto toDto(Product entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return ProductResponseDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .weight(entity.getWeight())
                .build();
    }

    public List<ProductResponseDto> toDtoList(List<Product> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}