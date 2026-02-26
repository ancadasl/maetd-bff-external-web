package es.maetd.marketcreators.bff.external.infrastructure.mappers;

import es.maetd.marketcreators.bff.external.application.service.dto.ReportDTO;
import es.maetd.marketcreators.clients.rest.domains.blind.model.FileData;
import es.maetd.marketcreators.sftp.client.service.dto.output.FileDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReportInfrastructureMapper {

    List<ReportDTO> map(List<FileData> fileData);

    ReportDTO map(FileDTO fileData);
}
