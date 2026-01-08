package es.maetd.marketcreators.bff.external.application.service.impl;

import es.maetd.marketcreators.bff.external.application.port.ChartRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.ChartApplicationService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.ChartResponseDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetChartsIDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class ChartApplicationServiceImpl implements ChartApplicationService {

    private final ChartRepositoryPort port;

    @Override
    public ChartResponseDTO getCharts(GetChartsIDTO idto) {

        return port.getCharts(idto);
    }
}
