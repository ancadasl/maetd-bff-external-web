package es.maetd.marketcreators.bff.external.application.mappers;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.DocumentDto;
import es.maetd.marketcreators.ddd.core.events.domains.files.CategoryType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.openapitools.model.DocumentDataWDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DocumentApplicationMapper {

    List<DocumentDataWDTO> map(List<DocumentDto> documentData);

    DocumentDataWDTO map(DocumentDto model);

    default DocumentDataWDTO.CategoryTypeEnum mapCategoryEnum(CategoryType categoryType) {
        return Optional.ofNullable(categoryType)
                .flatMap(t -> Arrays.stream(DocumentDataWDTO.CategoryTypeEnum.values())
                        .filter(p -> p.name().equalsIgnoreCase(t.name()))
                        .findFirst())
                .orElse(null);
    }
}
