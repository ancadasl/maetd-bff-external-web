package es.maetd.marketcreators.bff.external.application.port;

import es.maetd.marketcreators.bff.external.application.service.dto.GetSyndicatedIssuesIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.SyndicatedIssuesDTO;
import java.util.List;

public interface SyndicatedIssuesRepositoryPort {

    List<SyndicatedIssuesDTO> getSyndicatedIssues(GetSyndicatedIssuesIDTO idto);
}
