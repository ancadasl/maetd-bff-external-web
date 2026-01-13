package es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository;

import es.maetd.marketcreators.bff.external.application.port.ChartRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.dto.ChartDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.ChartResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetChartsIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.*;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.specification.*;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.ChartInfrastructureMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChartRepositoryAdapter implements ChartRepositoryPort {

    private final PrimaryChartRepository prRepository;

    private final SecondaryChartRepository scRepository;

    private final TotalChartRepository totalRepository;

    private final OverBiddingChartRepository overBiddingRepository;

    private final ChartInfrastructureMapper mapper;

    @Override
    public ChartResponseDTO getCharts(GetChartsIDTO idto) {

        Specification<PrimaryChartEntity> primaryFilters = setPrimaryChartFilters(idto);

        Specification<SecondaryChartEntity> secondaryFilters = setSecondaryChartFilters(idto);

        Specification<TotalChartEntity> totalFilters = setTotalChartFilters(idto);

        Specification<OverbiddingEntity> overBiddingFilters = setOverbiddingChartFilters(idto);

        List<ChartDTO> primary =
                prRepository.findAll(primaryFilters).stream().map(mapper::map).toList();

        List<ChartDTO> secondary =
                scRepository.findAll(secondaryFilters).stream().map(mapper::map).toList();

        List<ChartDTO> total =
                totalRepository.findAll(totalFilters).stream().map(mapper::map).toList();

        List<ChartDTO> overBidding = overBiddingRepository.findAll(overBiddingFilters).stream()
                .map(mapper::map)
                .toList();

        return ChartResponseDTO.builder()
                .primaryChartList(primary)
                .secondaryChartList(secondary)
                .totalChartList(total)
                .overBiddingChartList(overBidding)
                .build();
    }

    private Specification<PrimaryChartEntity> setPrimaryChartFilters(GetChartsIDTO idto) {
        return new PrimaryChartSpecification(idto.getProductType(), idto.getCsb(), idto.getYear());
    }

    private Specification<SecondaryChartEntity> setSecondaryChartFilters(GetChartsIDTO idto) {
        return new SecondaryChartSpecification(idto.getProductType(), idto.getCsb(), idto.getYear());
    }

    private Specification<TotalChartEntity> setTotalChartFilters(GetChartsIDTO idto) {
        return new TotalChartSpecification(idto.getCsb(), idto.getYear());
    }

    private Specification<OverbiddingEntity> setOverbiddingChartFilters(GetChartsIDTO idto) {
        return new OverBiddingSpecification(idto.getCsb(), idto.getYear(), idto.getProductType());
    }
}
