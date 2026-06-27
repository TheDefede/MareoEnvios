package sube.interviews.mareoenvios.dto.response;

import lombok.Builder;
import lombok.Data;
import sube.interviews.mareoenvios.dto.CustomerDto;
import sube.interviews.mareoenvios.dto.ShippingItemDto;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ShippingResponseDto {
    private Integer id;
    private CustomerDto customer;
    private String state;
    private Instant sendDate;
    private Instant arriveDate;
    private Integer priority;
    private List<ShippingItemDto> items;
}