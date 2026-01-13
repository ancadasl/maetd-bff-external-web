package es.maetd.marketcreators.bff.external.test.application.service.impl;

import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.port.ChartRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.dto.ChartDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.ChartResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetChartsIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import es.maetd.marketcreators.bff.external.application.service.impl.ChartApplicationServiceImpl;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChartApplicationServiceImplTest {

    public static final String CSB_123 = "0128";

    private static final int YEAR = 2022;

    @Mock
    private ChartRepositoryPort port;

    private ChartApplicationServiceImpl service;

    @BeforeEach
    void init() {
        service = new ChartApplicationServiceImpl(port);
    }

    @Test
    void getChartShould_return_a_list_of_charts() {

        // Given
        ChartDTO chartDTO = createDummyChartDTO();
        ChartResponseDTO responseDTO = ChartResponseDTO.builder()
                .primaryChartList(List.of(chartDTO))
                .secondaryChartList(List.of(chartDTO))
                .totalChartList(List.of(chartDTO))
                .overBiddingChartList(List.of(chartDTO))
                .build();

        GetChartsIDTO getChartsIDTO = GetChartsIDTO.builder()
                .productType(ProductType.BOND)
                .year(YEAR)
                .csb(CSB_123)
                .build();

        // When
        when(port.getCharts(any(GetChartsIDTO.class))).thenReturn(responseDTO);

        ChartResponseDTO result = service.getCharts(getChartsIDTO);

        // Then
        Assertions.assertEquals(result.getPrimaryChartList().get(0).getCsb(), chartDTO.getCsb());
        Assertions.assertEquals(result.getSecondaryChartList().get(0).getCsb(), chartDTO.getCsb());
        Assertions.assertEquals(result.getTotalChartList().get(0).getCsb(), chartDTO.getCsb());
        Assertions.assertEquals(result.getOverBiddingChartList().get(0).getCsb(), chartDTO.getCsb());

        verify(port, times(1)).getCharts(any(GetChartsIDTO.class));
    }

    public static ChartDTO createDummyChartDTO() {
        ChartDTO dto = new ChartDTO();
        dto.setId(1L);
        dto.setCsb(CSB_123);
        dto.setMonth(1);
        dto.setEvaluationYearMonth(LocalDateTime.of(2024, 1, 1, 0, 0));
        dto.setTotalPoints(85.5);
        dto.setMedia(8.55);
        dto.setProductType(ProductType.BOND);
        return dto;
    }
}
