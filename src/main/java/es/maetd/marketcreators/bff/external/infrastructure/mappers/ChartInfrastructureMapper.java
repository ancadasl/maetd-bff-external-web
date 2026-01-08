package es.maetd.marketcreators.bff.external.infrastructure.mappers;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.ChartDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.*;
import java.time.LocalDateTime;
import java.util.Optional;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ChartInfrastructureMapper {

    @Mapping(target = "month", source = "evaluationYearMonth")
    ChartDTO map(PrimaryChartEntity primaryChartEntity);

    @Mapping(target = "month", source = "evaluationYearMonth")
    ChartDTO map(SecondaryChartEntity secondaryChartEntity);

    @Mapping(target = "month", source = "evaluationYearMonth")
    ChartDTO map(TotalChartEntity totalChartEntity);

    @Mapping(source = "penalty", target = "totalPoints")
    @Mapping(source = "average", target = "media")
    ChartDTO map(OverbiddingEntity totalChartEntity);

    default Integer map(LocalDateTime evaluationYearMonth) {
        return Optional.ofNullable(evaluationYearMonth)
                .map(LocalDateTime::getMonthValue)
                .orElse(null);
    }
}
