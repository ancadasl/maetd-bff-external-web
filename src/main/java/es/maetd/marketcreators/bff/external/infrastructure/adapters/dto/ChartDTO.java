package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ChartDTO {

    private Long id;

    private String csb;

    private Integer month;

    private LocalDateTime evaluationYearMonth;

    private Double totalPoints;

    private Double media;

    private ProductType productType;
}
