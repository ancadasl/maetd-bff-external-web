package es.maetd.bff.external.infrastructure.adapters.web.client;

import es.maetd.bff.external.application.port.client.FilesRepositoryPort;
import es.maetd.bff.external.infrastructure.adapters.dto.FileDto;
import es.maetd.bff.external.infrastructure.mappers.FileInfrastructureMapper;
import es.maetd.marketcreators.clients.rest.domains.files.model.FileData;
import es.maetd.marketcreators.clients.rest.exception.RestException;
import es.maetd.marketcreators.clients.rest.service.base.RestClient;
import es.maetd.marketcreators.clients.rest.service.dto.input.RequestIDTO;
import es.maetd.marketcreators.clients.rest.util.KeysNamestore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FilesRepositoryRestAdapter implements FilesRepositoryPort {

    private static final String FIELD_ID = "id";

    private RestClient restClient;

    private FileInfrastructureMapper mapper;

    public FilesRepositoryRestAdapter(
            @Qualifier("OAuth2RestClient") RestClient restClient, FileInfrastructureMapper mapper) {
        this.restClient = restClient;
        this.mapper = mapper;
    }

    @Override
    public List<FileDto> getFiles() {

        Map<String, String> pathParams = new HashMap<>();
        /// pathParams.put(FIELD_ID, fileId); tengo que meter el falg de solo cana web extrna

        RequestIDTO<Void, List<FileData>> requestIDTO = RequestIDTO.<Void, List<FileData>>builder()
                .systemKey(KeysNamestore.FILE_MICROSERVICE.SYSTEM_KEY)
                .urlKey(KeysNamestore.FILE_MICROSERVICE.URL_KEY_GET_FILES)
                .pathParams(pathParams)
                .type(new ParameterizedTypeReference<>() {})
                .build();
        List<FileDto> list = null;
        try {
            ResponseEntity<List<FileData>> response = restClient.execute(requestIDTO);

            if (response.getStatusCode().is2xxSuccessful()) {
                list = mapper.mapList(response.getBody());
            }
        } catch (RestException rEx) {
            log.error("Error calling getFileList  {}", rEx.getMessage());
        }
        return list;
    }
}
