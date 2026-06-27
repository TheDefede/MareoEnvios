package sube.interviews.mareoenvios.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippingItemDto {
    private Integer id;
    private ProductDto product;
    private Integer productCount;
}