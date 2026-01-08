package es.maetd.marketcreators.bff.external.application.port;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.AuctionResponseDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetAuctionsIDTO;

public interface AuctionRepositoryPort {

    AuctionResponseDTO getAuctions(GetAuctionsIDTO idto);
}
