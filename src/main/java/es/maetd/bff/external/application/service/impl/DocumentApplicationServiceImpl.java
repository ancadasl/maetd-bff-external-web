package es.maetd.bff.external.application.service.impl;

import es.maetd.bff.external.application.port.client.DocumentRepositoryPort;
import es.maetd.bff.external.application.service.DocumentApplicationService;
import es.maetd.bff.external.infrastructure.adapters.dto.*;
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
        return repositoryPort.getFiles(idto);
    }
}
