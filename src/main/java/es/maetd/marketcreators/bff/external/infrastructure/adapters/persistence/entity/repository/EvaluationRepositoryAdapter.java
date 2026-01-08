package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository;

import es.maetd.marketcreators.bff.external.application.port.EvaluationRepositoryPort;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EvaluationDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EvaluationJointDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetEvaluationsIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.EvaluationEntity;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification.EvaluationSpecification;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.EvaluationInfrastructureMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EvaluationRepositoryAdapter implements EvaluationRepositoryPort {

    private final EvaluationRepository repository;
    private final EvaluationInfrastructureMapper mapper;

    @Override
    public List<EvaluationDTO> getEvaluations(GetEvaluationsIDTO idto) {
        Specification<EvaluationEntity> filters = setFilters(idto);
        return repository.findAll(filters).stream().map(mapper::map).toList();
    }

    @Override
    public List<EvaluationJointDTO> getEvaluationsJoint(GetEvaluationsIDTO idto) {
        Specification<EvaluationEntity> filters = setFilters(idto);
        List<EvaluationEntity> entities = repository.findAll(filters);
        return mapper.mapAndGroupByYearAndMonth(entities);
    }

    private Specification<EvaluationEntity> setFilters(GetEvaluationsIDTO idto) {
        return new EvaluationSpecification(
                idto.getProductType(), idto.getCsb(), idto.getMonth(), idto.getYear(), idto.getStatus());
    }
}
