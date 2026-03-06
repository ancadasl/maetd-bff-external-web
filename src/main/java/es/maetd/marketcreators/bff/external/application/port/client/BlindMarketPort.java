package es.maetd.marketcreators.bff.external.application.port.client;

import es.maetd.marketcreators.bff.external.application.service.dto.GetReportsIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.ReportDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.ReportResponseDTO;

/**
 * Interface for communication with services related to the ms-market-blind microservice.
 */
public interface BlindMarketPort {

    /**
     * Retrieves all creator reports from the market creators service.
     *
     * @return a list of {@link ReportDTO} representing the reports of all creators
     */
    ReportResponseDTO getReports(GetReportsIDTO idto);
}
