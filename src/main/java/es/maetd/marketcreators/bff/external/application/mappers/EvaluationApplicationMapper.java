package es.maetd.marketcreators.bff.external.application.mappers;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EvaluationDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.EvaluationDataWDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface EvaluationApplicationMapper {

    List<EvaluationDataWDTO> map(List<EvaluationDto> evaluationData);

    EvaluationDataWDTO map(EvaluationDto model);
}
