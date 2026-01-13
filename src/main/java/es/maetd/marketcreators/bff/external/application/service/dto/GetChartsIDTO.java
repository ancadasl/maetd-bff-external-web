package es.maetd.marketcreators.bff.external.application.service.dto;

import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetChartsIDTO {

    private String csb;

    private Integer year;

    private ProductType productType;
}
