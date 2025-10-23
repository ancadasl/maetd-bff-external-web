package es.maetd.bff.external.infrastructure.mappers;

import es.maetd.bff.external.infrastructure.adapters.dto.FileDto;
import es.maetd.marketcreators.clients.rest.domains.creators.model.MarketCreatorData.StatusEnum;
import es.maetd.marketcreators.clients.rest.domains.files.model.FileData;
import es.maetd.marketcreators.ddd.core.events.domains.files.FileType;
import es.maetd.marketcreators.ddd.core.events.domains.files.OriginSystemType;
import es.maetd.marketcreators.ddd.core.events.domains.status.FileStatusType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FileInfrastructureMapper {

    final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");

    List<FileDto> mapList(List<FileData> list);

    default FileType mapFileType(String type) {
        return List.of(FileType.values()).parallelStream()
                .filter(o -> o.name().equals(type))
                .findFirst()
                .orElse(null);
    }

    default OriginSystemType mapOriginSystemType(String origin) {
        return List.of(OriginSystemType.values()).parallelStream()
                .filter(o -> o.name().equals(origin))
                .findFirst()
                .orElse(null);
    }

    default FileStatusType map(StatusEnum status) {
        return List.of(FileStatusType.values()).stream()
                .filter(s -> s.name().equals(status.name()))
                .findFirst()
                .orElse(null);
    }

    default LocalDateTime stringDateToLocalDateTime(String date) {
        return LocalDateTime.parse(date.substring(0, 26), dateTimeFormatter);
    }
}
