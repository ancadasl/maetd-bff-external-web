package es.maetd.marketcreators.bff.external.application.service;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.ChartResponseDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetChartsIDTO;

public interface ChartApplicationService {

    ChartResponseDTO getCharts(GetChartsIDTO idto);
}
