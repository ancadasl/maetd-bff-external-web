package es.maetd.marketcreators.bff.external.application.service;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetSyndicatedIssuesIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.SyndicatedIssuesResponseDto;

public interface SyndicatedIssuesApplicationService {

    SyndicatedIssuesResponseDto getSyndicatedIssues(GetSyndicatedIssuesIDTO idto);
}
