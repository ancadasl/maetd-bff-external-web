package es.maetd.marketcreators.bff.external.test.application.service.impl;

import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.port.SyndicatedIssuesRepositoryPort;
import es.maetd.marketcreators.bff.external.application.service.dto.GetSyndicatedIssuesIDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.SyndicatedIssuesDTO;
import es.maetd.marketcreators.bff.external.application.service.dto.SyndicatedIssuesResponseDTO;
import es.maetd.marketcreators.bff.external.application.service.impl.SyndicatedIssuesApplicationServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SyndicatedIssuesApplicationServiceImplTest {

    public static final String NUMBER_CSB = "0128";

    private static final int YEAR = 2022;

    private static final int MONTH = 2;

    @Mock
    private SyndicatedIssuesRepositoryPort port;

    private SyndicatedIssuesApplicationServiceImpl service;

    @BeforeEach
    void init() {
        service = new SyndicatedIssuesApplicationServiceImpl(port);
    }

    @Test
    void getSyndicatedIssuesShould_return_a_list_of_syndicatedIssues() {

        // Given
        SyndicatedIssuesDTO syndicatedIssuesDTO = createDummySyndicatedIssuesDto();

        GetSyndicatedIssuesIDTO getSyndicatedIssuesIDTO = GetSyndicatedIssuesIDTO.builder()
                .month(MONTH)
                .year(YEAR)
                .csb(NUMBER_CSB)
                .build();

        // When
        when(port.getSyndicatedIssues(any(GetSyndicatedIssuesIDTO.class))).thenReturn(List.of(syndicatedIssuesDTO));

        SyndicatedIssuesResponseDTO result = service.getSyndicatedIssues(getSyndicatedIssuesIDTO);

        // Then
        Assertions.assertEquals(result.getList().get(0).getCsb(), syndicatedIssuesDTO.getCsb());

        verify(port, times(1)).getSyndicatedIssues(any(GetSyndicatedIssuesIDTO.class));
    }

    public static SyndicatedIssuesDTO createDummySyndicatedIssuesDto() {
        SyndicatedIssuesDTO dto = new SyndicatedIssuesDTO();

        dto.setCsb(NUMBER_CSB);
        dto.setYear(2023L);
        dto.setMonth(1L);
        dto.setExpiration(10.0);
        dto.setSize(5.5);
        return dto;
    }
}
