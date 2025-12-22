package es.maetd.marketcreators.bff.external.infrastructure.adapters.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuctionsResponseDto {

    private AuctionDto primary;

    private AuctionDto secondary;
}
