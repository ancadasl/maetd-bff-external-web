package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import com.auth0.jwt.interfaces.Claim;
import es.maetd.marketcreators.bff.external.application.mappers.EvaluationApplicationMapper;
import es.maetd.marketcreators.bff.external.application.mappers.EvaluationApplicationMapperImpl;
import es.maetd.marketcreators.bff.external.application.service.EvaluationApplicationService;
import es.maetd.marketcreators.bff.external.application.service.dto.EvaluationDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.EvaluationResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.GetEvaluationsIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.types.EvaluationStatus;
import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import es.maetd.marketcreators.bff.external.application.service.dto.types.Role;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.web.EvaluationControllerWebAdapter;
import es.maetd.marketcreators.common.security.jwks.validator.spring.interceptor.UserTokenContext;
import es.maetd.marketcreators.common.security.jwks.validator.spring.interceptor.UserTokenContextHolder;
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
import org.openapitools.model.EvaluationDataListWDTO;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class EvaluationControllerWebAdapterTest {

    public static final String CSB = "csb";
    public static final String NUMBER_CSB = "0128";

    @Mock
    private EvaluationApplicationService service;

    @Mock
    private EvaluationApplicationMapper mapper;

    @InjectMocks
    private EvaluationControllerWebAdapter controller;

    private static EvaluationApplicationMapper mapperImpl;

    @BeforeAll
    static void setUp() {
        mapperImpl = new EvaluationApplicationMapperImpl();

        Map<String, Claim> claims = Map.of(CSB, mock(Claim.class));

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
    void whenGetEvaluationListIsCalled_thenResponseShouldBeOk() {
        // given
        Optional<Integer> year = Optional.of(2021);
        Optional<Integer> month = Optional.of(15);

        Optional<String> product = Optional.of(ProductType.LETTER.name());
        Optional<String> csb = Optional.of(NUMBER_CSB);

        List<EvaluationStatus> statusList =
                List.of(EvaluationStatus.FINISH_CONSOLIDATED_FINISHED, EvaluationStatus.FINISH_CLOSED_FINISHED);

        List<String> statusStrings = statusList.stream().map(Enum::name).toList();

        Optional<List<String>> optionalStatus = Optional.of(statusStrings);

        EvaluationDTO dto = createDummyEvaluationDataDto();
        EvaluationResponseDTO resEvaluation =
                EvaluationResponseDTO.builder().list(List.of(dto)).build();

        // when
        when(service.getEvaluations(any(GetEvaluationsIDTO.class))).thenReturn(resEvaluation);
        when(mapper.map(anyList())).thenReturn(mapperImpl.map(resEvaluation.getList()));

        // then
        ResponseEntity<EvaluationDataListWDTO> response =
                controller.getEvaluations(product, csb, month, year, optionalStatus);

        Assertions.assertEquals(
                resEvaluation.getList().get(0).getCsb(),
                Objects.requireNonNull(response.getBody())
                        .getEvaluations()
                        .get(0)
                        .getCsb());

        verify(service, times(1)).getEvaluations(any(GetEvaluationsIDTO.class));
        verify(mapper, times(1)).map(anyList());
    }

    public static EvaluationDTO createDummyEvaluationDataDto() {
        EvaluationDTO dto = new EvaluationDTO();

        dto.setEvaluationId(1L);
        dto.setStatus(EvaluationStatus.FINISH_CONSOLIDATED_FINISHED);
        dto.setCsb("128");
        dto.setYear(2023L);
        dto.setMonth("enero");
        dto.setMonthNumeric(1L);
        dto.setJointMonthly(15L);
        dto.setCumulative(15L);
        dto.setLetter(10L);
        dto.setBonds(5L);
        dto.setSubscription(12L);
        dto.setQuoting(8L);
        dto.setTrading(10L);
        dto.setElectronic(7L);
        dto.setTier(9L);
        dto.setClients(20L);
        dto.setStrips(5L);
        dto.setQualitative(15L);
        dto.setSecondRound(20.0);
        dto.setProductType(ProductType.LETTER);

        return dto;
    }
}
