package es.maetd.marketcreators.bff.external.infrastructure.mappers;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EvaluationDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.EvaluationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface EvaluationInfrastructureMapper {

    @Mapping(source = "csb", target = "csb", qualifiedByName = "padCsb")
    EvaluationDto map(EvaluationEntity entity);

    EvaluationEntity map(EvaluationDto entity);

    @Named("padCsb")
    default String padCsb(Long csb) {
        return csb == null ? null : String.format("%04d", csb);
    }
}
