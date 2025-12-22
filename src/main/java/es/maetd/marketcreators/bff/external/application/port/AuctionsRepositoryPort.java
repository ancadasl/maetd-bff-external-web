package es.maetd.marketcreators.bff.external.application.port;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.AuctionsResponseDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetAuctionsIDTO;

public interface AuctionsRepositoryPort {

    AuctionsResponseDto getAuctions(GetAuctionsIDTO idto);
}
