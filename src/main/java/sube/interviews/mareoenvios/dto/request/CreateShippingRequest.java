package sube.interviews.mareoenvios.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sube.interviews.mareoenvios.dto.ItemDto;

import java.util.List;

@Data
public class CreateShippingRequest {
    private Integer customerId;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private boolean partialFulfillment;
    @NotNull
    private Integer priority;
    @NotEmpty(message = "La solicitud de envío debe tener al menos un producto")
    private List<ItemDto> products;
}
