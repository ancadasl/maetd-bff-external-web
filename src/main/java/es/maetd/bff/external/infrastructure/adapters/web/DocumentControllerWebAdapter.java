package es.maetd.bff.external.infrastructure.adapters.web;

import es.maetd.bff.external.application.mappers.DocumentApplicationMapper;
import es.maetd.bff.external.application.service.DocumentApplicationService;
import es.maetd.bff.external.infrastructure.adapters.dto.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.DocumentsApi;
import org.openapitools.model.DocumentDataListWDTO;
import org.openapitools.model.DocumentDataWDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DocumentControllerWebAdapter implements DocumentsApi {

    private final DocumentApplicationService service;

    private final DocumentApplicationMapper mapper;

    public DocumentControllerWebAdapter(DocumentApplicationService service, DocumentApplicationMapper mapper) {

        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<DocumentDataListWDTO> getDocuments(
            Optional<Integer> page,
            Optional<Integer> size,
            Optional<LocalDate> creationDate,
            Optional<String> category,
            Optional<String> csb) {

        GetDocumentsIDTO idto = new GetDocumentsIDTO();
        idto.setPage(page.orElse(null));
        idto.setSize(size.orElse(null));
        idto.setCategory(getCategory(category.orElse(null)));
        idto.setCsb(csb.orElse(null));
        idto.setCreationDate(creationDate.orElse(null));

        DocumentDataListWDTO response = new DocumentDataListWDTO();
        DocumentDataResponseDto dto = service.getFiles(idto);
        response.setDocuments(mapper.map(dto.getDocumentDtoList()));
        response.setTotal(dto.getCount());

        return ResponseEntity.ok(response);
    }

    private String getCategory(@Valid String category) {
        DocumentDataWDTO.CategoryTypeEnum item = Arrays.stream(DocumentDataWDTO.CategoryTypeEnum.values())
                .filter(p -> p.getValue().equalsIgnoreCase(category))
                .findFirst()
                .orElse(null);
        return item != null ? item.getValue() : null;
    }
}
