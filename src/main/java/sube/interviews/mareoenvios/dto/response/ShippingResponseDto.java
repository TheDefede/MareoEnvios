package sube.interviews.mareoenvios.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ShippingResponseDto {
    private Integer id;
    private CustomerResponseDto customer;
    private String state;
    private Instant sendDate;
    private Instant arriveDate;
    private Integer priority;
    private List<ShippingItemResponseDto> items;
}