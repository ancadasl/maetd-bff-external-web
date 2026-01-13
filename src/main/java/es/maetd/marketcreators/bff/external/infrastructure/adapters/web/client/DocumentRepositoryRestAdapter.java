package es.maetd.marketcreators.bff.external.infrastructure.adapters.web.client;

import es.maetd.marketcreators.bff.external.application.port.client.DocumentRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.dto.DocumentDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.DocumentDataResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetDocumentsIDTO;
import es.maetd.marketcreators.bff.external.exception.DocumentServiceException;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.DocumentInfrastructureMapper;
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
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Component
public class DocumentRepositoryRestAdapter implements DocumentRepositoryPort {

    private static final String FIELD_ID = "id";

    private static final String PAGE = "page";

    private static final String SIZE = "size";

    private static final String ORIGIN_SYSTEM = "originSystem";

    private static final String EXTERNAL_WEB = "externalWeb";

    private static final String CATEGORY = "categoryType";

    private static final String CSB = "csb";

    private static final String LANGUAGE = "language";

    private final RestClient restClient;

    private final DocumentInfrastructureMapper mapper;

    public DocumentRepositoryRestAdapter(
            @Qualifier("OAuth2RestClient") RestClient restClient, DocumentInfrastructureMapper mapper) {
        this.restClient = restClient;
        this.mapper = mapper;
    }

    @Override
    public DocumentDataResponseDTO getDocuments(GetDocumentsIDTO idto) {

        MultiValueMap<String, String> queryParams = getStringStringMultiValueMap(idto);

        RequestIDTO<Void, FileResponse> requestIDTO = RequestIDTO.<Void, FileResponse>builder()
                .systemKey(KeysNamestore.FILE_MICROSERVICE.SYSTEM_KEY)
                .urlKey(KeysNamestore.FILE_MICROSERVICE.URL_KEY_GET_FILES)
                .queryParams(queryParams)
                .type(new ParameterizedTypeReference<>() {})
                .build();

        DocumentDataResponseDTO dto = DocumentDataResponseDTO.builder()
                .list(Collections.emptyList())
                .count(0)
                .build();

        try {
            ResponseEntity<FileResponse> response = restClient.execute(requestIDTO);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {

                FileResponse body = response.getBody();

                List<DocumentDTO> documentsDtos = mapper.map(body.getFiles());

                dto = DocumentDataResponseDTO.builder()
                        .list(documentsDtos)
                        .count(body.getCountFiles())
                        .build();
            }

        } catch (RestException rEx) {
            log.error("Error calling getFileList  {}", rEx.getMessage());
        }

        return dto;
    }

    @Override
    public DocumentDTO getDocumentData(UUID fileId) {

        log.info("Recovering file binary data with id : {}", fileId);

        Map<String, String> pathParams = new HashMap<>();
        pathParams.put(FIELD_ID, String.valueOf(fileId));

        RequestIDTO<Void, byte[]> requestIDTO = RequestIDTO.<Void, byte[]>builder()
                .systemKey(KeysNamestore.FILE_MICROSERVICE.SYSTEM_KEY)
                .urlKey(KeysNamestore.FILE_MICROSERVICE.URL_KEY_GET_FILE_DATA)
                .pathParams(pathParams)
                .type(new ParameterizedTypeReference<>() {})
                .build();

        try {
            ResponseEntity<byte[]> response = restClient.execute(requestIDTO);

            if (response.getStatusCode().is2xxSuccessful()) {

                log.info("File found with id = {}", fileId);

                byte[] fileBytes = response.getBody();

                if (fileBytes == null) {
                    throw new DocumentServiceException("File not found in remote service");
                }
                String filename = response.getHeaders().getContentDisposition().getFilename();
                return DocumentDTO.builder()
                        .data(fileBytes)
                        .documentName(filename)
                        .build();
            } else {
                throw new DocumentServiceException(String.format(
                        "File id = %s not found. Service = %s , endpoint = %s",
                        fileId, requestIDTO.getSystemKey(), requestIDTO.getUrlKey()));
            }
        } catch (RestException ex) {
            throw new DocumentServiceException(String.format(
                    "REST resource not found. Service = %s , endpoint = %s",
                    requestIDTO.getSystemKey(), requestIDTO.getUrlKey()));
        }
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
        if (idto.getLanguage() != null) {
            queryParams.add(LANGUAGE, idto.getLanguage());
        }
        return queryParams;
    }
}
