package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationResponseDto {

    private List<EvaluationDto> list;
}
