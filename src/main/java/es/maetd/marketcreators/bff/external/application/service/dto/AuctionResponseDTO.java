package es.maetd.marketcreators.bff.external.application.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuctionResponseDTO {

    private AuctionDTO primary;

    private AuctionDTO secondary;
}
