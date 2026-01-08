package es.maetd.marketcreators.bff.external.application.port;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.ChartResponseDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetChartsIDTO;

public interface ChartRepositoryPort {

    ChartResponseDTO getCharts(GetChartsIDTO idto);
}
