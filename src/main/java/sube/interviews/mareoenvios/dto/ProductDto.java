package sube.interviews.mareoenvios.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDto {
    private Integer id;
    private String description;
    private Double weight;
}