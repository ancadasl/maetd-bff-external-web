package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetAuctionsIDTO {

    private String csb;

    private Integer month;

    private Integer year;

    private ProductType productType;
}
