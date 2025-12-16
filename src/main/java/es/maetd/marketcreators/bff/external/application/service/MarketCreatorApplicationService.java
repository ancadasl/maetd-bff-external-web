package es.maetd.marketcreators.bff.external.application.service;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EntityDto;
import java.util.List;

public interface MarketCreatorApplicationService {

    List<EntityDto> getMarketCreators();
}
