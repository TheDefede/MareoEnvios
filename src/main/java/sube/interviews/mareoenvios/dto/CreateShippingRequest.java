package sube.interviews.mareoenvios.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateShippingRequest {
    private Integer customerId;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    @NotEmpty(message = "La solicitud de envío debe tener al menos un producto")
    private List<ItemDto> products;
}
