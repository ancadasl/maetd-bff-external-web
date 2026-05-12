package es.maetd.marketcreators.bff.external.test.application.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import es.maetd.marketcreators.bff.external.application.port.client.MarketCreatorsPort;
import es.maetd.marketcreators.bff.external.application.service.dto.EntityDTO;
import es.maetd.marketcreators.bff.external.application.service.impl.MarketCreatorsApplicationServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MarketCreatorsApplicationServiceImplTest {

    @Mock
    private MarketCreatorsPort port;

    @InjectMocks
    private MarketCreatorsApplicationServiceImpl service;

    @Test
    void shouldReturnOnlyValidMarketCreators() {
        // Given
        EntityDTO dto = generateDummy();

        // When
        when(port.getCreators()).thenReturn(List.of(dto));

        List<EntityDTO> result = service.getMarketCreators();

        // Then
        assertEquals(dto.getId(), result.get(0).getId());

        verify(port, times(1)).getCreators();
        verifyNoMoreInteractions(port);
    }

    private EntityDTO generateDummy() {
        EntityDTO dto = new EntityDTO();
        dto.setId("1");
        dto.setCsb("0045");
        dto.setName("Bankinter");
        return dto;
    }
}
