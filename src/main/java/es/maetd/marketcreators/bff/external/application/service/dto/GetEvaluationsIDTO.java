package es.maetd.marketcreators.bff.external.application.service.dto;

import es.maetd.marketcreators.bff.external.application.service.dto.types.EvaluationStatus;
import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetEvaluationsIDTO {

    private String csb;

    private Integer month;

    private Integer year;

    private ProductType productType;

    private List<EvaluationStatus> status;
}
