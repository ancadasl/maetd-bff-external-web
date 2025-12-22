package es.maetd.marketcreators.bff.external.application.mappers;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EvaluationJointDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.EvaluationJointDataWDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface EvaluationJointApplicationMapper {

    List<EvaluationJointDataWDTO> map(List<EvaluationJointDto> evaluationJointDto);

    EvaluationJointDataWDTO map(EvaluationJointDto model);
}
