package sube.interviews.mareoenvios.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopSendedResponseDto {
    private String productDescription;
    private Long totalSended;
}
