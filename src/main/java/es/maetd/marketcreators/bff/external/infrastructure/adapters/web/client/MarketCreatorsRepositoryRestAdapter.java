package es.maetd.marketcreators.bff.external.infrastructure.adapters.web.client;

import es.maetd.marketcreators.bff.external.application.port.client.MarketCreatorsPort;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EntityDTO;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.MarketCreatorInfrastructureMapper;
import es.maetd.marketcreators.clients.rest.domains.creators.model.MarketCreatorsList;
import es.maetd.marketcreators.clients.rest.exception.RestException;
import es.maetd.marketcreators.clients.rest.service.base.RestClient;
import es.maetd.marketcreators.clients.rest.service.dto.input.RequestIDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MarketCreatorsRepositoryRestAdapter implements MarketCreatorsPort {

    private final RestClient restClient;

    private final MarketCreatorInfrastructureMapper mapper;

    public MarketCreatorsRepositoryRestAdapter(
            @Qualifier("OAuth2RestClient") RestClient restClient, MarketCreatorInfrastructureMapper mapper) {
        this.restClient = restClient;
        this.mapper = mapper;
    }

    @Override
    public List<EntityDTO> getCreators() {
        RequestIDTO<Void, MarketCreatorsList> request = RequestIDTO.<Void, MarketCreatorsList>builder()
                .systemKey("CREATORS_MS")
                .urlKey("getCreators")
                .type(new ParameterizedTypeReference<>() {})
                .build();

        List<EntityDTO> list = null;
        try {
            ResponseEntity<MarketCreatorsList> response = restClient.execute(request);
            if (HttpStatus.OK.equals(response.getStatusCode())) {
                list = mapper.mapAsList(response.getBody().getData());
            }

        } catch (RestException rEx) {
            log.error("Error calling creators endpoint " + rEx.getMessage());
        }

        return list;
    }
}
