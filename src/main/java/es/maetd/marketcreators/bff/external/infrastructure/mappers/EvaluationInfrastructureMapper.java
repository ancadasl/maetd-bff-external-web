package es.maetd.marketcreators.bff.external.infrastructure.mappers;

import es.maetd.marketcreators.bff.external.application.service.dto.EvaluationDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.EvaluationJointDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.EvaluationEntity;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface EvaluationInfrastructureMapper {

    @Mapping(source = "csb", target = "csb", qualifiedByName = "padCsb")
    EvaluationDTO map(EvaluationEntity entity);

    EvaluationEntity map(EvaluationDTO entity);

    @Named("padCsb")
    default String padCsb(Long csb) {
        return csb == null ? null : String.format("%04d", csb);
    }

    default List<EvaluationJointDTO> mapAndGroupByYearAndMonth(List<EvaluationEntity> entities) {
        return entities.stream()
                .collect(Collectors.toMap(
                        e -> e.getYear() + "-" + e.getMonthNumeric() + "-" + e.getCsb(), // clave única por año-mes-csb
                        e -> {
                            // Mapeamos la entidad a DTO individual
                            EvaluationDTO dto = map(e);
                            return new EvaluationJointDTO(
                                    dto.getStatus(),
                                    dto.getCsb(),
                                    dto.getYear(),
                                    dto.getMonthNumeric(),
                                    dto.getJointMonthly(),
                                    dto.getCumulative(),
                                    dto.getLetter(),
                                    dto.getBonds());
                        },
                        (existing, replacement) -> {
                            // Combinamos los registros para no perder datos
                            Long jointMonthly = existing.getJointMonthly() != null
                                    ? existing.getJointMonthly()
                                    : replacement.getJointMonthly();
                            Long cumulative = existing.getCumulative() != null
                                    ? existing.getCumulative()
                                    : replacement.getCumulative();
                            Long letter = existing.getLetter() != null ? existing.getLetter() : replacement.getLetter();
                            Long bonds = existing.getBonds() != null ? existing.getBonds() : replacement.getBonds();

                            return new EvaluationJointDTO(
                                    existing.getStatus(),
                                    existing.getCsb(),
                                    existing.getYear(),
                                    existing.getMonthNumeric(),
                                    jointMonthly,
                                    cumulative,
                                    letter,
                                    bonds);
                        }))
                .values()
                .stream()
                .sorted(Comparator.comparing(EvaluationJointDTO::getYear)
                        .thenComparing(EvaluationJointDTO::getMonthNumeric))
                .toList();
    }
}
