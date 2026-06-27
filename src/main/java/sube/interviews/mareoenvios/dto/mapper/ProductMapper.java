package sube.interviews.mareoenvios.dto.mapper;

import org.springframework.stereotype.Component;
import sube.interviews.mareoenvios.dto.ProductDto;
import sube.interviews.mareoenvios.entity.Product;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProductMapper {

    public ProductDto toDto(Product entity) {
        if (Objects.isNull(entity)) {
            return null;
        }
        return ProductDto.builder()
                .id(entity.getId())
                .description(entity.getDescription())
                .weight(entity.getWeight())
                .build();
    }

    public List<ProductDto> toDtoList(List<Product> entities) {
        if (Objects.isNull(entities)) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}