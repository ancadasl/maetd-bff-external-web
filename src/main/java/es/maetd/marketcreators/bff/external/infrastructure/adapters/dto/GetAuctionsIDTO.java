package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAuctionsIDTO {

    private String csb;

    private Integer month;

    private Integer year;

    private ProductType productType;
}
