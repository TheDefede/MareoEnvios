package sube.interviews.mareoenvios.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerResponseDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
}