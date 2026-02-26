package es.maetd.marketcreators.bff.external.application.service.impl;

import es.maetd.marketcreators.bff.external.application.port.client.BlindMarketPort;
import es.maetd.marketcreators.bff.external.application.service.ReportApplicationService;
import es.maetd.marketcreators.bff.external.application.service.dto.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ReportApplicationServiceImpl implements ReportApplicationService {

    private final BlindMarketPort repositoryPort;

    @Override
    public ReportResponseDTO getReports(GetReportsIDTO idto) {
        return repositoryPort.getReports(idto);
    }
}
