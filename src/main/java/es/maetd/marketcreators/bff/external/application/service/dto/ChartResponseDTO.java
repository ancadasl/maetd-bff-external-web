package es.maetd.marketcreators.bff.external.application.service.dto;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChartResponseDTO {

    private List<ChartDTO> primaryChartList;

    private List<ChartDTO> secondaryChartList;

    private List<ChartDTO> totalChartList;

    private List<ChartDTO> overBiddingChartList;
}
