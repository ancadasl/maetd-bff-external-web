package es.maetd.marketcreators.bff.external.application.port.client;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.DocumentDataResponseDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.DocumentDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetDocumentsIDTO;
import java.util.UUID;

public interface DocumentRepositoryPort {

    DocumentDataResponseDto getDocuments(GetDocumentsIDTO idto);

    DocumentDto getDocumentData(UUID fileId);
}
