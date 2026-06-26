package sube.interviews.mareoenvios.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponseDto  {
    private Integer id;
    private String description;
    private Double weight;
}