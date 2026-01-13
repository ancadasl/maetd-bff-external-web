package es.maetd.marketcreators.bff.external.application.port;

import es.maetd.marketcreators.bff.external.application.service.dto.ChartResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetChartsIDTO;

public interface ChartRepositoryPort {

    ChartResponseDTO getCharts(GetChartsIDTO idto);
}
