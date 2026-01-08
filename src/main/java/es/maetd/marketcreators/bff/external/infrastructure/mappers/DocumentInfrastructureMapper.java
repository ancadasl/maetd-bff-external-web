package es.maetd.marketcreators.bff.external.infrastructure.mappers;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.DocumentDTO;
import es.maetd.marketcreators.clients.rest.domains.files.model.ExternalWebFileData;
import es.maetd.marketcreators.clients.rest.domains.files.model.FileData;
import es.maetd.marketcreators.clients.rest.domains.files.model.FileList;
import es.maetd.marketcreators.ddd.core.events.domains.files.CategoryType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DocumentInfrastructureMapper {

    @Mapping(target = "documentName", source = "filename")
    @Mapping(target = "categoryType", source = "externalWebFileData.categoryType")
    @Mapping(target = "csb", source = "externalWebFileData.csb")
    @Mapping(target = "description", source = "externalWebFileData.description")
    DocumentDTO map(FileData fileData);

    List<DocumentDTO> map(List<FileData> fileDataList);

    @Named("mapDocumentsListToList")
    default List<DocumentDTO> map(FileList files) {
        if (files == null || files.getData() == null || files.getData().isEmpty()) {
            return Collections.emptyList();
        }
        return files.getData().stream().map(this::map).toList();
    }

    default CategoryType mapCategoryEnum(ExternalWebFileData.CategoryTypeEnum categoryEnum) {
        return Optional.ofNullable(categoryEnum)
                .flatMap(e -> Arrays.stream(CategoryType.values())
                        .filter(p -> p.name().equalsIgnoreCase(e.name()))
                        .findFirst())
                .orElse(null);
    }
}
