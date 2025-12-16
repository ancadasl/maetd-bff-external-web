package es.maetd.marketcreators.bff.external.application.mappers;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.SyndicatedIssuesDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.SyndicatedIssuesDataWDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SyndicatedIssuesApplicationMapper {

    List<SyndicatedIssuesDataWDTO> map(List<SyndicatedIssuesDto> syndicatedIssuesDto);

    SyndicatedIssuesDataWDTO map(SyndicatedIssuesDto model);
}
