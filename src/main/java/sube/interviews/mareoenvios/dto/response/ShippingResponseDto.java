package sube.interviews.mareoenvios.dto.response;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ShippingResponseDto {
    private Integer id;
    private CustomerResponseDto customer;
    private String state;
    private LocalDate sendDate;
    private LocalDate arriveDate;
    private Integer priority;
    private List<ShippingItemResponseDto> items;
}