package es.maetd.marketcreators.bff.external.application.port;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetSyndicatedIssuesIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.SyndicatedIssuesDto;
import java.util.List;

public interface SyndicatedIssuesRepositoryPort {

    List<SyndicatedIssuesDto> getSyndicatedIssues(GetSyndicatedIssuesIDTO idto);
}
