package es.maetd.marketcreators.bff.external.application.service.impl;

import es.maetd.marketcreators.bff.external.application.port.SyndicatedIssuesRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.SyndicatedIssuesApplicationService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetSyndicatedIssuesIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.SyndicatedIssuesResponseDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class SyndicatedIssuesApplicationServiceImpl implements SyndicatedIssuesApplicationService {

    private final SyndicatedIssuesRepositoryPort port;

    @Override
    public SyndicatedIssuesResponseDto getSyndicatedIssues(GetSyndicatedIssuesIDTO idto) {

        return SyndicatedIssuesResponseDto.builder()
                .list(port.getSyndicatedIssues(idto))
                .build();
    }
}
