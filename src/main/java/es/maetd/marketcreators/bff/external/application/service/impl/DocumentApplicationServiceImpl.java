package es.maetd.marketcreators.bff.external.application.service.impl;

import es.maetd.marketcreators.bff.external.application.port.client.DocumentRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.DocumentApplicationService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.*;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.DocumentDataResponseDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.DocumentDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetDocumentsIDTO;
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
    public DocumentDataResponseDto getFiles(GetDocumentsIDTO idto) {
        return repositoryPort.getDocuments(idto);
    }

    @Override
    public DocumentDto getDocumentData(UUID fileId) {
        return repositoryPort.getDocumentData(fileId);
    }
}
