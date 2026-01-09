package es.maetd.marketcreators.bff.external.application.service;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.ChartResponseDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetChartsIDTO;

/**
 * Application service responsible for handling chart-related use cases.
 * <p>
 * This service acts as an entry point to retrieve chart information from
 * the external layer, orchestrating the necessary application logic.
 * </p>
 */
public interface ChartApplicationService {

    /**
     * Retrieves chart data based on the provided input parameters.
     *
     * @param idto input DTO containing the criteria required to obtain the charts
     * @return a {@link ChartResponseDTO} containing the chart data
     */
    ChartResponseDTO getCharts(GetChartsIDTO idto);
}
