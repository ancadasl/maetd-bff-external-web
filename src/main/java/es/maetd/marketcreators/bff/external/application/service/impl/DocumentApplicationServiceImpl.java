package es.maetd.marketcreators.bff.external.application.service.impl;

import es.maetd.marketcreators.bff.external.application.port.client.DocumentRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.DocumentApplicationService;
import es.maetd.marketcreators.bff.external.application.service.dto.DocumentDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.DocumentDataResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetDocumentsIDTO;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class DocumentApplicationServiceImpl implements DocumentApplicationService {

    private final DocumentRepositoryPort repositoryPort;

    @Override
    public DocumentDataResponseDTO getFiles(GetDocumentsIDTO idto) {
        return repositoryPort.getDocuments(idto);
    }

    @Override
    public DocumentDTO getDocumentData(UUID fileId) {
        return repositoryPort.getDocumentData(fileId);
    }
}
