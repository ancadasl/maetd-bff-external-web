package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.service.dto.ChartResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetChartsIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.*;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.*;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.ChartInfrastructureMapper;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.ChartInfrastructureMapperImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ChartsRepositoryAdapterTest {

    public static final String CSB_123 = "CSB123";

    private static final int YEAR = 2022;

    @InjectMocks
    ChartRepositoryAdapter adapter;

    @Mock
    PrimaryChartRepository primaryChartRepository;

    @Mock
    SecondaryChartRepository secondaryChartRepository;

    @Mock
    TotalChartRepository totalChartRepository;

    @Mock
    OverBiddingChartRepository overBiddingChartRepository;

    @Mock
    ChartInfrastructureMapper mapper;

    private static ChartInfrastructureMapperImpl mapperImpl;

    @BeforeAll
    static void setUp() {
        mapperImpl = new ChartInfrastructureMapperImpl();
    }

    @Test
    void getChartsShould_return_a_list_of_charts() {

        // Given
        PrimaryChartEntity primaryChartEntity = createDummyPrimaryChartEntity();
        SecondaryChartEntity secondaryChartEntity = createDummySecondaryChartEntity();
        TotalChartEntity totalChartEntity = createDummyTotalChartEntity();
        OverbiddingEntity overbiddingEntity = createDummyOverbiddingEntity();

        // When
        when(primaryChartRepository.findAll(any(Specification.class))).thenReturn(List.of(primaryChartEntity));
        when(secondaryChartRepository.findAll(any(Specification.class))).thenReturn(List.of(secondaryChartEntity));
        when(totalChartRepository.findAll(any(Specification.class))).thenReturn(List.of(totalChartEntity));
        when(overBiddingChartRepository.findAll(any(Specification.class))).thenReturn(List.of(overbiddingEntity));
        when(mapper.map(any(PrimaryChartEntity.class))).thenReturn(mapperImpl.map(primaryChartEntity));
        when(mapper.map(any(SecondaryChartEntity.class))).thenReturn(mapperImpl.map(secondaryChartEntity));
        when(mapper.map(any(TotalChartEntity.class))).thenReturn(mapperImpl.map(totalChartEntity));
        when(mapper.map(any(OverbiddingEntity.class))).thenReturn(mapperImpl.map(overbiddingEntity));

        GetChartsIDTO idtoMock = GetChartsIDTO.builder()
                .year(YEAR)
                .csb(CSB_123)
                .productType(ProductType.BOND)
                .build();

        ChartResponseDTO result = adapter.getCharts(idtoMock);

        // Then
        assertEquals(result.getPrimaryChartList().get(0).getCsb(), primaryChartEntity.getCsb());
        assertEquals(result.getSecondaryChartList().get(0).getCsb(), secondaryChartEntity.getCsb());
        assertEquals(result.getTotalChartList().get(0).getCsb(), totalChartEntity.getCsb());
        assertEquals(result.getOverBiddingChartList().get(0).getCsb(), overbiddingEntity.getCsb());

        verify(primaryChartRepository, times(1)).findAll(any(Specification.class));
        verify(secondaryChartRepository, times(1)).findAll(any(Specification.class));
        verify(totalChartRepository, times(1)).findAll(any(Specification.class));
        verify(overBiddingChartRepository, times(1)).findAll(any(Specification.class));
        verify(mapper, times(1)).map(any(PrimaryChartEntity.class));
        verify(mapper, times(1)).map(any(SecondaryChartEntity.class));
        verify(mapper, times(1)).map(any(TotalChartEntity.class));
        verify(mapper, times(1)).map(any(OverbiddingEntity.class));
    }

    public static PrimaryChartEntity createDummyPrimaryChartEntity() {
        PrimaryChartEntity dto = new PrimaryChartEntity();
        dto.setId(1L);
        dto.setCsb(CSB_123);
        dto.setEvaluationYearMonth(LocalDateTime.of(2024, 1, 1, 0, 0));
        dto.setTotalPoints(85.5);
        dto.setMedia(8.55);
        dto.setProductType(ProductType.BOND);
        return dto;
    }

    public static SecondaryChartEntity createDummySecondaryChartEntity() {
        SecondaryChartEntity dto = new SecondaryChartEntity();
        dto.setId(3L);
        dto.setCsb(CSB_123);
        dto.setEvaluationYearMonth(LocalDateTime.of(2024, 3, 1, 0, 0));
        dto.setTotalPoints("92.3");
        dto.setMedia(9.23);
        dto.setProductType(ProductType.BOND);
        return dto;
    }

    public static TotalChartEntity createDummyTotalChartEntity() {
        TotalChartEntity dto = new TotalChartEntity();
        dto.setId(4L);
        dto.setCsb(CSB_123);
        dto.setEvaluationYearMonth(LocalDateTime.of(2024, 4, 1, 0, 0));
        dto.setTotalPoints(95.75);
        dto.setMedia(9.58);
        return dto;
    }

    public static OverbiddingEntity createDummyOverbiddingEntity() {
        OverbiddingEntity dto = new OverbiddingEntity();
        dto.setId(5L);
        dto.setDate(LocalDate.of(2024, 5, 1));
        dto.setYear(2024);
        dto.setMonth(5);
        dto.setCsb(CSB_123);
        dto.setPenalty(150.0);
        dto.setProductType(ProductType.BOND);
        dto.setAverage(75.5);
        return dto;
    }
}
