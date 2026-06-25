package sube.interviews.mareoenvios.dto;

import jakarta.validation.constraints.NotNull;

public class ItemDto {
    @NotNull(message = "El ID del producto no puede ser nulo")
    private Integer productId;

    @NotNull(message = "La cantidad no puede ser nula")
    private Integer count;
}
