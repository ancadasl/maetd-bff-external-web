package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuctionResponseDTO {

    private AuctionDTO primary;

    private AuctionDTO secondary;
}
