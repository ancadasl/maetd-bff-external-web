package es.maetd.marketcreators.bff.external.application.service.impl;

import es.maetd.marketcreators.bff.external.application.port.client.MarketCreatorsPort;
import es.maetd.marketcreators.bff.external.application.service.MarketCreatorApplicationService;
import es.maetd.marketcreators.bff.external.application.service.dto.EntityDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class MarketCreatorsApplicationServiceImpl implements MarketCreatorApplicationService {

    private final MarketCreatorsPort port;

    @Override
    public List<EntityDTO> getMarketCreators() {
        return port.getCreators().parallelStream()
                .filter(c -> c.getCsb() != null
                        && !c.getCsb().isEmpty()
                        && !c.getName().isEmpty())
                .toList();
    }
}
