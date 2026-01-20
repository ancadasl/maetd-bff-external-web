package es.maetd.marketcreators.bff.external.application.port.client;

import es.maetd.marketcreators.bff.external.application.service.dto.DocumentDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.DocumentDataResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetDocumentsIDTO;
import java.util.UUID;

public interface DocumentRepositoryPort {

    DocumentDataResponseDTO getDocuments(GetDocumentsIDTO idto);

    DocumentDTO getDocumentData(UUID fileId);
}
