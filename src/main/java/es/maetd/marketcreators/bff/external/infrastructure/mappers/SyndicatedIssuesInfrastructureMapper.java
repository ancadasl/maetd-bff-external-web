package es.maetd.marketcreators.bff.external.infrastructure.mappers;

import es.maetd.marketcreators.bff.external.application.service.dto.SyndicatedIssuesDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.SyndicatedIssuesEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface SyndicatedIssuesInfrastructureMapper {

    @Mapping(source = "month", target = "month", qualifiedByName = "monthToNumber")
    @Mapping(source = "li", target = "letter")
    SyndicatedIssuesDTO map(SyndicatedIssuesEntity entity);

    SyndicatedIssuesEntity map(SyndicatedIssuesDTO dto);

    @Named("monthToNumber")
    default int monthToNumber(String month) {
        return switch (month.toUpperCase()) {
            case "ENERO" -> 1;
            case "FEBRERO" -> 2;
            case "MARZO" -> 3;
            case "ABRIL" -> 4;
            case "MAYO" -> 5;
            case "JUNIO" -> 6;
            case "JULIO" -> 7;
            case "AGOSTO" -> 8;
            case "SEPTIEMBRE" -> 9;
            case "OCTUBRE" -> 10;
            case "NOVIEMBRE" -> 11;
            case "DICIEMBRE" -> 12;
            default -> throw new IllegalArgumentException("Mes no válido: " + month);
        };
    }
}
