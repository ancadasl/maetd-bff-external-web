package es.maetd.marketcreators.bff.external.infrastructure.adapters.web;

import es.maetd.marketcreators.bff.external.application.mappers.ChartApplicationMapper;
import es.maetd.marketcreators.bff.external.application.service.ChartApplicationService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.ChartResponseDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetChartsIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.ChartsApi;
import org.openapitools.model.ChartsDataGroupWDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ChartControllerWebAdapter extends BaseController implements ChartsApi {

    private final ChartApplicationService service;

    private final ChartApplicationMapper mapper;

    public ChartControllerWebAdapter(ChartApplicationService service, ChartApplicationMapper mapper) {

        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public ResponseEntity<ChartsDataGroupWDTO> getCharts(
            Optional<String> productType, Optional<String> csb, Optional<Integer> year) {
        log.info(
                "Get charts for productType: {} , csb: {} , year: {}}.",
                productType.orElse(null),
                csb.orElse(null),
                year.orElse(null));

        doValidateCsbPermission(csb.orElse(null));

        GetChartsIDTO idto = GetChartsIDTO.builder()
                .productType(productType.map(ProductType::fromString).orElse(null))
                .csb(csb.orElse(null))
                .year(year.orElse(null))
                .build();

        ChartResponseDTO dto = service.getCharts(idto);

        ChartsDataGroupWDTO response = new ChartsDataGroupWDTO();

        response.setPrimaryCharts(mapper.map(dto.getPrimaryChartList()));

        response.setSecondaryCharts(mapper.map(dto.getSecondaryChartList()));

        response.setTotalCharts(mapper.map(dto.getTotalChartList()));

        response.setOverBiddingCharts(mapper.map(dto.getOverBiddingChartList()));

        return ResponseEntity.ok(response);
    }
}
