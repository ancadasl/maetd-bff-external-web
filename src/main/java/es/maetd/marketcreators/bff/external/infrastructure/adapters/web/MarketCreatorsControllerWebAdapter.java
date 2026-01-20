package es.maetd.marketcreators.bff.external.infrastructure.adapters.web;

import es.maetd.marketcreators.bff.external.application.mappers.MarketCreatorApplicationMapper;
import es.maetd.marketcreators.bff.external.application.service.MarketCreatorApplicationService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.MarketCreatorsApi;
import org.openapitools.model.MarketCreatorDataWDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class MarketCreatorsControllerWebAdapter implements MarketCreatorsApi {

    private final MarketCreatorApplicationService service;

    private final MarketCreatorApplicationMapper mapper;

    public MarketCreatorsControllerWebAdapter(
            MarketCreatorApplicationService service, MarketCreatorApplicationMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<List<MarketCreatorDataWDTO>> getMarketCreators() {
        log.info("Recovering all market creators");
        return ResponseEntity.ok(mapper.mapAsList(service.getMarketCreators()));
    }
}
