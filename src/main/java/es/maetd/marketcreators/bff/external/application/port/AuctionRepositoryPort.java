package es.maetd.marketcreators.bff.external.application.port;

import es.maetd.marketcreators.bff.external.application.service.dto.AuctionResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetAuctionsIDTO;

public interface AuctionRepositoryPort {

    AuctionResponseDTO getAuctions(GetAuctionsIDTO idto);
}
