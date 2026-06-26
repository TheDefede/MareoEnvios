package sube.interviews.mareoenvios.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShippingItemResponseDto  {
    private Integer id;
    private ProductResponseDto product;
    private Integer productCount;
}