package es.maetd.marketcreators.bff.external.application.service.impl;

import es.maetd.marketcreators.bff.external.application.port.AuctionRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.AuctionApplicationService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.AuctionResponseDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetAuctionsIDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuctionApplicationServiceImpl implements AuctionApplicationService {

    private final AuctionRepositoryPort port;

    @Override
    public AuctionResponseDTO getAuctions(GetAuctionsIDTO idto) {

        return port.getAuctions(idto);
    }
}
