package es.maetd.bff.external.infrastructure.adapters.web.client;

import es.maetd.bff.external.application.port.client.DocumentRepositoryPort;
import es.maetd.bff.external.infrastructure.adapters.dto.DocumentDataResponseDto;
import es.maetd.bff.external.infrastructure.adapters.dto.DocumentDto;
import es.maetd.bff.external.infrastructure.adapters.dto.GetDocumentsIDTO;
import es.maetd.bff.external.infrastructure.mappers.DocumentInfrastructureMapper;
import es.maetd.marketcreators.clients.rest.domains.files.model.FileResponse;
import es.maetd.marketcreators.clients.rest.exception.RestException;
import es.maetd.marketcreators.clients.rest.service.base.RestClient;
import es.maetd.marketcreators.clients.rest.service.dto.input.RequestIDTO;
import es.maetd.marketcreators.clients.rest.util.KeysNamestore;
import es.maetd.marketcreators.ddd.core.events.domains.files.OriginSystemType;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Component
public class DocumentRepositoryRestAdapter implements DocumentRepositoryPort {

    private static final String PAGE = "page";

    private static final String SIZE = "size";

    private static final String ORIGIN_SYSTEM = "originSystem";

    private static final String EXTERNAL_WEB = "externalWeb";

    private static final String CATEGORY = "categoryType";

    private static final String CSB = "csb";

    private RestClient restClient;

    private DocumentInfrastructureMapper mapper;

    public DocumentRepositoryRestAdapter(
            @Qualifier("OAuth2RestClient") RestClient restClient, DocumentInfrastructureMapper mapper) {
        this.restClient = restClient;
        this.mapper = mapper;
    }

    @Override
    public DocumentDataResponseDto getFiles(GetDocumentsIDTO idto) {

        MultiValueMap<String, String> queryParams = getStringStringMultiValueMap(idto);

        RequestIDTO<Void, FileResponse> requestIDTO = RequestIDTO.<Void, FileResponse>builder()
                .systemKey(KeysNamestore.FILE_MICROSERVICE.SYSTEM_KEY)
                .urlKey(KeysNamestore.FILE_MICROSERVICE.URL_KEY_GET_FILES)
                .queryParams(queryParams)
                .type(new ParameterizedTypeReference<>() {})
                .build();
        DocumentDataResponseDto dto = new DocumentDataResponseDto();
        try {
            ResponseEntity<FileResponse> response = restClient.execute(requestIDTO);

            if (response.getStatusCode().is2xxSuccessful()) {
                Optional.of(response)
                        .filter(r -> r.getStatusCode().is2xxSuccessful())
                        .map(HttpEntity::getBody)
                        .ifPresent(body -> {
                            List<DocumentDto> documentsDtos = mapper.map(body.getFiles());
                            dto.setDocumentDtoList(documentsDtos);
                            dto.setCount(body.getCountFiles());
                        });
            }
        } catch (RestException rEx) {
            log.error("Error calling getFileList  {}", rEx.getMessage());
        }
        return dto;
    }

    private static @NotNull MultiValueMap<String, String> getStringStringMultiValueMap(GetDocumentsIDTO idto) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        boolean externalWeb = true;

        queryParams.add(PAGE, String.valueOf(idto.getPage()));
        queryParams.add(SIZE, String.valueOf(idto.getSize()));
        queryParams.add(EXTERNAL_WEB, String.valueOf(externalWeb));
        queryParams.add(ORIGIN_SYSTEM, OriginSystemType.EXTERNAL_WEB.name());

        if (idto.getCategory() != null) {
            queryParams.add(CATEGORY, idto.getCategory());
        }
        if (idto.getCsb() != null) {
            queryParams.add(CSB, idto.getCsb());
        }
        return queryParams;
    }
}
