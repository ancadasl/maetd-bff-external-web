package es.maetd.marketcreators.bff.external.application.service;

import es.maetd.marketcreators.bff.external.application.service.dto.GetReportsIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.ReportResponseDTO;

public interface ReportApplicationService {
    ReportResponseDTO getReports(GetReportsIDTO idto);
}
