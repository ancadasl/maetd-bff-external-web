package es.maetd.marketcreators.bff.external.application.service.impl;

import es.maetd.marketcreators.bff.external.application.port.AuctionsRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.AuctionApplicationService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.AuctionsResponseDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetAuctionsIDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class AuctionsApplicationServiceImpl implements AuctionApplicationService {

    private final AuctionsRepositoryPort port;

    @Override
    public AuctionsResponseDto getAuctions(GetAuctionsIDTO idto) {

        AuctionsResponseDto response = port.getAuctions(idto);

        return AuctionsResponseDto.builder()
                .primary(response.getPrimary())
                .secondary(response.getSecondary())
                .build();
    }
}
