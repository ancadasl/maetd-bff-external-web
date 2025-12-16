package es.maetd.marketcreators.bff.external.test.infraestructure.adapter.persistence.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.GetSyndicatedIssuesIDTO;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.dto.SyndicatedIssuesDto;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.SyndicatedIssuesEntity;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.SyndicatedIssuesRepository;
import es.maetd.marketcreators.bff.external.infrastructure.adapters.persistence.entity.repository.SyndicatedIssuesRepositoryAdapter;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.SyndicatedIssuesInfrastructureMapper;
import es.maetd.marketcreators.bff.external.infrastructure.mappers.SyndicatedIssuesInfrastructureMapperImpl;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class SyndicatedIssuesRepositoryAdapterTest {

    private static final int YEAR = 2022;

    private static final int MONTH = 2;

    private static final long CSB = 125L;

    @InjectMocks
    SyndicatedIssuesRepositoryAdapter adapter;

    @Mock
    SyndicatedIssuesRepository repository;

    @Mock
    SyndicatedIssuesInfrastructureMapper mapper;

    private static SyndicatedIssuesInfrastructureMapperImpl mapperImpl;

    @BeforeAll
    static void setUp() {
        mapperImpl = new SyndicatedIssuesInfrastructureMapperImpl();
    }

    @Test
    void getEvaluationsShould_return_a_list_of_evaluations() {

        // Given
        SyndicatedIssuesEntity entity = createDummySyndicatedIssuesEntity();

        // When
        when(repository.findAll(any(Specification.class))).thenReturn(List.of(entity));
        when(mapper.map(any(SyndicatedIssuesEntity.class))).thenReturn(mapperImpl.map(entity));

        GetSyndicatedIssuesIDTO idtoMock = new GetSyndicatedIssuesIDTO();

        idtoMock.setYear(YEAR);
        idtoMock.setMonth(MONTH);
        idtoMock.setCsb(CSB);

        List<SyndicatedIssuesDto> result = adapter.getSyndicatedIssues(idtoMock);

        // Then
        assertEquals(entity.getBonds(), result.get(0).getBonds());

        verify(repository, times(1)).findAll(any(Specification.class));
        verify(mapper, times(1)).map(any(SyndicatedIssuesEntity.class));
    }

    public static SyndicatedIssuesEntity createDummySyndicatedIssuesEntity() {
        SyndicatedIssuesEntity dto = new SyndicatedIssuesEntity();

        dto.setCsb(128L);
        dto.setYear(2023L);
        dto.setMonth("NOVIEMBRE");
        dto.setLi("10");
        dto.setBonds(5L);
        return dto;
    }
}
