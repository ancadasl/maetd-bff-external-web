package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository;

import es.maetd.marketcreators.bff.external.application.port.SyndicatedIssuesRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.dto.GetSyndicatedIssuesIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.SyndicatedIssuesDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.SyndicatedIssuesEntity;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification.SyndicatedIssueSpecification;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.SyndicatedIssuesInfrastructureMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SyndicatedIssuesRepositoryAdapter implements SyndicatedIssuesRepositoryPort {

    private final SyndicatedIssuesRepository repository;
    private final SyndicatedIssuesInfrastructureMapper mapper;

    @Override
    public List<SyndicatedIssuesDTO> getSyndicatedIssues(GetSyndicatedIssuesIDTO idto) {
        Specification<SyndicatedIssuesEntity> filters = setFilters(idto);
        return repository.findAll(filters).stream().map(mapper::map).toList();
    }

    private Specification<SyndicatedIssuesEntity> setFilters(GetSyndicatedIssuesIDTO idto) {
        return new SyndicatedIssueSpecification(idto.getCsb(), idto.getMonth(), idto.getYear());
    }
}
