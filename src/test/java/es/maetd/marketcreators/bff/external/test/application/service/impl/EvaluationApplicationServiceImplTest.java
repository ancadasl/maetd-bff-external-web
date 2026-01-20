package es.maetd.marketcreators.bff.external.test.application.service.impl;

import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.port.EvaluationRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.dto.*;
import es.maetd.marketcreators.bff.external.application.service.dto.types.EvaluationStatus;
import es.maetd.marketcreators.bff.external.application.service.dto.types.ProductType;
import es.maetd.marketcreators.bff.external.application.service.impl.EvaluationApplicationServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EvaluationApplicationServiceImplTest {

    public static final String CSB_123 = "CSB123";

    private static final int YEAR = 2022;

    @Mock
    private EvaluationRepositoryPort port;

    private EvaluationApplicationServiceImpl service;

    @BeforeEach
    void init() {
        service = new EvaluationApplicationServiceImpl(port);
    }

    @Test
    void getEvaluationShould_return_a_list_of_evaluations() {

        // Given
        EvaluationDTO evaluationDTO = createDummyEvaluationDataDto();

        GetEvaluationsIDTO getEvaluationsIDTO = GetEvaluationsIDTO.builder()
                .productType(ProductType.BOND)
                .year(YEAR)
                .csb(CSB_123)
                .build();

        // When
        when(port.getEvaluations(any(GetEvaluationsIDTO.class))).thenReturn(List.of(evaluationDTO));

        EvaluationResponseDTO result = service.getEvaluations(getEvaluationsIDTO);

        // Then
        Assertions.assertEquals(result.getList().get(0).getEvaluationId(), evaluationDTO.getEvaluationId());

        verify(port, times(1)).getEvaluations(any(GetEvaluationsIDTO.class));
    }

    @Test
    void getEvaluationShould_return_a_list_of_evaluations_joint() {

        // Given
        EvaluationJointDTO evaluationJointDTO = createDummyEvaluationJointDataDto();

        GetEvaluationsIDTO getEvaluationsIDTO = GetEvaluationsIDTO.builder()
                .productType(ProductType.BOND)
                .year(YEAR)
                .csb(CSB_123)
                .build();

        // When
        when(port.getEvaluationsJoint(any(GetEvaluationsIDTO.class))).thenReturn(List.of(evaluationJointDTO));

        EvaluationJointResponseDTO result = service.getEvaluationsJoint(getEvaluationsIDTO);

        // Then
        Assertions.assertEquals(result.getList().get(0).getCsb(), evaluationJointDTO.getCsb());

        verify(port, times(1)).getEvaluationsJoint(any(GetEvaluationsIDTO.class));
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
        dto.setBond(5L);
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

    public static EvaluationJointDTO createDummyEvaluationJointDataDto() {
        return new EvaluationJointDTO(
                EvaluationStatus.FINISH_CONSOLIDATED_FINISHED, "128", 2023L, 1L, 15L, 15L, 10L, 5L);
    }
}
