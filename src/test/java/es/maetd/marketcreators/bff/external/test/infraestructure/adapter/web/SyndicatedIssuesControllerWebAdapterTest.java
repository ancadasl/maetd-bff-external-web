package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

import com.auth0.jwt.interfaces.Claim;
import es.maetd.marketcreators.bff.external.application.mappers.SyndicatedIssuesApplicationMapper;
import es.maetd.marketcreators.bff.external.application.mappers.SyndicatedIssuesApplicationMapperImpl;
import es.maetd.marketcreators.bff.external.application.service.SyndicatedIssuesApplicationService;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.*;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.Role;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.web.SyndicatedIssuesControllerWebAdapter;
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
import org.openapitools.model.SyndicatedIssuesDataListWDTO;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class SyndicatedIssuesControllerWebAdapterTest {

    @Mock
    private SyndicatedIssuesApplicationService service;

    @Mock
    private SyndicatedIssuesApplicationMapper mapper;

    @InjectMocks
    private SyndicatedIssuesControllerWebAdapter controller;

    private static SyndicatedIssuesApplicationMapper mapperImpl;

    public static final String CSB = "csb";
    public static final String NUMBER_CSB = "0128";

    @BeforeAll
    static void setUp() {
        mapperImpl = new SyndicatedIssuesApplicationMapperImpl();
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
    void whenGetSyndicatedIssuesListIsCalled_thenResponseShouldBeOk() {

        // given
        Optional<Integer> year = Optional.of(2021);
        Optional<Integer> month = Optional.of(15);
        Optional<String> csb = Optional.of(NUMBER_CSB);

        SyndicatedIssuesDTO dto = createDummySyndicatedIssuesDto();
        SyndicatedIssuesResponseDTO resSyndicatedIssues =
                SyndicatedIssuesResponseDTO.builder().list(List.of(dto)).build();

        // when
        when(service.getSyndicatedIssues(any(GetSyndicatedIssuesIDTO.class))).thenReturn(resSyndicatedIssues);
        when(mapper.map(anyList())).thenReturn(mapperImpl.map(resSyndicatedIssues.getList()));

        // then
        ResponseEntity<SyndicatedIssuesDataListWDTO> response = controller.getSyndicatedIssues(csb, month, year);

        Assertions.assertEquals(
                resSyndicatedIssues.getList().get(0).getCsb(),
                Objects.requireNonNull(response.getBody())
                        .getSyndicatedIssues()
                        .get(0)
                        .getCsb());

        verify(service, times(1)).getSyndicatedIssues(any(GetSyndicatedIssuesIDTO.class));
        verify(mapper, times(1)).map(anyList());
    }

    public static SyndicatedIssuesDTO createDummySyndicatedIssuesDto() {
        SyndicatedIssuesDTO dto = new SyndicatedIssuesDTO();

        dto.setCsb(NUMBER_CSB);
        dto.setYear(2023L);
        dto.setMonth(1L);
        dto.setLetter(10L);
        dto.setBonds(5);
        return dto;
    }
}
