package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.EvaluationDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetEvaluationsIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.EvaluationStatus;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.types.ProductType;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.EvaluationEntity;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.EvaluationRepository;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.EvaluationRepositoryAdapter;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.EvaluationInfrastructureMapper;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.EvaluationInfrastructureMapperImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class EvaluationRepositoryAdapterTest {

    private static final int YEAR = 2022;

    private static final int MONTH = 2;

    private static final String CSB = "0125";

    @InjectMocks
    EvaluationRepositoryAdapter adapter;

    @Mock
    EvaluationRepository repository;

    @Mock
    EvaluationInfrastructureMapper mapper;

    private static EvaluationInfrastructureMapperImpl mapperImpl;

    @BeforeAll
    static void setUp() {
        mapperImpl = new EvaluationInfrastructureMapperImpl();
    }

    @Test
    void getEvaluationsShould_return_a_list_of_evaluations() {

        // Given
        EvaluationDto model = createDummyEvaluationDataDto();

        // When
        when(repository.findAll(any(Specification.class))).thenReturn(List.of(mapperImpl.map(model)));
        when(mapper.map(any(EvaluationEntity.class))).thenReturn(mapperImpl.map(mapperImpl.map(model)));

        GetEvaluationsIDTO idtoMock = new GetEvaluationsIDTO();

        idtoMock.setYear(YEAR);
        idtoMock.setMonth(MONTH);
        idtoMock.setCsb(CSB);

        List<EvaluationDto> result = adapter.getEvaluations(idtoMock);

        // Then
        assertEquals(model.getCsb(), result.get(0).getCsb());

        verify(repository, times(1)).findAll(any(Specification.class));
        verify(mapper, times(1)).map(any(EvaluationEntity.class));
    }

    public static EvaluationDto createDummyEvaluationDataDto() {
        EvaluationDto dto = new EvaluationDto();

        dto.setEvaluationId(1L);
        dto.setStatus(EvaluationStatus.FINISH_CONSOLIDATED_FINISHED);
        dto.setCsb("0128");
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
