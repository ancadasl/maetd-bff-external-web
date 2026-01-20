package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import com.auth0.jwt.interfaces.Claim;
import es.maetd.marketcreators.bff.external.application.mappers.ChartApplicationMapper;
import es.maetd.marketcreators.bff.external.application.mappers.ChartApplicationMapperImpl;
import es.maetd.marketcreators.bff.external.application.service.ChartApplicationService;
import es.maetd.marketcreators.bff.external.application.service.dto.ChartDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.ChartResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetChartsIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import es.maetd.marketcreators.bff.external.application.service.dto.types.Role;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.web.ChartControllerWebAdapter;
import es.maetd.marketcreators.common.security.jwks.validator.spring.interceptor.UserTokenContext;
import es.maetd.marketcreators.common.security.jwks.validator.spring.interceptor.UserTokenContextHolder;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.model.ChartsDataGroupWDTO;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ChartControllerWebAdapterTest {

    @Mock
    private ChartApplicationService service;

    @Mock
    private ChartApplicationMapper mapper;

    @InjectMocks
    private ChartControllerWebAdapter controller;

    private static ChartApplicationMapper mapperImpl;

    public static final String CSB = "csb";
    public static final String NUMBER_CSB = "0128";
    static Map<String, Claim> claims = Map.of(CSB, mock(Claim.class));

    @BeforeAll
    static void setUp() {
        mapperImpl = new ChartApplicationMapperImpl();

        when(claims.get(CSB).asString()).thenReturn(NUMBER_CSB);
        when(claims.get(CSB).isNull()).thenReturn(false);

        // Crear UserTokenContext
        UserTokenContext testContext = new UserTokenContext(
                "superuser", List.of(Role.EVALUATION_ACCESS.getValue(), Role.IS_ADMIN.getValue()), claims);

        UserTokenContextHolder.set(testContext);
    }

    @AfterEach
    void cleanup() {
        UserTokenContextHolder.clear();
    }

    @Test
    void whenGetChartsListIsCalled_thenResponseShouldBeOk() {

        // given
        Optional<Integer> year = Optional.of(2021);
        Optional<String> productType = Optional.of(ProductType.BOND.name());
        Optional<String> csb = Optional.of(NUMBER_CSB);

        ChartDTO dto = createDummyChartDTO();
        ChartResponseDTO resCharts = ChartResponseDTO.builder()
                .primaryChartList(List.of(dto))
                .secondaryChartList(List.of(dto))
                .totalChartList(List.of(dto))
                .overBiddingChartList(List.of(dto))
                .build();

        // when
        when(service.getCharts(any(GetChartsIDTO.class))).thenReturn(resCharts);
        when(mapper.map(anyList())).thenReturn(mapperImpl.map(resCharts.getPrimaryChartList()));

        // then
        ResponseEntity<ChartsDataGroupWDTO> response = controller.getCharts(productType, csb, year);

        Assertions.assertEquals(
                resCharts.getPrimaryChartList().get(0).getCsb(),
                Objects.requireNonNull(response.getBody())
                        .getPrimaryCharts()
                        .get(0)
                        .getCsb());

        verify(service, times(1)).getCharts(any(GetChartsIDTO.class));
        verify(mapper, times(4)).map(anyList());
        verify(claims.get(CSB), times(1)).asString();
        verify(claims.get(CSB), times(1)).isNull();
    }

    public static ChartDTO createDummyChartDTO() {
        ChartDTO dto = new ChartDTO();
        dto.setId(1L);
        dto.setCsb("CSB001");
        dto.setMonth(1);
        dto.setEvaluationYearMonth(LocalDateTime.of(2024, 1, 1, 0, 0));
        dto.setTotalPoints(85.5);
        dto.setMedia(8.55);
        dto.setProductType(ProductType.BOND);
        return dto;
    }
}
