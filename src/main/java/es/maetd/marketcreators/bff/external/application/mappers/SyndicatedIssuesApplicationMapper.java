package es.maetd.marketcreators.bff.external.application.mappers;

import es.maetd.marketcreators.bff.external.application.service.dto.SyndicatedIssuesDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.SyndicatedIssuesDataWDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SyndicatedIssuesApplicationMapper {

    List<SyndicatedIssuesDataWDTO> map(List<SyndicatedIssuesDTO> syndicatedIssuesDto);

    SyndicatedIssuesDataWDTO map(SyndicatedIssuesDTO model);
}
