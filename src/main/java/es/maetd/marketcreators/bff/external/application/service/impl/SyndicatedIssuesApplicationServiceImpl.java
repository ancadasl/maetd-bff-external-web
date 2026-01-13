package es.maetd.marketcreators.bff.external.application.service.impl;

import es.maetd.marketcreators.bff.external.application.port.SyndicatedIssuesRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.SyndicatedIssuesApplicationService;
import es.maetd.marketcreators.bff.external.application.service.dto.GetSyndicatedIssuesIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.SyndicatedIssuesResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SyndicatedIssuesApplicationServiceImpl implements SyndicatedIssuesApplicationService {

    private final SyndicatedIssuesRepositoryPort port;

    @Override
    public SyndicatedIssuesResponseDTO getSyndicatedIssues(GetSyndicatedIssuesIDTO idto) {

        return SyndicatedIssuesResponseDTO.builder()
                .list(port.getSyndicatedIssues(idto))
                .build();
    }
}
