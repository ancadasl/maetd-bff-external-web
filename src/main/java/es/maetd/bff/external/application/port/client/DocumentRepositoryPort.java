package es.maetd.bff.external.application.port.client;

import es.maetd.bff.external.infrastructure.adapters.dto.DocumentDataResponseDto;
import es.maetd.bff.external.infrastructure.adapters.dto.GetDocumentsIDTO;

public interface DocumentRepositoryPort {

    DocumentDataResponseDto getFiles(GetDocumentsIDTO idto);
}
