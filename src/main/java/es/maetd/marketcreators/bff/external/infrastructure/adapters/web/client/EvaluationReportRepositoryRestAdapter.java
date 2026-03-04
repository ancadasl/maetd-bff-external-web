package es.maetd.marketcreators.bff.external.infrastructure.adapters.web.client;

import es.maetd.marketcreators.bff.external.application.port.client.BlindMarketPort;
import es.maetd.marketcreators.bff.external.application.service.dto.*;
import es.maetd.marketcreators.bff.external.application.service.dto.GetReportsIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.ReportInfrastructureMapper;
import es.maetd.marketcreators.clients.rest.domains.evaluations.model.ReportList;
import es.maetd.marketcreators.clients.rest.exception.RestException;
import es.maetd.marketcreators.clients.rest.service.base.RestClient;
import es.maetd.marketcreators.clients.rest.service.dto.input.RequestIDTO;
import es.maetd.marketcreators.clients.rest.util.KeysNamestore;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Service
public class EvaluationReportRepositoryRestAdapter implements BlindMarketPort {

    private final RestClient restClient;

    private final ReportInfrastructureMapper mapper;

    private static final String FIELD_ID = "creatorId";

    private static final String PAGE = "page";

    private static final String SIZE = "size";

    private static final String TYPE = "type";

    private static final String DAILY_CREATOR = "DAILY_CREATOR";

    private static final String YEAR = "year";

    private static final String MONTH = "month";

    public EvaluationReportRepositoryRestAdapter(
            @Qualifier("OAuth2RestClient") RestClient restClient, ReportInfrastructureMapper mapper) {
        this.restClient = restClient;
        this.mapper = mapper;
    }

    @Override
    public ReportResponseDTO getReports(GetReportsIDTO idto) {

        MultiValueMap<String, String> queryParams = getStringStringMultiValueMap(idto);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(FIELD_ID, String.valueOf(idto.getCreatorId()));

        RequestIDTO<Void, ReportList> requestIDTO = RequestIDTO.<Void, ReportList>builder()
                .systemKey(KeysNamestore.EVALUATION_MICROSERVICE.SYSTEM_KEY)
                .urlKey(KeysNamestore.EVALUATION_MICROSERVICE.URL_KEY_REPORTS_GET)
                .queryParams(queryParams)
                .type(new ParameterizedTypeReference<>() {})
                .build();

        ReportResponseDTO dto = ReportResponseDTO.builder()
                .list(Collections.emptyList())
                .count(0)
                .build();

        try {
            ResponseEntity<ReportList> response = restClient.execute(requestIDTO);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

                ReportList body = response.getBody();

                dto = ReportResponseDTO.builder()
                        .list(mapper.mapAsList(body.getReports()))
                        .count(body.getTotal())
                        .build();
            }

        } catch (RestException rEx) {
            log.error("Error calling getReports  {}", rEx.getMessage());
        }
        return dto;
    }

    private static @NotNull MultiValueMap<String, String> getStringStringMultiValueMap(GetReportsIDTO idto) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

        queryParams.add(PAGE, String.valueOf(idto.getPage()));
        queryParams.add(SIZE, String.valueOf(idto.getSize()));
        queryParams.add(TYPE, DAILY_CREATOR);
        queryParams.add(FIELD_ID, String.valueOf(idto.getCreatorId()));

        if (idto.getYear() != null) {
            queryParams.add(YEAR, String.valueOf(idto.getYear()));
            if (idto.getMonth() != null) {
                queryParams.add(MONTH, String.valueOf(idto.getMonth()));
            }
        }

        return queryParams;
    }
}
