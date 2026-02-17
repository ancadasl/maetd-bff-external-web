package es.maetd.marketcreators.bff.external.application.mappers;

import es.maetd.marketcreators.bff.external.application.service.dto.ReportDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.ReportDataWDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReportApplicationMapper {

    List<ReportDataWDTO> map(List<ReportDTO> fileData);

    ReportDataWDTO map(ReportDTO fileData);
}
