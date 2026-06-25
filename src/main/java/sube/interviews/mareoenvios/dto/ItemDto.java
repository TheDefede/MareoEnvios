package sube.interviews.mareoenvios.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDto {
    @NotNull(message = "El ID del producto no puede ser nulo")
    private Integer productId;

    @NotNull(message = "La cantidad no puede ser nula")
    private Integer count;
}
